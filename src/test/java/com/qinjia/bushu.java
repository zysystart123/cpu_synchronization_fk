package com.qinjia;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class bushu {

    private static String File = "C:\\Users\\12489\\Desktop\\update.txt"; //要读取sql的目录
    private static long time = 300; //读取一条休眠的毫秒数
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String CONNECTION_URL = "jdbc:mysql://rm-hp3qgn64g7i3s4yujco.mysql.huhehaote.rds.aliyuncs.com/dbus";
    private static final String USERNAME = "qindbuser";
    private static final String PASSWORD = "M99ZrvCWwhTma88j";
    private static Connection con;
    private static PreparedStatement ps;

    public static void main(String[] args) {
        BufferedReader reader = null;
        List<String> list = new ArrayList();
        int count = 0;
        long start = System.currentTimeMillis(); //获取结束时间
        long end = System.currentTimeMillis(); //获取结束时间
        try {
            reader = new BufferedReader(new FileReader(File));
            String sql = "";
            while ((sql = reader.readLine()) != null) {
//                list.add(sql);
//                if (list.size() > 20 || (end - start) > 10000) {
//                    getResultSetMetaData(sql);
//                    start= System.currentTimeMillis(); //重新读取
//                    count+=list.size();
//                }
                getResultSetMetaData(sql);
                count += 1;
                //               end = System.currentTimeMillis(); //获取结束时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("已经插入数量为" + count+"    当前时间为"+sdf.format(new Date()));
                Thread.sleep(time);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static {
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement stmt, Connection con) throws SQLException {
        if (stmt != null)
            stmt.close();
        if (con != null)
            con.close();
    }

    public static void getResultSetMetaData(String sql) throws SQLException {

        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
