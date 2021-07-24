package com.xiaolvche.cloudconnection.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * @author liaoxh
 * @create 2021-07-21-20:33
 */
@TableName("conversation")
@Data
public class Conversation implements Serializable{

    private String id;
    @TableField("userid")
    private String userid;
    @TableField("kefuid")
    private String kefuid;
    @TableField("createtime")
    private Date createtime;
    @TableField("endtime")
    private Date endtime;
    @TableField("ishandle")
    private int ishandle;
    @TableField("comment")
    private String comment;
    @TableField("what")
    private String what;
    @TableField(exist = false)
    private SocketIOClient agent;
    @TableField(exist = false)
    private SocketIOClient client;
    @TableField(exist = false)
    private HashMap<String,SocketIOClient> clinets;
}
