package com.qinjia.listening;

import com.alibaba.fastjson.JSON;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.qinjia.domain.synchronous;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Log4j2
@Component
public class BinLogListener implements BinaryLogClient.EventListener {
    @Resource
    private synchronous synchronous;

    private String database = ""; //记录数据库
    private String table = "";  // 记录表名
    private long tableId = 0L;  // 记录表ID

    @Override
    public void onEvent(Event event) {
        EventData data = event.getData();

        if (data instanceof TableMapEventData) {
            database = ((TableMapEventData) data).getDatabase();
            table = ((TableMapEventData) data).getTable();
            tableId = ((TableMapEventData) data).getTableId();
            // log.info("********************************* 数据库操作： " + database + "." + table + " *********************************");
        }
        if (data instanceof UpdateRowsEventData) {
            //如果日志是更新操作 && !table.equals("t_loan_repay_log")&& !table.equals("t_base_trans_log")
            long updateTableId = ((UpdateRowsEventData) data).getTableId();
            if (database.equals("cpu") && database != null && tableId == updateTableId && table.equals("t_loan_lender_apply")) {
                String res = JSON.toJSONString(((UpdateRowsEventData) data).getRows());
                synchronous.update(table, res);
            }
            database = "";
            table = "";
            tableId = 0L;
        } else if (data instanceof WriteRowsEventData) {
            //如果日志是写入操作
            long writeTableId = ((WriteRowsEventData) data).getTableId();
            if (database.equals("cpu") && database != null && tableId == writeTableId && table.equals("t_loan")) {
                String res = JSON.toJSONString(((WriteRowsEventData) data).getRows());
                synchronous.insert(table, res);
            }
            database = "";
            table = "";
            tableId = 0L;
        }
    }
}
