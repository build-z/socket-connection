package com.xiaolvche.cloudconnection.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liaoxh
 * @create 2021-07-24-19:10
 */
@Data
public class ChatMessage implements Serializable {
    private String message;
    private  byte[] data;
    private String userid;
}
