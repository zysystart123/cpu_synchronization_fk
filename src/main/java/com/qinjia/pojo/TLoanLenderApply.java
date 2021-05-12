package com.qinjia.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class TLoanLenderApply {
    private Long id;
    private String lenderType;
    private Long subAcctId;
    private Long custId;
    private String loanNo;
    private String fundOrderId;
    private String idType;
    private String idNo;
    private String accountName;
    private String accountNo;
    private String accountType;
    private String accountBranchNo;
    private String accountBrandName;
    private String accountProvince;
    private String accountCity;
    private Date applyTime;
    private Double lenderAmount;
    private String lenderStatus;
    private Date finishTime;
    private String fundNo;
    private String assetNo;
    private Integer version;
    private Date createTime;
    private Date updateTime;
    private String remark;
}
