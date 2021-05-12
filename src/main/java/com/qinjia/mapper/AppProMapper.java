package com.qinjia.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * @author TYJ
 * @note ****
 * @date 2021/1/20
 */

public interface AppProMapper {
    // 根据user_id 获取 login_success_user_num_with_same_ip_10m
    @Select("SELECT COUNT(DISTINCT user_id) as login_success_user_num_with_same_ip_10m \n" +
            " FROM t_user_login_log  \n" +
            " WHERE login_ip= \n" +
            "(SELECT  ip \n" +
            "FROM t_risk_behavior_ext  \n" +
            "WHERE user_id=#{user_id} AND content_id=4   \n" +
            "ORDER BY update_time DESC LIMIT 1\n" +
            ")\n" +
            "AND login_type=\"2\" AND create_time>=  DATE_SUB(NOW(),INTERVAL 10 MINUTE )")
    Long selectSuccessNumByUserId(@Param("user_id") Long user_id);
}
