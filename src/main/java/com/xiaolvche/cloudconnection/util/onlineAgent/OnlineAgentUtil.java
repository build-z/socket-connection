package com.xiaolvche.cloudconnection.util.onlineAgent;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaolvche.cloudconnection.bean.Conversation;
import com.xiaolvche.cloudconnection.mapper.service.ConversationService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author liaoxh
 * @create 2021-07-21-19:43
 */
@Component
@NoArgsConstructor
@Data
public class OnlineAgentUtil {
     @Autowired
     ConversationService conversationService;
    //public static OnlineAgentUtil onlineAgentUtil = new OnlineAgentUtil();
    /*public static OnlineAgentUtil getInstance(){
        return onlineAgentUtil ;
    }*/
    public   String  getOnlineAgent(String userid){
        QueryWrapper<Conversation> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", userid);
        wrapper.orderByDesc("endtime");
        if(conversationService!=null){
        List<Conversation> list = conversationService.list(wrapper);
        if(list.size()>0)
            return list.get(0).getKefuid();
        else return null;
        }
        else
        System.out.println("不能使用数据库连接");
        return null;
    }
}
