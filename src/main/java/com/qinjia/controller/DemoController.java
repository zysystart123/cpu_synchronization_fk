package com.qinjia.controller;

import com.alibaba.fastjson.JSONObject;
import com.qinjia.common.Result;
import com.qinjia.service.AppProService;
import com.qinjia.service.CpuProService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname DemoController
 * @Description TODO
 * @Date 2021/5/11 11:34
 * @Created by 86153
 */
@RestController
public class DemoController {
    @Resource
     private CpuProService cpuProService;
    @Resource
    private AppProService appProService;


    @GetMapping("/hello")
    public Result hello( String demo){
        System.err.println("suyao----->"+demo);
        Map map = new HashMap();
        map.put("cash_max_lender_amt",0);
        map.put("login_success_user_num_with_same_ip_10m",0);
        return  Result.resultFunction(map);
    }

    @PostMapping("/test001")
    public Result test3(@RequestBody String demo){
        JSONObject jsonObject = JSONObject.parseObject(demo);
        long id = Long.parseLong(jsonObject.get("id").toString());
        String name = jsonObject.get("name").toString();

        long user_id = Long.parseLong(jsonObject.get("user_id").toString());
        System.err.println("suyao----->"+demo);

        Long AmtByNameID = cpuProService.selectAmtByNameID(id, name);
        Long SuccessNum = appProService.selectSuccessNumByUserId(user_id);

        Map map = new HashMap();
        map.put("cash_max_lender_amt",AmtByNameID);
        map.put("login_success_user_num_with_same_ip_10m",SuccessNum);


        return  Result.resultFunction(map);
    }

}
