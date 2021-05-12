package com.qinjia.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/21
 */
@Data
public class TRiskCashRes1 {
    private String loanNo;
    private Date applyTime;
    private String applyAmt;
    private String userId;
    private String rejectReason;
    private String remark;
    private Date createTime;
    private Integer judge;


}
