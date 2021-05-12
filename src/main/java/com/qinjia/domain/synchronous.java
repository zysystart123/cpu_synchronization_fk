package com.qinjia.domain;


import com.qinjia.pojo.TLoan;
import com.qinjia.pojo.TLoanLenderApply;
import com.qinjia.pojo.TRiskCreditLevel1;
import com.qinjia.service.CreditService;
import com.qinjia.service.LenderService;
import com.qinjia.utils.getData;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Log4j2
@Component
public class synchronous {
    @Resource
    private CreditService creditService;
    @Resource
    private LenderService lenderService;

    public void update(String table, String data) {
        if (table.equals("t_loan_lender_apply")) {
            List<Object> updateDate = new ArrayList<>();
            log.info("--------------cpu." + table + "更新操作-------------------");
            log.info(table + "原更新数据为:" + data);
            updateDate = getData.getUpdateDate(TLoanLenderApply.class, data);
            List<TLoanLenderApply> successList = new ArrayList<>();
            List<TLoanLenderApply> failureList = new ArrayList<>();
            for (int i = 0; i < updateDate.size(); i++) {
                TLoanLenderApply tLoanLenderApply = (TLoanLenderApply) updateDate.get(i);
                //放款成功
                if (tLoanLenderApply.getLenderStatus().equals("2")) {
                    successList.add(tLoanLenderApply);
                } else if (tLoanLenderApply.getLenderStatus().equals("4")) {
                    failureList.add(tLoanLenderApply);
                }
            }
            if (successList.size()>0){
                lenderService.lenderSuccess(successList);
            }
            if (failureList.size()>0){
                lenderService.lenderFailure(failureList);
            }
        }
    }

    public void insert(String table, String data) {
        if (table.equals("t_loan")) {
            List<Object> insertDate = new ArrayList<>();
            log.info("--------------cpu." + table + "新增操作-------------------");
            log.info(table + "原新增数据为:" + data);
            insertDate = getData.getInsertDate(TLoan.class, data);

            long startTime = System.currentTimeMillis(); //获取结束时间
            //根据接受到的实时数据，返回此次要新增或者更新的授信日志
            List<TRiskCreditLevel1> tRiskCreditLevel1Amt = creditService.creditAmt(insertDate);

            log.info("---------end----------");
            //将上一步新增或者更新的授信日志进行补充用户等级并进行新增或者修改，返回新增的数据
            List<TRiskCreditLevel1> tRiskCreditLevel1sLog = creditService.creditLog(tRiskCreditLevel1Amt);
            log.info("---------end----------");

            //根据返回的新增的数据，增加未用信记录,并更新新增用户表,有新增，增加，无新增，不增加
            if (tRiskCreditLevel1sLog.size() > 0) {
                creditService.creditNoLog(tRiskCreditLevel1sLog);
                log.info("---------end----------");
            }

            //根据返回的新增的数据，增加授信拒绝用户,有新增，增加，无新增，不增加
            if (tRiskCreditLevel1sLog.size() > 0) {
                creditService.creditNo(tRiskCreditLevel1sLog);
                log.info("---------end----------");
            }

            log.info("根据t_loan实时同步结束");
            long endTime = System.currentTimeMillis(); //获取结束时间
            log.info(" 耗时" + (endTime - startTime) + "ms");
        }
    }

    public void delete(String table, String data) {

    }

}
