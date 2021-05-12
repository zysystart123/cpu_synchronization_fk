package com.qinjia.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qinjia.mapper.CpuProMapper;
import com.qinjia.pojo.ZwUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/20
 */
@Service
@DS("cpuPro")
public class CpuProService {
    @Resource
    private CpuProMapper cpuProMapper;


    Long selectUserIdByCustId(Long custId){
        return cpuProMapper.selectUserIdByCustId(custId);
    }

    ZwUser selectZwUser(String userId){
        return cpuProMapper.selectZwUser(userId);
    }

    public Long selectAmtByNameID(Long id,String name){
        return cpuProMapper.selectAmtByNameID(id,name);}
}
