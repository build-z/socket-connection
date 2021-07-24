package com.xiaolvche.cloudconnection.vo;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liaoxh
 * @create 2021-07-24-10:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgBean {
    SocketIOClient agent;
    Integer accpetnum;
    Integer servingnum;
}

