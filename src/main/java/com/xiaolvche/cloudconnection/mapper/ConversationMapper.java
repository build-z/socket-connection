package com.xiaolvche.cloudconnection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolvche.cloudconnection.bean.Conversation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liaoxh
 * @create 2021-07-21-20:28
 */
@Mapper
public interface ConversationMapper extends BaseMapper<Conversation> {
     List<String> getKefuId(String userid);
}
