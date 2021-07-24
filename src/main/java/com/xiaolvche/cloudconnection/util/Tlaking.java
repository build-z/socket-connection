package com.xiaolvche.cloudconnection.util;

import com.corundumstudio.socketio.SocketIOClient;
import com.xiaolvche.cloudconnection.bean.Conversation;
import com.xiaolvche.cloudconnection.config.ApplicationContextProvider;
import com.xiaolvche.cloudconnection.service.ConversationService;
import com.xiaolvche.cloudconnection.util.client.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author liaoxh
 * @create 2021-07-22-12:24
 */
@Component
public class Tlaking {
    @Autowired
    ConversationService conversationService;
    ServiceQuene serviceQuene = ApplicationContextProvider.getBean(ServiceQuene.class);
    private static List<Conversation> seat = new ArrayList();
    public void addConversation(Conversation conversation){
        if(conversation!=null){
            seat.add(conversation);
        }
    }
    //返回与客户连接的客服ID
    public String getAgentPos(String userid){
        if(userid==null)return null;
        for (Conversation c:seat) {
             if(userid.equals(c.getUserid())){
                return c.getKefuid();
            }
        }
        return null;
    }

    //返回与客服连接的客户ID
    public String getClientPos(String agentid){

        if(agentid==null)return null;
        System.out.println(seat.toString());
        for (Conversation c:seat) {
            System.out.println("找用户id");
            if(agentid.equals(c.getKefuid())){
                return c.getUserid();
            }
        }
        return null;
    }

    //会话队列中删除，并保存到数据库
    public void stop(String userid){
        if(userid==null || seat.size()<=0)return ;
        for (int i=0;i<seat.size();i++) {
            if(userid.equals(seat.get(i).getUserid())){
               Conversation c = seat.get(i);
               c.setEndtime(new Date());
                conversationService.save(c);
                seat.remove(i);
            }
        }
    }
    public void agentStop(String agentid){
        if(agentid==null) return;
        for(int i=0;i<seat.size();i++){
            if(agentid.equals(seat.get(i).getKefuid())){
                serviceQuene.saveClient(seat.get(i).getUserid(),seat.get(i).getClient());
                seat.remove(i);
            }
        }
    }
    public SocketIOClient getAgent(String userid){
        if(userid==null)return null;
        for (Conversation c:seat) {
            if(userid.equals(c.getUserid())){
                return c.getAgent();
            }
        }
        return null;
    }
    public SocketIOClient getClient(String agentid){
        if(agentid==null)return null;
        for (Conversation c:seat) {
            if(agentid.equals(PasIp.getIp(c.getAgent().getRemoteAddress()))){
                return c.getClient();
            }
        }
        return null;
    }
    public void addClient(SocketIOClient agent, SocketIOClient client){
        String agentid = PasIp.getIp(agent.getRemoteAddress());
        String clientid = PasIp.getIp(client.getRemoteAddress());
        if(agentid==null) return;
        for(int i=0;i<seat.size();i++){
            if(agentid.equals(seat.get(i).getKefuid())){
                Conversation c = seat.get(i);
                HashMap<String, SocketIOClient> map = c.getClinets();
                map.put(clientid, client);
                return ;
            }
        }
        HashMap<String, SocketIOClient> hashMap = new HashMap<>();
        hashMap.put(clientid, client);
        Conversation conversation = new Conversation();
        conversation.setUserid(clientid);
        conversation.setKefuid(agentid);
        conversation.setAgent(client);
        conversation.setClient(client);
        conversation.setCreatetime(new Date());

    }
}
