package com.qinjia.mapper;

import com.qinjia.pojo.TCustBaseInfo;
import com.qinjia.pojo.TCustUser;
import com.qinjia.pojo.TRiskCreditLevel1;
import com.qinjia.pojo.ZwUser;
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

public interface LoanJDMapper {

    // 根据用户(user_id)未结清放款订单总金额
    // @Select("select IFNULL(sum(lender_amt),0) from t_loan_new loan LEFT JOIN t_cust_base_info info on loan.cust_id = info.id where info.user_id = #{userId} and loan.settle_time is null;")
    @Select("select \n" +
            "IFNULL(SUM(case when planned_capital = indeed_capital then 0 else planned_capital end),0) as overLenderAmt \n" +
            " from (\n" +
            "select \n" +
            "loan_no,sum(planned_capital) planned_capital ,sum(case when plan.repay_time <= NOW() then planned_capital END) as indeed_capital\n" +
            "from t_loan_plan plan left JOIN t_cust_base_info info on plan.cust_id = info.id  \n" +
            "left join t_cust_user user on user.id = info.user_id\n" +
            "where user.id=#{userId}\n" +
            "group by loan_no\n" +
            ") tmp")
    Double selectOverLenderAmt(@Param("userId") Long userId);

    // 根据传入的参数得到t_cust_user_real表数据
    @Select("select * from t_cust_user_real where remark ='' or remark is null limit #{num}")
    //补数专用
    //@Select("select * from t_cust_user_real where remark ='' or remark is null order by id desc limit #{num}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userNo", column = "user_no"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "certType", column = "cert_type"),
            @Result(property = "certNo", column = "cert_no"),
            @Result(property = "regProv", column = "reg_prov"),
            @Result(property = "isValid", column = "is_valid"),
            @Result(property = "version", column = "version"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "remark", column = "remark"),
    })
    List<TCustUser> selectTCustUserS(@Param("num") int num);

    // 根据传入的参数得到t_cust_base_info_real表数据
    List<TCustBaseInfo> selectTCustBaseInfo(@Param("list") List<TCustUser> tCustUserList);

    //将要插入t_cust_user表的数据在t_cust_user_real中打标记
    int updateTCustUserS(@Param(value = "list") List<TCustUser> tCustUserList);

    //将要插入t_cust_base_info表的数据在t_cust_base_info_real中打标记
    int updateTCustBaseInfoS(@Param(value = "list") List<TCustBaseInfo> tCustBaseInfoList);

    //将从t_cust_user_real表中得到的数据插入t_cust_user表
    int insertTCustUserS(@Param(value = "list") List<TCustUser> tCustUserList);

    //将从t_cust_base_info_real表中得到的数据插入t_cust_base_info表
    int insertTCustBaseInfoS(@Param(value = "list") List<TCustBaseInfo> tCustBaseInfoList);

    @Select("select \n" +
            "user.cert_no as idNumber,\n" +
            "user.user_name as customerName,\n" +
            "info.phone as phoneNumber \n" +
            "from t_cust_base_info info INNER JOIN t_cust_user user on  info.user_id = user.id  where user.id = #{userId} limit 1;")
    ZwUser selectZwUser(@Param("userId") String userId);

}
