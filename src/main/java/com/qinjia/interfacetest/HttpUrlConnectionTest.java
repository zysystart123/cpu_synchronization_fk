package com.qinjia.interfacetest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author sy
 * @create 2021-05-12 15:00
 */
public class HttpUrlConnectionTest {

    public static String getNetData(String urlStr) {
        HttpURLConnection conn = null;

        //连接成功后我们是要读取数据的   所以要有一个输入流
        InputStream inputStream = null;

        // 因为读取的都是文本信息  所以使用BufferedReader
        BufferedReader bufferedReader = null;

        //StringBuilder来把接收到的数据拼接起来
        StringBuilder result = new StringBuilder();
        try {
            // 读取初始url 并且创建对象
            URL url = new URL(urlStr);
            //打开url连接
            conn = (HttpURLConnection) url.openConnection();
            //设置连接
            //请求的方法
            conn.setRequestMethod("GET");
            //设置主机连接超时(单位：毫秒)
            // 发送请求端 连接到 url目标地址端的时间   受距离长短和网络速度的影响
            conn.setConnectTimeout(15000);
            //设置从主机读取数据超时（单位：毫秒)
            // 连接成功后 获取数据的时间   受数据量和服务器处理数据的影响
            conn.setReadTimeout(60000);

            //设置请求参数  可以指定接收json参数  服务端的key为content-type
            conn.setRequestProperty("Accept", "application/json");
            //发送请求
//            conn.connect();

            //获取响应码 如果响应码不为200 表示请求不成功
            if (conn.getResponseCode() != 200) {
                //todo  此处应该增加异常处理手段
                return "请求失败！！！";
            }
            //获取响应码 如果响应码为200 表示请求成功 然后可以读取数据
            //获取输入流  然后读取数据
            inputStream = conn.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            //逐行读取数据
            String line;//用来读取数据
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
                //System.out.print(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭各种流
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String urlStr = "https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5";//以这个网址为例
        System.out.println(HttpUrlConnectionTest.getNetData(urlStr));

    }
}