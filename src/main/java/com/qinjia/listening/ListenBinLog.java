package com.qinjia.listening;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Log4j2
@Component
public class ListenBinLog implements ApplicationRunner {
    @Value("${spring.fromMysql.hostname}")
    private String hostname;
    @Value("${spring.fromMysql.port}")
    private int port;
    @Value("${spring.fromMysql.username}")
    private String username;
    @Value("${spring.fromMysql.password}")
    private String password;
    @Resource
    private BinLogListener binLogListener;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("开始进行数据同步");
        // 配置连接(指定数据库的地址,端口号,账号,密码)
        BinaryLogClient logClient = new BinaryLogClient(hostname,port,username,password);

        logClient.registerEventListener(binLogListener);
        try {
            // 开始连接监听
            logClient.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
