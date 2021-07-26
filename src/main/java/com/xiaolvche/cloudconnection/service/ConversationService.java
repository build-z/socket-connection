package com.xiaolvche.cloudconnection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolvche.cloudconnection.bean.Conversation;

import java.util.List;

/**
 * @author liaoxh
 * @create 2021-07-21-20:32
 */

public interface ConversationService extends IService<Conversation> {
     List<String> getKefuId(String userid);
}
