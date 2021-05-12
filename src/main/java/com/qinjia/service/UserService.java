package com.qinjia.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qinjia.pojo.TCustBaseInfo;
import com.qinjia.pojo.TCustUser;
import com.qinjia.pojo.TRiskCreditLevel1;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/25
 */
@Log4j2
@Service
public class UserService {
    @Autowired
    private LoanJDService loanJDService;

    @DS("loanJD")
    @Transactional
    public void insertAndUpdate(List<TCustBaseInfo> tCustBaseInfoList, List<TCustUser> tCustUserList) {
        //插入用户及更新备注
        log.info("开始插入t_cust_base_info及更新t_cust_base_info_real------------");
        loanJDService.insertTCustBaseInfoS(tCustBaseInfoList);
        loanJDService.updateTCustBaseInfoS(tCustBaseInfoList);
        log.info("插入t_cust_base_info及更新t_cust_base_info_real结束------------" + tCustUserList.size());
        log.info("开始插入t_cust_user及更新t_cust_user_real------------");
        loanJDService.insertTCustUserS(tCustUserList);
        loanJDService.updateTCustUserS(tCustUserList);
        log.info("插入t_cust_user及更新t_cust_user_real结束------------" + tCustBaseInfoList.size());
    }
}
