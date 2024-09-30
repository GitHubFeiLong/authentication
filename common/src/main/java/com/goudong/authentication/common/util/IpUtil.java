package com.goudong.authentication.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 获取请求对象的IP地址
 * @author msi
 */
public class IpUtil {

    /**
     * 获取请求的IP地址以long类型返回
     * @param request   请求对象
     * @return  long类型IP地址
     */
    public static long getLongIp(HttpServletRequest request) {
        String ipString = IpUtil.getStringIp(request);

        return IpUtil.ipToLong(ipString);
    }

    /**
     * 获取Ip地址
     * @param request 请求对象
     * @return ip地址
     */
    public static String getStringIp(HttpServletRequest request) {
        String xIp = request.getHeader("X-Real-IP");
        if (StringUtil.isNotBlank(xIp)) {
            return xIp;
        }
        String xFor = request.getHeader("X-Forwarded-For");
        if(StringUtil.isNotBlank(xFor) && !"unknown".equalsIgnoreCase(xFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xFor.indexOf(",");
            if(index != -1){
                return xFor.substring(0,index);
            }else{
                return xFor;
            }
        }

        return "localhost";
    }

    /**
     * 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
     * 对应MySQL的INET_ATON函数
     * @param strIp 形如 127.0.0.1字符串IP地址
     * @return  long类型IP
     */
    public static long ipToLong(String strIp){
        long[] ip = new long[4];

        if (strIp.contains(".")) {
            //先找到IP地址字符串中.的位置
            int position1 = strIp.indexOf(".");
            int position2 = strIp.indexOf(".", position1 + 1);
            int position3 = strIp.indexOf(".", position2 + 1);
            //将每个.之间的字符串转换成整型
            ip[0] = Long.parseLong(strIp.substring(0, position1));
            ip[1] = Long.parseLong(strIp.substring(position1+1, position2));
            ip[2] = Long.parseLong(strIp.substring(position2+1, position3));
            ip[3] = Long.parseLong(strIp.substring(position3+1));
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        }

        return 0;
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址
     * 对应MySQL的INET_NTOA函数
     * @param longIp    longIP
     * @return  字符串IP
     */
    public static String longToIP(long longIp){
        //直接右移24位
        String sb = "" + String.valueOf((longIp >>> 24)) +
                "." +
                //将高8位置0，然后右移16位
                String.valueOf((longIp & 0x00FFFFFF) >>> 16) +
                "." +
                //将高16位置0，然后右移8位
                String.valueOf((longIp & 0x0000FFFF) >>> 8) +
                "." +
                //将高24位置0
                String.valueOf((longIp & 0x000000FF));
        return sb;
    }

    private IpUtil(){}
}

