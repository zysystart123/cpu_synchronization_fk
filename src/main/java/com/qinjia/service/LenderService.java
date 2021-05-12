package com.qinjia.service;

import com.qinjia.pojo.*;
import com.qinjia.utils.getDate;
import com.qinjia.utils.getRandomNum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/21
 */
@Log4j2
@Service
public class LenderService {
    @Autowired
    private CpuProService cpuProService;

    @Autowired
    private RiskModelService riskModelService;

    @Value("${delete.remark}")
    private String remark;
    /**
     * 根据放款成功日志，生成成功提现日志
     */
    public void lenderSuccess(List<TLoanLenderApply> insertDate) {
        log.info("开始生成成功提现日志------------");
        List<TRiskCashRes1> tRiskCashRes1List = new ArrayList<>();
        for (int i = 0; i < insertDate.size(); i++) {
           // String yearMon = getDate.getYearMon();
            TLoanLenderApply tLoanLenderApply = insertDate.get(i);
            //得到生产的 userId
            Long userId = cpuProService.selectUserIdByCustId(tLoanLenderApply.getCustId());
            TRiskCashRes1 tRiskCashRes1 = new TRiskCashRes1();
            tRiskCashRes1.setLoanNo(tLoanLenderApply.getLoanNo());
            tRiskCashRes1.setApplyTime(tLoanLenderApply.getApplyTime());
            tRiskCashRes1.setApplyAmt(tLoanLenderApply.getLenderAmount().toString());
            tRiskCashRes1.setUserId(userId.toString());
            tRiskCashRes1.setRejectReason("");
            tRiskCashRes1.setRemark(remark);
            tRiskCashRes1.setCreateTime(tLoanLenderApply.getCreateTime());
            tRiskCashRes1List.add(tRiskCashRes1);
        }
//        List<ZwResultset> zwResultsetList = lenderResultsetSucc(tRiskCashRes1List);
//        riskModelService.insertZW(zwResultsetList);
//        log.info("插入328提现成功引擎日志结束------------" + zwResultsetList.size());

        List<TRiskCashRes1> tRiskCashRes1ListNew = new ArrayList<>();
        for (int i = 0; i < tRiskCashRes1List.size(); i++) {
            int i1 = riskModelService.selectTRiskCashResByLoanNo(tRiskCashRes1List.get(i).getLoanNo());
            if (i1==0){
                tRiskCashRes1ListNew.add(tRiskCashRes1List.get(i));
            }
        }
        if (tRiskCashRes1ListNew.size()>0){
            riskModelService.insertTRiskCashRes1(tRiskCashRes1ListNew);
            List<ZwResultset> zwResultsetList = lenderResultsetSucc(tRiskCashRes1ListNew);
            riskModelService.insertZW(zwResultsetList);
            log.info("插入328提现成功引擎日志结束------------" + zwResultsetList.size());
        }else{
            log.info("成功提现日志已存在，本次不插入------------");
            log.info("328提现成功引擎日志本次不插入------------");
        }

        log.info("插入成功提现日志结束------------" + tRiskCashRes1ListNew.size());
    }

    /**
     * 根据放款失败日志，生成失败提现日志
     */
    public void lenderFailure(List<TLoanLenderApply> insertDate) {
        log.info("开始生成失败提现日志------------");
        List<TRiskCashRes1> tRiskCashRes1List = new ArrayList<>();
        for (int i = 0; i < insertDate.size(); i++) {
           // String yearMon = getDate.getYearMon();
            TLoanLenderApply tLoanLenderApply = insertDate.get(i);
            //得到生产的 userId
            Long userId = cpuProService.selectUserIdByCustId(tLoanLenderApply.getCustId());
            TRiskCashRes1 tRiskCashRes1 = new TRiskCashRes1();
            tRiskCashRes1.setLoanNo(tLoanLenderApply.getLoanNo());
            tRiskCashRes1.setApplyTime(tLoanLenderApply.getApplyTime());
            tRiskCashRes1.setApplyAmt(tLoanLenderApply.getLenderAmount().toString());
            tRiskCashRes1.setUserId(userId.toString());
            tRiskCashRes1.setRejectReason("根据决策引擎修改");
            tRiskCashRes1.setRemark(remark);
            tRiskCashRes1.setCreateTime(tLoanLenderApply.getCreateTime());
            tRiskCashRes1List.add(tRiskCashRes1);
        }
//        List<ZwResultset> zwResultsetList = lenderResultsetFail(tRiskCashRes1List);
//        riskModelService.insertZW(zwResultsetList);
//        log.info("插入328提现拒绝引擎日志结束------------" + zwResultsetList.size());
        List<TRiskCashRes1> tRiskCashRes1ListNew = new ArrayList<>();
        for (int i = 0; i < tRiskCashRes1List.size(); i++) {
            int i1 = riskModelService.selectTRiskCashResByLoanNo(tRiskCashRes1List.get(i).getLoanNo());
            if (i1==0){
                tRiskCashRes1ListNew.add(tRiskCashRes1List.get(i));
            }
        }
        if (tRiskCashRes1ListNew.size()>0){
            riskModelService.insertTRiskCashRes1(tRiskCashRes1ListNew);
            List<ZwResultset> zwResultsetList = lenderResultsetFail(tRiskCashRes1ListNew);
            riskModelService.insertZW(zwResultsetList);
            log.info("插入328提现拒绝引擎日志结束------------" + zwResultsetList.size());
        }else{
            log.info("失败提现日志已存在，本次不插入------------");
            log.info("328提现拒绝引擎日志本次不插入------------");
        }
        log.info("插入失败提现日志结束------------" + tRiskCashRes1ListNew.size());
    }

    /**
     * 根据提现成功日志，生成328提现成功引擎日志
     */
    public  List<ZwResultset> lenderResultsetSucc(List<TRiskCashRes1> tRiskCashRes1List) {
        log.info("开始生成328提现成功引擎日志------------");
        List<Integer> integers = riskModelService.selectZWSucc328();
        List<ZwResultset> zwResultsetList = new ArrayList<>();
        for (int i = 0; i < tRiskCashRes1List.size(); i++) {
            TRiskCashRes1 tRiskCashRes1 = tRiskCashRes1List.get(i);
            ZwUser zwUser = cpuProService.selectZwUser(tRiskCashRes1.getUserId());
            ZwResultset zwResultset = new ZwResultset();
            zwResultset.setBatchNo(tRiskCashRes1.getLoanNo());
            zwResultset.setEngineId(328);
            zwResultset.setEngineVer(1258);
            zwResultset.setSubVer(2);
            zwResultset.setEngineName("自营APP-提现触发人脸决策规则");
            zwResultset.setEngineCode("9bb0e31b-db3b-4c9b-b0d7-d93536490206");
            zwResultset.setType(1);
            zwResultset.setAuditResult("通过");
            zwResultset.setCreditLimit(0);
            zwResultset.setStatus("成功");
            zwResultset.setIdNumber(zwUser.getIdNumber());
            zwResultset.setCustomerName(zwUser.getCustomerName());
            zwResultset.setPhoneNumber(zwUser.getPhoneNumber());
            zwResultset.setCreateTime(tRiskCashRes1.getApplyTime());
            zwResultset.setResultsetId(getRandomNum.getRandomId(integers));
            zwResultset.setUserId(Long.parseLong(tRiskCashRes1.getUserId()));
            zwResultset.setImporttype(Integer.parseInt(remark));
            zwResultsetList.add(zwResultset);
        }
        log.info("生成328提现成功引擎日志结束------------" + zwResultsetList.size());
        return zwResultsetList;
    }

    /**
     * 根据提现失败日志，生成328提现失败引擎日志
     */
    public  List<ZwResultset> lenderResultsetFail(List<TRiskCashRes1> tRiskCashRes1List) {
        log.info("开始生成328提现失败引擎日志------------");
        List<Integer> integers = riskModelService.selectZWFail328();
        List<ZwResultset> zwResultsetList = new ArrayList<>();
        for (int i = 0; i < tRiskCashRes1List.size(); i++) {
            TRiskCashRes1 tRiskCashRes1 = tRiskCashRes1List.get(i);
            ZwUser zwUser = cpuProService.selectZwUser(tRiskCashRes1.getUserId());
            ZwResultset zwResultset = new ZwResultset();
            zwResultset.setBatchNo(tRiskCashRes1.getLoanNo());
            zwResultset.setEngineId(328);
            zwResultset.setEngineVer(1258);
            zwResultset.setSubVer(2);
            zwResultset.setEngineName("自营APP-提现触发人脸决策规则");
            zwResultset.setEngineCode("9bb0e31b-db3b-4c9b-b0d7-d93536490206");
            zwResultset.setType(1);
            zwResultset.setAuditResult("拒绝");
            zwResultset.setCreditLimit(0);
            zwResultset.setStatus("成功");
            zwResultset.setIdNumber(zwUser.getIdNumber());
            zwResultset.setCustomerName(zwUser.getCustomerName());
            zwResultset.setPhoneNumber(zwUser.getPhoneNumber());
            zwResultset.setCreateTime(tRiskCashRes1.getApplyTime());
            zwResultset.setResultsetId(getRandomNum.getRandomId(integers));
            zwResultset.setUserId(Long.parseLong(tRiskCashRes1.getUserId()));
            zwResultset.setImporttype(Integer.parseInt(remark));
            zwResultsetList.add(zwResultset);
            //log.info("更新提现拒绝理由------------" );
            if (getRandomNum.getRandomLenderFail()){
                tRiskCashRes1List.get(i).setRejectReason("资金方拒绝");
            }else{
                List<Integer> integers1 = riskModelService.selectZWFailRemark(zwResultset.getResultsetId());
                String strByIntArr = getRandomNum.getStrByIntArr(integers1);
                tRiskCashRes1List.get(i).setRejectReason(strByIntArr);
            }

        }
        log.info("生成328提现失败引擎日志结束------------" + zwResultsetList.size());
        return zwResultsetList;
    }
}
