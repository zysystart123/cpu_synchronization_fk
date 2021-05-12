package com.qinjia.interfacetest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class CallPort {

    // 设置连接超时时间
    private static int connectTimeOut = 5000;
    // 设置读取超时时间
    private static int readTimeOut = 10000;
    // 设置请求编码格式
    private static String requestEncoding = "utf-8";

    // get set 方法
    public static int getConnectTimeOut() {
        return CallPort.connectTimeOut;
    }

    public static int getReadTimeOut() {
        return CallPort.readTimeOut;
    }

    public static String getRequestEncoding() {
        return requestEncoding;
    }

    public static void setConnectTimeOut(int connectTimeOut) {
        CallPort.connectTimeOut = connectTimeOut;
    }

    public static void setReadTimeOut(int readTimeOut) {
        CallPort.readTimeOut = readTimeOut;
    }

    public static void setRequestEncoding(String requestEncoding) {
        CallPort.requestEncoding = requestEncoding;
    }

    /**
     * <pre>	 * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     *                   parameters 入参
     *                   recvEncoding 编码
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String doPost(String reqUrl, Map<String, String> parameters, String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        // 拼接入参的各个字段以及字段值
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext(); ) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), CallPort.requestEncoding));
                params.append("&");
            }

            // 删除最后一个连接符 &
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            // 读取初始化URL，将url转化成URL对象
            URL url = new URL(reqUrl);
            // 打开URL连接
            url_con = (HttpURLConnection) url.openConnection();
            // 设置连接
            url_con.setRequestMethod("POST");
            // 设置请求头
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(CallPort.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(CallPort.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时


            url_con.setDoOutput(true);// 默认是false
            byte[] b = params.toString().getBytes();
            // 发送URL请求
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            // 根据入参从数据接口接受数据
            InputStream in = url_con.getInputStream();
            //responseContent =CallHFWServices.InputStream2String(in, "UTF-8");
            // 将输入流放入缓冲数组内
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
            // 按行读取缓冲数组内的内容
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }


    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("conid", "1021");
        map.put("stype", "1");
        map.put("appkey", "f5efc58896eae979");
        map.put("n", "龙耀华");
        map.put("id", "450302198210080539");
        //map.put("msg", "牟宗祥,370704193510140814");
        String temp = CallPort.doPost("http://testapi.lawxp.com/s.aspx", map, "utf-8");
        System.out.println("返回的消息是:" + temp);
    }

}
