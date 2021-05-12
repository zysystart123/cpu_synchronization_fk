package com.qinjia.mapper;

import com.qinjia.pojo.ZwUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * @author TYJ
 * @note ****
 * @date 2021/1/20
 */

public interface CpuProMapper {

    //根据cust_id获取t_cust_base_info的user_id
    @Select("select user_id from t_cust_base_info where id =#{custId}")
    Long selectUserIdByCustId(@Param("custId") Long custId);

    @Select("select \n" +
            "user.cert_no as idNumber,\n" +
            "user.user_name as customerName,\n" +
            "info.phone as phoneNumber \n" +
            "from t_cust_base_info info INNER JOIN t_cust_user user on  info.user_id = user.id  where user.id = #{userId} limit 1;")
    ZwUser selectZwUser(@Param("userId") String  userId);

    // 根据id,name 获取cash_max_lender_amt
    @Select("select IFNULL(ROUND(max(lender_amt),0),0) as cash_max_lender_amt from cpu.t_loan loan   \n" +
            " left join cpu.t_cust_base_info info on loan.cust_id = info.id \n" +
            " left join cpu.t_cust_user user on info.user_id = user.id \n" +
            " where user.cert_no = #{id} and user_name = #{name}")
    Long selectAmtByNameID(@Param("id") Long id,@Param("name") String name);
}
