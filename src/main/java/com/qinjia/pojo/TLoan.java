package com.qinjia.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class TLoan {
    private Long id;
    private String loanNo;
    private String loanStatus;
    private String fundLoanStatus;
    private String lockFlag;
    private Long subAcctId;
    private Long custId;
    private String custNo;
    private String assetNo;
    private String assetTransNo;
    private String assetProductNo;
    private String assetProductName;
    private String fundTransNo;
    private String fundNo;
    private String fundOrderNo;
    private String fundCustNo;
    private String fundLoanNo;
    private String fundProductNo;
    private String fundProductName;
    private String fundContractNo;
    private Double fundLoanAmt;
    private String fundBankInstNo;
    private String fundBankInstName;
    private String fundBankProductNo;
    private String fundBankProductName;
    private String productRootId;
    private String productCategoryId;
    private String productCategoryCode;
    private String productCategoryName;
    private String applyNo;
    private Double applyAmt;
    private Integer applyPhase;
    private String applyPhaseUnit;
    private Date applyTime;
    private String applyPurpose;
    private Double premiumAmt;
    private String safeguardType;
    private String premiumPayWay;
    private String lenderMethod;
    private Date lenderTime;
    private Double lenderAmt;
    private Long lenderCardId;
    private Long repayCardId;
    private String repayWay;
    private Date loanStartDate;
    private Date loanEndDate;
    private Date repayStartDate;
    private Date repayEndDate;
    private Double dailyChargeRatio;
    private Double dailyInterstRatio;
    private Double firstRepayAmt;
    private Date firstRepayDate;
    private Date mthRepayDate;
    private Double mthRepayAmt;
    private Date interestCalcDate;
    private Date settleTime;
    private String piccRiskAppId;
    private Integer version;
    private Date createTime;
    private Date updateTime;
    private String remark;
    private String path;
    private String guaranteeOrganization;
    private String loanPurposeDesc;
    private Double yearInterstRatio;
    private Integer withdrawState;
    private Date withdrawTime;
    private Date auditTime;
    private String withdrawFailedInfo;
}
