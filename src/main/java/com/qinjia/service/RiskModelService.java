package com.qinjia.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qinjia.mapper.RiskModelMapper;
import com.qinjia.pojo.TRiskCashRes1;
import com.qinjia.pojo.TRiskCreditLevel1;
import com.qinjia.pojo.ZwResultset;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/20
 */
@Service
@DS("riskModel")
public class RiskModelService {
    @Resource
    private RiskModelMapper riskModelMapper;

    @DS("loanJD")
    List<TRiskCreditLevel1> selectCountByUserId(Long userId) {
        return riskModelMapper.selectCountByUserId(userId);
    }

    @DS("loanJD")
    TRiskCreditLevel1 selectOneLimitAmount(Double approvedAmount, String applyDate,Long userId) {
        return riskModelMapper.selectOneLimitAmount(approvedAmount, applyDate,userId);
    }

    @DS("loanJD")
    TRiskCreditLevel1 selectOneById(String id) {
        return riskModelMapper.selectOneById(id);
    }

    @DS("loanJD")
    int insertTRiskCreditLevel1(List<TRiskCreditLevel1> tRiskCreditLevel1List) {
        return riskModelMapper.insertTRiskCreditLevel1(tRiskCreditLevel1List);
    }
    @DS("loanJD")
    int updateTRiskCreditLevel1(List<TRiskCreditLevel1> tRiskCreditLevel1List) {
        return riskModelMapper.updateTRiskCreditLevel1(tRiskCreditLevel1List);
    }
    @DS("loanJD")
    int updateTRiskCreditLevel1One(TRiskCreditLevel1 tRiskCreditLevel1) {
        return riskModelMapper.updateTRiskCreditLevel1One(tRiskCreditLevel1);
    }
    @DS("loanJD")
    int updateTRiskCreditLevel1Entity(TRiskCreditLevel1 tRiskCreditLevel1) {
        return riskModelMapper.updateTRiskCreditLevel1Entity(tRiskCreditLevel1);
    }
    @DS("loanJD")
    int insertTRiskCashRes1(List<TRiskCashRes1> tRiskCashRes1List) {
        return riskModelMapper.insertTRiskCashRes1(tRiskCashRes1List);
    }
    @DS("loanJD")
    int selectTRiskCashResByLoanNo(String loanNo){
        return riskModelMapper.selectTRiskCashResByLoanNo(loanNo);
    }

    List<Integer> selectZWSucc328() {
        return riskModelMapper.selectZWSucc328();
    }

    List<Integer> selectZWFail328() {
        return riskModelMapper.selectZWFail328();
    }

    List<Integer> selectZWFailRemark(Integer resultsetId) {
        return riskModelMapper.selectZWFailRemark(resultsetId);
    }
    List<Integer> selectZWSucc327() {
        return riskModelMapper.selectZWSucc327();
    }

    List<Integer> selectZWFail327() {
        return riskModelMapper.selectZWFail327();
    }


    int insertZW(List<ZwResultset> zwResultsetList) {
        return riskModelMapper.insertZW(zwResultsetList);
    }

    @DS("riskModel")
    int updateZwResultestByBatchNo(ZwResultset zwResultset) {
        return riskModelMapper.updateZwResultestByBatchNo(zwResultset);
    }

}
