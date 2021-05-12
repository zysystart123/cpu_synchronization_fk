package com.qinjia.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/20
 */
@Data
public class TRiskCreditLevel1 {
    private String id;
    private Long userId;
    private String sourceId;
    private String productCode;
    private String fundCode;
    private Date applyDate;
    private Double approvedAmount;
    private int state;
    private Date stateTime;
    private Date channelTime;
    private String rejectReason;
    private String rejectReasonDescr;
    private Date forbidDate;
    private String deviceFingerprint;
    private String rejectSource;
    private String level;
    private String simpleLevel;
    private int isFristApply;

}
