package com.qinjia.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/21
 */
@Data
public class ZwResultset {
    private Integer id;
    private String batchNo;
    private String input;
    private Integer engineId;
    private Integer engineVer;
    private Integer subVer;
    private String engineName;
    private String engineCode;
    private Integer type;
    private String auditResult;
    private double creditScore;
    private double creditLimit;
    private String status;
    private String datilResult;
    private String assetNo;
    private String assetCode;
    private String channel;
    private String idNumber;
    private String customerName;
    private String phoneNumber;
    private Date createTime;
    private Integer resultsetId;
    private Long userId;
    private Integer importtype;
}
