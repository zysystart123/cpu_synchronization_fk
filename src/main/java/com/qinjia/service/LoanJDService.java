package com.qinjia.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qinjia.mapper.LoanJDMapper;
import com.qinjia.pojo.TCustBaseInfo;
import com.qinjia.pojo.TCustUser;
import com.qinjia.pojo.ZwUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/20
 */
@Service
@DS("loanJD")
public class LoanJDService {
    @Resource
    private LoanJDMapper loanJDMapper;

    Double selectOverLenderAmt(Long userId) {
        return loanJDMapper.selectOverLenderAmt(userId);
    }

    List<TCustUser> selectTCustUserS(int num) {
        return loanJDMapper.selectTCustUserS(num);
    }

    List<TCustBaseInfo> selectTCustBaseInfo(List<TCustUser> tCustUserList) {
        return loanJDMapper.selectTCustBaseInfo(tCustUserList);
    }

    int updateTCustUserS(List<TCustUser> tCustUserList) {
        return loanJDMapper.updateTCustUserS(tCustUserList);
    }

    int updateTCustBaseInfoS(List<TCustBaseInfo> tCustBaseInfoList) {
        return loanJDMapper.updateTCustBaseInfoS(tCustBaseInfoList);
    }

    int insertTCustUserS(List<TCustUser> tCustUserList) {
        return loanJDMapper.insertTCustUserS(tCustUserList);
    }

    int insertTCustBaseInfoS(List<TCustBaseInfo> tCustBaseInfoList) {
        return loanJDMapper.insertTCustBaseInfoS(tCustBaseInfoList);
    }
    ZwUser selectZwUser(String userId){
        return loanJDMapper.selectZwUser(userId);
    }

}
