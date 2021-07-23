package com.xiaolvche.cloudconnection.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.Data;

import java.util.Date;

/**
 * @author liaoxh
 * @create 2021-07-21-20:33
 */
@TableName("conversation")
@Data
public class Conversation {
    private String id;
    private String userid;
    private String kefuid;
    private Date createtime;
    private Date endtime;
    private int ishandle;
    private String comment;
    private String what;
    @TableField(exist = false)
    private SocketIOClient agent;
    @TableField(exist = false)
    private SocketIOClient client;
}
