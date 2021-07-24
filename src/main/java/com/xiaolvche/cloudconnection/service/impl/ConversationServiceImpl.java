package com.xiaolvche.cloudconnection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolvche.cloudconnection.bean.Conversation;
import com.xiaolvche.cloudconnection.mapper.ConversationMapper;
import com.xiaolvche.cloudconnection.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liaoxh
 * @create 2021-07-21-20:32
 */
@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper,Conversation> implements ConversationService {
    @Autowired
    ConversationMapper conversationMapper;
    @Override
    public List<String> getKefuId(String userid) {
        return conversationMapper.getKefuId( userid);
    }
}
