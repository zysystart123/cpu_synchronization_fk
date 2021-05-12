package com.qinjia.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qinjia.mapper.AppProMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/20
 */
@Service
@DS("appPro")
public class AppProService {
    @Resource
    private AppProMapper appProMapper;

    public Long selectSuccessNumByUserId(Long user_id) {
        return appProMapper.selectSuccessNumByUserId(user_id);}
}
