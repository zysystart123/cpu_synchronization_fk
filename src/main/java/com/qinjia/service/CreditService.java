package com.qinjia.service;

import com.qinjia.pojo.*;
import com.qinjia.utils.DateUtil;
import com.qinjia.utils.getRandomNum;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Log4j2
@Service
public class CreditService {
    @Autowired
    private CpuProService cpuProService;

    @Autowired
    private LoanJDService loanJDService;

    @Autowired
    private RiskModelService riskModelService;

    @Autowired
    private UserService userService;

    @Value("${delete.remark}")
    private String remark;

    /**
     * 根据t_loan表的用户，生成授信日志,返回要新增或者更新的授信日志
     */
    public List<TRiskCreditLevel1> creditAmt(List<Object> insertDate) {


        log.info("开始生成新增或者更新的授信日志------------");
        List<TRiskCreditLevel1> tRiskCreditLevel1List = new ArrayList<>();
        for (int i = 0; i < insertDate.size(); i++) {
            TLoan tLoan = (TLoan) insertDate.get(i);
            //得到生产cpu.t_loan 的 userId
            Long userId = cpuProService.selectUserIdByCustId(tLoan.getCustId());
            //得到尽调库loan未结清放款订单总金额
            Double overLenderAmt = loanJDService.selectOverLenderAmt(userId);
            //生成授信额度
            double creditAmt = overLenderAmt + tLoan.getApplyAmt() + getRandomNum.getRandomAmt();
            log.info("生成授信额度------------" + creditAmt);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            TRiskCreditLevel1 tRiskCreditLevel1 = new TRiskCreditLevel1();
            tRiskCreditLevel1.setId(uuid);
            tRiskCreditLevel1.setUserId(userId);
            tRiskCreditLevel1.setProductCode("100209");
            tRiskCreditLevel1.setApplyDate(tLoan.getApplyTime());
            tRiskCreditLevel1.setApprovedAmount(creditAmt);
            tRiskCreditLevel1.setState(90);
            tRiskCreditLevel1.setIsFristApply(0);

            tRiskCreditLevel1List.add(tRiskCreditLevel1);
        }
        log.info("生成新增或者更新的授信日志结束------------" + tRiskCreditLevel1List.size());
        return tRiskCreditLevel1List;
    }

    /**
     * 根据生成授信日志，跟t_risk_credit_level比较，进行更新或插入操作，返回新增的授信日志
     */
    public List<TRiskCreditLevel1> creditLog(List<TRiskCreditLevel1> list) {
        log.info("开始插入新增或者更新的授信日志------------");
        List<TRiskCreditLevel1> tRiskCreditLevel1ListInsert = new ArrayList<>();
        List<TRiskCreditLevel1> tRiskCreditLevel1ListUpdate = new ArrayList<>();
        Map<String, Double> diffAmt = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            TRiskCreditLevel1 tRiskCreditLevel1 = list.get(i);
            //判断该用户在授信日志表近一年内是否有记录，有则更新，无则新增
            List<TRiskCreditLevel1> listByUserid = riskModelService.selectCountByUserId(tRiskCreditLevel1.getUserId());
            if (listByUserid.size() == 0) {
                tRiskCreditLevel1ListInsert.add(tRiskCreditLevel1);
            } else if (listByUserid.size() == 1) {
                //如果原授信额度小于新的授信额度，进行更新
                if (listByUserid.get(0).getApprovedAmount() < tRiskCreditLevel1.getApprovedAmount()) {
                    tRiskCreditLevel1.setId(listByUserid.get(0).getId());
                    tRiskCreditLevel1.setApplyDate(listByUserid.get(0).getApplyDate());
                    tRiskCreditLevel1ListUpdate.add(tRiskCreditLevel1);
                    //额度差值计算
                    Double nowApprovedAmount = tRiskCreditLevel1.getApprovedAmount();
                    Double historyApprovedAmount = listByUserid.get(0).getApprovedAmount();
                    diffAmt.put(tRiskCreditLevel1.getId(), nowApprovedAmount - historyApprovedAmount);
                }
            }
        }
        log.info("新增的授信日志条数------------" + tRiskCreditLevel1ListInsert.size());
        log.info("更新已有的授信日志条数------------" + tRiskCreditLevel1ListUpdate.size());
        log.info("额度差值数据------------" + diffAmt.toString());
        if (tRiskCreditLevel1ListInsert.size() >= 1) {
            creditUserLevel(tRiskCreditLevel1ListInsert);
            riskModelService.insertTRiskCreditLevel1(tRiskCreditLevel1ListInsert);
            List<ZwResultset> zwResultsetList = creditResultsetSucc(tRiskCreditLevel1ListInsert, "cpuPro");
            riskModelService.insertZW(zwResultsetList);
            log.info("插入327授信成功引擎日志结束------------" + zwResultsetList.size());
        }
        if (tRiskCreditLevel1ListUpdate.size() >= 1) {
            //将当前月数据与历史数据区分开
            creditUserLevel(tRiskCreditLevel1ListUpdate);
            ArrayList<TRiskCreditLevel1> curMonTRiskCreditLevel1 = new ArrayList<>();
            ArrayList<TRiskCreditLevel1> hisMonTRiskCreditLevel1 = new ArrayList<>();
            for (TRiskCreditLevel1 tRiskCreditLevel1 : tRiskCreditLevel1ListUpdate) {
                String dateFormt = DateUtil.format(tRiskCreditLevel1.getApplyDate());
                if (DateUtil.isThisMonth(DateUtil.Datestr2timestmp(dateFormt, "yyyy-MM-dd"))) {
                    curMonTRiskCreditLevel1.add(tRiskCreditLevel1);
                } else {
                    hisMonTRiskCreditLevel1.add(tRiskCreditLevel1);
                }
            }

            //更新当前月数据
            if (curMonTRiskCreditLevel1.size() > 0) {
                riskModelService.updateTRiskCreditLevel1(curMonTRiskCreditLevel1);
                log.info("更新当前月数据end------------" + curMonTRiskCreditLevel1.size());
            }

            //更新历史数据
            if (hisMonTRiskCreditLevel1.size() > 0) {
                for (TRiskCreditLevel1 tRiskCreditLevel1 : hisMonTRiskCreditLevel1) {
                    Double amountDiff = diffAmt.get(tRiskCreditLevel1.getId());//额度差值
                    String applyDate = DateUtil.format(tRiskCreditLevel1.getApplyDate(), "yyyy-MM-dd HH:mm:ss");
                    //判断未用信数据是否存在 批核额度>额度差值
                    TRiskCreditLevel1 notTRiskCreditLevel1 = riskModelService.selectOneLimitAmount(amountDiff+1000, applyDate,tRiskCreditLevel1.getUserId());
                    if (notTRiskCreditLevel1 != null) {
                        riskModelService.updateTRiskCreditLevel1One(tRiskCreditLevel1);
                        ZwResultset zwResultset = new ZwResultset();
                        zwResultset.setBatchNo(tRiskCreditLevel1.getId());
                        zwResultset.setCreditLimit(tRiskCreditLevel1.getApprovedAmount());
                        zwResultset.setDatilResult("决策选项:"+tRiskCreditLevel1.getApprovedAmount().toString());
                        riskModelService.updateZwResultestByBatchNo(zwResultset);
                        notTRiskCreditLevel1.setApprovedAmount(notTRiskCreditLevel1.getApprovedAmount() - amountDiff);
                        List<TRiskCreditLevel1> isLevel = new ArrayList<>();
                        isLevel.add(notTRiskCreditLevel1);
                        creditUserLevel(isLevel);
                        riskModelService.updateTRiskCreditLevel1One(notTRiskCreditLevel1);
                        zwResultset.setBatchNo(notTRiskCreditLevel1.getId());
                        zwResultset.setCreditLimit(notTRiskCreditLevel1.getApprovedAmount());
                        zwResultset.setDatilResult("决策选项:"+notTRiskCreditLevel1.getApprovedAmount().toString());
                        riskModelService.updateZwResultestByBatchNo(zwResultset);
                        log.info("更新历史数据end额度差值存在------add------" + tRiskCreditLevel1.toString());
                        log.info("更新历史数据end额度差值存在------diff------" + notTRiskCreditLevel1.toString());


                    } else {
                        TRiskCreditLevel1 rawTRiskCreditLevel1 = riskModelService.selectOneById(tRiskCreditLevel1.getId());
                        rawTRiskCreditLevel1.setSourceId("1");
                        riskModelService.updateTRiskCreditLevel1Entity(rawTRiskCreditLevel1);
                        log.info("更新历史数据end额度差值不存在------------" + curMonTRiskCreditLevel1.size());

                    }
                }
            }

        }
        log.info("插入新增的授信日志结束------------" + tRiskCreditLevel1ListInsert.size());
        log.info("更新已有的授信日志结束------------" + tRiskCreditLevel1ListUpdate.size());
        return tRiskCreditLevel1ListInsert;
    }

    /**
     * 根据已经新增的授信日志，增加未用信记录
     */
    public void creditNoLog(List<TRiskCreditLevel1> list) {
        log.info("开始插入新未用信记录------------");
        List<TRiskCreditLevel1> tRiskCreditLevel1List = new ArrayList<>();
        //返回true，新增
        boolean random = getRandomNum.getRandomNoLog();
        if (random) {
            //要随机新增的条数
            int random1 = getRandomNum.getRandom(list.size());
            log.info("随机增加未用信记录条数------------" + random1);
            if (random1 > 0) {
                //获取条数
                List<TCustUser> tCustUserList = loanJDService.selectTCustUserS(random1);
                List<TCustBaseInfo> tCustBaseInfoList = loanJDService.selectTCustBaseInfo(tCustUserList);
                log.info("查到t_cust_user条数------------" + tCustUserList.size());
                log.info("查到t_cust_base_info条数------------" + tCustBaseInfoList.size());
                //如果已经新增的授信日志的条数等于要新增的未用信记录
                if (random1 <= list.size()) {
                    for (int i = 0; i < list.size(); i++) {
                        TRiskCreditLevel1 tRisk = new TRiskCreditLevel1();
                        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                        tRisk.setId(uuid);
                        tRisk.setApprovedAmount(getRandomNum.getRandom(list.get(i).getApprovedAmount()));
                        tRisk.setApplyDate(getRandomNum.getRandom(list.get(i).getApplyDate()));
                        tRisk.setUserId(tCustBaseInfoList.get(i).getUserId());
                        tRisk.setProductCode("100209");
                        tRisk.setIsFristApply(0);
                        tRisk.setState(91);
                        tRiskCreditLevel1List.add(tRisk);
                    }
                } else if (random1 > list.size()) {
                    for (int i = 0; i < random1; i++) {
                        TRiskCreditLevel1 tRisk = new TRiskCreditLevel1();
                        if (i < list.size()) {
                            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                            tRisk.setId(uuid);
                            tRisk.setApprovedAmount(getRandomNum.getRandom(list.get(i).getApprovedAmount()));
                            tRisk.setApplyDate(getRandomNum.getRandom(list.get(i).getApplyDate()));
                            tRisk.setUserId(tCustBaseInfoList.get(i).getUserId());
                            tRisk.setProductCode("100209");
                            tRisk.setState(91);
                            tRisk.setIsFristApply(0);
                        } else {
                            int randomIndex = getRandomNum.getRandomIndex(list.size());
                            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                            tRisk.setId(uuid);
                            tRisk.setApprovedAmount(getRandomNum.getRandom(list.get(randomIndex).getApprovedAmount()));
                            tRisk.setApplyDate(getRandomNum.getRandom(list.get(randomIndex).getApplyDate()));
                            tRisk.setUserId(tCustBaseInfoList.get(i).getUserId());
                            tRisk.setProductCode("100209");
                            tRisk.setIsFristApply(0);
                            tRisk.setState(91);
                        }
                        tRiskCreditLevel1List.add(tRisk);
                    }
                }
                //修改获取的用户表的备注
                for (int i = 0; i < tCustUserList.size(); i++) {
                    //String yearMon = getDate.getYearMon();
                    tCustUserList.get(i).setRemark(remark);
                    tCustBaseInfoList.get(i).setRemark(remark);
                }
                //插入用户及更新备注
                userService.insertAndUpdate(tCustBaseInfoList, tCustUserList);


                //插入未用信记录
                creditUserLevel(tRiskCreditLevel1List);
                List<ZwResultset> zwResultsetList = creditResultsetSucc(tRiskCreditLevel1List, "loanJD");
                riskModelService.insertZW(zwResultsetList);
                log.info("插入327授信成功引擎日志结束------------" + zwResultsetList.size());

                riskModelService.insertTRiskCreditLevel1(tRiskCreditLevel1List);
                log.info("插入新未用信记录结束------------" + tRiskCreditLevel1List.size());

            }
        } else {
            log.info("插入新未用信记录结束------------" + tRiskCreditLevel1List.size());
        }

    }


    /**
     * 根据已经新增的授信日志，增加授信拒绝用户
     */
    public void creditNo(List<TRiskCreditLevel1> list) {
        log.info("开始插入授信拒绝用户------------");
        List<TRiskCreditLevel1> tRiskCreditLevel1List = new ArrayList<>();

        //要随机新增的条数
        int random1 = getRandomNum.getRandomNo(list.size());
        //获取条数
        List<TCustUser> tCustUserList = loanJDService.selectTCustUserS(random1);
        List<TCustBaseInfo> tCustBaseInfoList = loanJDService.selectTCustBaseInfo(tCustUserList);
        //如果已经新增的授信日志的条数等于要新增的未用信记录
        if (random1 <= list.size()) {
            for (int i = 0; i < list.size(); i++) {
                TRiskCreditLevel1 tRisk = new TRiskCreditLevel1();
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                tRisk.setId(uuid);
                tRisk.setApplyDate(getRandomNum.getRandom(list.get(i).getApplyDate()));
                tRisk.setUserId(tCustBaseInfoList.get(i).getUserId());
                tRisk.setProductCode("100209");
                tRisk.setState(92);
                //tRisk.setRejectReason("根据决策引擎修改");

                String creditNoUser = getRandomNum.getCreditNoUser();
                tRisk.setLevel(creditNoUser);
                tRisk.setSimpleLevel(creditNoUser.substring(0, 1));
                tRisk.setIsFristApply(0);
                tRisk.setApprovedAmount(0.0);
                tRiskCreditLevel1List.add(tRisk);
            }
        } else if (random1 > list.size()) {
            for (int i = 0; i < random1; i++) {
                TRiskCreditLevel1 tRisk = new TRiskCreditLevel1();
                if (i < list.size()) {
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    tRisk.setId(uuid);
                    tRisk.setApplyDate(getRandomNum.getRandom(list.get(i).getApplyDate()));
                    tRisk.setUserId(tCustBaseInfoList.get(i).getUserId());
                    tRisk.setProductCode("100209");
                    tRisk.setState(92);
                    //tRisk.setRejectReason("根据决策引擎修改");
                    String creditNoUser = getRandomNum.getCreditNoUser();
                    tRisk.setLevel(creditNoUser);
                    tRisk.setSimpleLevel(creditNoUser.substring(0, 1));
                    tRisk.setApprovedAmount(0.0);
                    tRisk.setIsFristApply(0);
                } else {
                    int randomIndex = getRandomNum.getRandomIndex(list.size());
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    tRisk.setId(uuid);
                    tRisk.setApplyDate(getRandomNum.getRandom(list.get(randomIndex).getApplyDate()));
                    tRisk.setUserId(tCustBaseInfoList.get(i).getUserId());
                    tRisk.setProductCode("100209");
                    tRisk.setState(92);
                    //tRisk.setRejectReason("根据决策引擎修改");

                    String creditNoUser = getRandomNum.getCreditNoUser();
                    tRisk.setLevel(creditNoUser);
                    tRisk.setSimpleLevel(creditNoUser.substring(0, 1));
                    tRisk.setApprovedAmount(0.0);
                    tRisk.setIsFristApply(0);
                }
                tRiskCreditLevel1List.add(tRisk);
            }
        }
        //修改获取的用户表的备注
        for (int i = 0; i < tCustUserList.size(); i++) {
            // String yearMon = getDate.getYearMon();
            tCustUserList.get(i).setRemark(remark);
            tCustBaseInfoList.get(i).setRemark(remark);
        }


        userService.insertAndUpdate(tCustBaseInfoList, tCustUserList);

        List<ZwResultset> zwResultsetList = creditResultsetFail(tRiskCreditLevel1List);
        riskModelService.insertZW(zwResultsetList);
        log.info("插入327授信拒绝引擎日志结束------------" + zwResultsetList.size());

        //插入授信拒绝用户
        riskModelService.insertTRiskCreditLevel1(tRiskCreditLevel1List);
        log.info("插入授信拒绝用户结束------------" + tRiskCreditLevel1List.size());


    }


    //根据增加记录授信额度 计算用户等级
    public List<TRiskCreditLevel1> creditUserLevel(List<TRiskCreditLevel1> list) {

        for (int i = 0; i < list.size(); i++) {
            Double approvedAmount = list.get(i).getApprovedAmount();
            if (0 <= approvedAmount && approvedAmount <= 4400.0) {
                list.get(i).setLevel("D1");
                list.get(i).setSimpleLevel("D");
            } else if (4400.0 < approvedAmount && approvedAmount <= 7100.0) {
                list.get(i).setLevel("D2");
                list.get(i).setSimpleLevel("D");
            } else if (7100.0 < approvedAmount && approvedAmount <= 8600.0) {
                list.get(i).setLevel("D3");
                list.get(i).setSimpleLevel("D");
            } else if (8600.0 < approvedAmount && approvedAmount <= 10100.0) {
                list.get(i).setLevel("D4");
                list.get(i).setSimpleLevel("D");
            } else if (10100.0 < approvedAmount && approvedAmount <= 11500.0) {
                list.get(i).setLevel("E1");
                list.get(i).setSimpleLevel("E");
            } else if (11500.0 < approvedAmount && approvedAmount <= 13800.0) {
                list.get(i).setLevel("E2");
                list.get(i).setSimpleLevel("E");
            } else if (13800.0 < approvedAmount && approvedAmount <= 16300.0) {
                list.get(i).setLevel("E3");
                list.get(i).setSimpleLevel("E");
            } else if (16300.0 < approvedAmount) {
                list.get(i).setLevel("E4");
                list.get(i).setSimpleLevel("E");
            }
        }
        return list;
    }

    /**
     * 根据新增的授信成功日志，生成327提现授信成功引擎日志
     */
    public List<ZwResultset> creditResultsetSucc(List<TRiskCreditLevel1> tRiskCreditLevel1List, String datasource) {
        log.info("开始生成327授信成功引擎日志------------");
        List<Integer> integers = riskModelService.selectZWSucc327();
        List<ZwResultset> zwResultsetList = new ArrayList<>();
        for (int i = 0; i < tRiskCreditLevel1List.size(); i++) {
            TRiskCreditLevel1 tRiskCreditLevel1 = tRiskCreditLevel1List.get(i);
            ZwUser zwUser = new ZwUser();
            if (datasource.equals("cpuPro")) {
                zwUser = cpuProService.selectZwUser(tRiskCreditLevel1.getUserId().toString());
            } else {
                zwUser = loanJDService.selectZwUser(tRiskCreditLevel1.getUserId().toString());
            }
            ZwResultset zwResultset = new ZwResultset();
            zwResultset.setBatchNo(tRiskCreditLevel1.getId());
            zwResultset.setEngineId(327);
            zwResultset.setEngineVer(1261);
            zwResultset.setSubVer(13);
            zwResultset.setEngineName("自营APP-授信决策规则");
            zwResultset.setEngineCode("1c000bce-5519-4cd3-85e7-112d4c302ed5");
            zwResultset.setType(1);
            zwResultset.setAuditResult("通过");
            zwResultset.setCreditLimit(tRiskCreditLevel1.getApprovedAmount());
            zwResultset.setDatilResult("决策选项:" + tRiskCreditLevel1.getApprovedAmount());
            zwResultset.setStatus("成功");
            zwResultset.setIdNumber(zwUser.getIdNumber());
            zwResultset.setCustomerName(zwUser.getCustomerName());
            zwResultset.setPhoneNumber(zwUser.getPhoneNumber());
            zwResultset.setCreateTime(tRiskCreditLevel1.getApplyDate());
            zwResultset.setResultsetId(getRandomNum.getRandomId(integers));
            zwResultset.setUserId(Long.parseLong(tRiskCreditLevel1.getUserId().toString()));
            zwResultset.setImporttype(411381);
            zwResultsetList.add(zwResultset);
        }
        log.info("生成327授信成功引擎日志结束------------" + zwResultsetList.size());
        return zwResultsetList;
    }


    /**
     * 根据新增的授信失败日志，生成327提现授信失败引擎日志
     */
    public List<ZwResultset> creditResultsetFail(List<TRiskCreditLevel1> tRiskCreditLevel1List) {
        log.info("开始生成327授信拒绝引擎日志及更新提现拒绝理由------------");
        List<Integer> integers = riskModelService.selectZWFail327();
        List<ZwResultset> zwResultsetList = new ArrayList<>();
        for (int i = 0; i < tRiskCreditLevel1List.size(); i++) {
            TRiskCreditLevel1 tRiskCreditLevel1 = tRiskCreditLevel1List.get(i);
            ZwUser zwUser = loanJDService.selectZwUser(tRiskCreditLevel1.getUserId().toString());
            ZwResultset zwResultset = new ZwResultset();
            zwResultset.setBatchNo(tRiskCreditLevel1.getId());
            zwResultset.setEngineId(327);
            zwResultset.setEngineVer(1261);
            zwResultset.setSubVer(13);
            zwResultset.setEngineName("自营APP-授信决策规则");
            zwResultset.setEngineCode("1c000bce-5519-4cd3-85e7-112d4c302ed5");
            zwResultset.setType(1);
            zwResultset.setAuditResult("拒绝");
            zwResultset.setCreditLimit(tRiskCreditLevel1.getApprovedAmount());
            zwResultset.setDatilResult("决策选项:" + tRiskCreditLevel1.getApprovedAmount());
            zwResultset.setStatus("成功");
            zwResultset.setIdNumber(zwUser.getIdNumber());
            zwResultset.setCustomerName(zwUser.getCustomerName());
            zwResultset.setPhoneNumber(zwUser.getPhoneNumber());
            zwResultset.setCreateTime(tRiskCreditLevel1.getApplyDate());
            zwResultset.setResultsetId(getRandomNum.getRandomId(integers));
            zwResultset.setUserId(Long.parseLong(tRiskCreditLevel1.getUserId().toString()));
            zwResultset.setImporttype(411381);
            zwResultsetList.add(zwResultset);
            List<Integer> integers1 = riskModelService.selectZWFailRemark(zwResultset.getResultsetId());
            String strByIntArr = getRandomNum.getStrByIntArr(integers1);
            tRiskCreditLevel1List.get(i).setRejectReason(strByIntArr);
        }
        log.info("生成327授信拒绝引擎日志结束------------" + zwResultsetList.size());
        return zwResultsetList;
    }
}
