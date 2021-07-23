package com.xiaolvche.cloudconnection.mapper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolvche.cloudconnection.bean.Conversation;
import com.xiaolvche.cloudconnection.mapper.ConversationMapper;
import com.xiaolvche.cloudconnection.mapper.service.ConversationService;
import org.springframework.stereotype.Service;

/**
 * @author liaoxh
 * @create 2021-07-21-20:32
 */
@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper,Conversation> implements ConversationService {
}
