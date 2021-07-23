package com.xiaolvche.cloudconnection.util;


import java.net.SocketAddress;

/**
 * @author liaoxh
 * @create 2021-07-22-13:00
 */

public class PasIp {
    public static String getIp(SocketAddress addr){
        if (addr==null) return null;
        String add =addr.toString();
        return add.substring(1,add.length());
    }
}
