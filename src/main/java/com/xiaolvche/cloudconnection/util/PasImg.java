package com.xiaolvche.cloudconnection.util;

import sun.misc.BASE64Decoder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

/**
 * @author liaoxh
 * @create 2021-07-23-15:08
 */

public class PasImg {
    public static void generateImage(byte[] data) {

        System.out.println("图片长度:" + data.length);
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "");
        String filePath = "D:\\picture\\temppicture\\";
        System.out.println(newFileName);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        FileChannel channel = null;
        try {
            channel = new FileOutputStream(filePath+newFileName+".png").getChannel();
            while (byteBuffer.hasRemaining()) {
                channel.write(byteBuffer);
            }
            channel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}