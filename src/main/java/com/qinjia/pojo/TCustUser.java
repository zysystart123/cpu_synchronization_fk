package com.qinjia.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class TCustUser {
    private Long id;
    private String userNo;
    private String userName;
    private String sex;
    private String certType;
    private String certNo;
    private String regProv;
    private String isValid;
    private Integer version;
    private Date createTime;
    private Date updateTime;
    private String remark;
}
