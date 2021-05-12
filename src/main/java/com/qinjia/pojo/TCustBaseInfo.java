package com.qinjia.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class TCustBaseInfo {
    private Long id;
    private String fundNo;
    private String assetNo;
    private String custNo;
    private String custName;
    private Long userId;
    private String certType;
    private String certNo;
    private String phone;
    private String sex;
    private String country;
    private String folk;
    private Date birthday;
    private String qq;
    private String email;
    private String wechat;
    private String edu;
    private String isValid;
    private Integer version;
    private Date createTime;
    private Date updateTime;
    private String auth;
    private String remark;
    private Integer rownum;
}
