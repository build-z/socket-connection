package com.xiaolvche.cloudconnection.util;

import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author liaoxh
 * @create 2021-07-23-15:08
 */

public class PasImg {
    public static boolean generateImage(String imgStr, String filename) {

        if (imgStr == null) {
            return false;
        }
       // BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
           // byte[] b = decoder.decodeBuffer(imgStr);
            byte [] b =imgStr.getBytes();
            // 处理数据
            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(filename);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }
}
