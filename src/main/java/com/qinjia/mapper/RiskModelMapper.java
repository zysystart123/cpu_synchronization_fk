package com.qinjia.mapper;

import com.qinjia.pojo.TRiskCashRes1;
import com.qinjia.pojo.TRiskCreditLevel1;
import com.qinjia.pojo.ZwResultset;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/20
 */
public interface RiskModelMapper {
    // 根据用户(user_id)查询t_risk_credit_level表是否有近一年历史记录
    @Select("select * from t_risk_credit_level where user_id = #{userId} and apply_date BETWEEN DATE_SUB(NOW(),INTERVAL 1 YEAR) and now() order by apply_date desc limit 1; ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "sourceId", column = "source_id"),
            @Result(property = "productCode", column = "product_code"),
            @Result(property = "fundCode", column = "fund_code"),
            @Result(property = "applyDate", column = "apply_date"),
            @Result(property = "approvedAmount", column = "approved_amount"),
            @Result(property = "state", column = "state"),
            @Result(property = "stateTime", column = "state_time"),
            @Result(property = "channelTime", column = "channel_time"),
            @Result(property = "rejectReason", column = "reject_reason"),
            @Result(property = "rejectReasonDescr", column = "reject_reason_descr"),
            @Result(property = "forbidDate", column = "forbid_date"),
            @Result(property = "deviceFingerprint", column = "device_fingerprint"),
            @Result(property = "rejectSource", column = "reject_source"),
            @Result(property = "level", column = "level"),
            @Result(property = "simpleLevel", column = "simple_level"),
            @Result(property = "isFristApply", column = "is_frist_apply"),
    })
    List<TRiskCreditLevel1> selectCountByUserId(@Param("userId") Long userId);

    // 根据授信数量获取对应同等数量且approved_amount的size
    @Select("select * from t_risk_credit_level where state = 91 and apply_date BETWEEN concat(SUBSTR(date_add(#{applyDate}, interval - day(#{applyDate}) + 1 day), 1, 10) ,' 00:00:00') and concat(last_day(#{applyDate}) ,' 23:59:59') and approved_amount > #{approvedAmount}  and user_id != #{userId} limit 1; ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "sourceId", column = "source_id"),
            @Result(property = "productCode", column = "product_code"),
            @Result(property = "fundCode", column = "fund_code"),
            @Result(property = "applyDate", column = "apply_date"),
            @Result(property = "approvedAmount", column = "approved_amount"),
            @Result(property = "state", column = "state"),
            @Result(property = "stateTime", column = "state_time"),
            @Result(property = "channelTime", column = "channel_time"),
            @Result(property = "rejectReason", column = "reject_reason"),
            @Result(property = "rejectReasonDescr", column = "reject_reason_descr"),
            @Result(property = "forbidDate", column = "forbid_date"),
            @Result(property = "deviceFingerprint", column = "device_fingerprint"),
            @Result(property = "rejectSource", column = "reject_source"),
            @Result(property = "level", column = "level"),
            @Result(property = "simpleLevel", column = "simple_level"),
            @Result(property = "isFristApply", column = "is_frist_apply"),
    })
    TRiskCreditLevel1 selectOneLimitAmount(@Param("approvedAmount") Double approvedAmount, @Param("applyDate") String applyDate, @Param("userId") Long userId);

    // 根据授信数量获取对应同等数量且approved_amount的size
    @Select("select * from t_risk_credit_level where id = #{id}; ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "sourceId", column = "source_id"),
            @Result(property = "productCode", column = "product_code"),
            @Result(property = "fundCode", column = "fund_code"),
            @Result(property = "applyDate", column = "apply_date"),
            @Result(property = "approvedAmount", column = "approved_amount"),
            @Result(property = "state", column = "state"),
            @Result(property = "stateTime", column = "state_time"),
            @Result(property = "channelTime", column = "channel_time"),
            @Result(property = "rejectReason", column = "reject_reason"),
            @Result(property = "rejectReasonDescr", column = "reject_reason_descr"),
            @Result(property = "forbidDate", column = "forbid_date"),
            @Result(property = "deviceFingerprint", column = "device_fingerprint"),
            @Result(property = "rejectSource", column = "reject_source"),
            @Result(property = "level", column = "level"),
            @Result(property = "simpleLevel", column = "simple_level"),
            @Result(property = "isFristApply", column = "is_frist_apply"),
    })
    TRiskCreditLevel1 selectOneById(@Param("id") String id);

    // t_risk_credit_level 新增
    int insertTRiskCreditLevel1(@Param(value = "list") List<TRiskCreditLevel1> tRiskCreditLevel1List);

    // t_risk_credit_level 更新
    int updateTRiskCreditLevel1(@Param(value = "list") List<TRiskCreditLevel1> tRiskCreditLevel1List);

    // t_risk_credit_level 更新
    int updateTRiskCreditLevel1One(TRiskCreditLevel1 tRiskCreditLevel1);

    // t_risk_credit_level 更新
    int updateTRiskCreditLevel1Entity(TRiskCreditLevel1 tRiskCreditLevel1);

    // t_risk_cash_res 新增
    int insertTRiskCashRes1(@Param(value = "list") List<TRiskCashRes1> tRiskCashRes1List);

    // 查询zw_resultset表input字段不为空的
    @Select("select id from zw_resultset_real where engine_id =328 and audit_result = '通过' and input is not null;")
    List<Integer> selectZWSucc328();

    // 查询zw_resultset表input字段不为空的
    @Select("select id from zw_resultset_real where engine_id =328 and audit_result = '拒绝' and input is not null and id not in (\n" +
            "select resultset_id from zw_resultset_list where   remark = '10061'\n" +
            ");")
    List<Integer> selectZWFail328();

    // 查询zw_resultset_list表拒绝原因
    @Select("select remark from zw_resultset_list where resultset_id = #{resultsetId} and remark != '10061';")
    List<Integer> selectZWFailRemark(@Param(value = "resultsetId") Integer resultsetId);

    // 查询zw_resultset表input字段不为空的
    @Select("select id from zw_resultset_real where engine_id =327 and audit_result = '通过' and input is not null;")
    List<Integer> selectZWSucc327();

    // 查询zw_resultset表input字段不为空的
    @Select("select id from zw_resultset_real where engine_id =327 and audit_result = '拒绝' and input is not null;")
    List<Integer> selectZWFail327();

    int insertZW(@Param(value = "list") List<ZwResultset> zwResultsetList);

    // 根据batchNo 更新 creditLimit、datilResult
    int updateZwResultestByBatchNo(ZwResultset zwResultset);

    @Select("select count(1) from t_risk_cash_res where loan_no =#{loanNo}")
    int selectTRiskCashResByLoanNo(@Param("loanNo") String loanNo);
}
