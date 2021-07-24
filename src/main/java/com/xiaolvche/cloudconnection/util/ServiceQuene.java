package com.xiaolvche.cloudconnection.util;


import com.corundumstudio.socketio.SocketIOClient;
import com.google.common.collect.ArrayListMultimap;
import com.xiaolvche.cloudconnection.bean.Conversation;
import com.xiaolvche.cloudconnection.config.ApplicationContextProvider;
import com.xiaolvche.cloudconnection.service.ConversationService;
import com.xiaolvche.cloudconnection.util.onlineAgent.OnlineAgentUtil;
import com.xiaolvche.cloudconnection.vo.AgBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liaoxh
 * @create 2021-07-21-21:05
 */
@Component
public class ServiceQuene {
   // public static ServiceQuene serviceQuene = new ServiceQuene();

    private  static ConcurrentHashMap<String,AgBean> agentQuene = new ConcurrentHashMap();
    private  static ConcurrentHashMap<String,SocketIOClient> clientQuene = new ConcurrentHashMap();
    @Autowired
    ConversationService conversationService;
   /* public static ServiceQuene getInstance(){
        return serviceQuene ;
    }*/
    public SocketIOClient getAgent(String userid){
        SocketIOClient service = null;
        List<String> agentList = conversationService.getKefuId(userid);
        for(int i=0;i<agentList.size();i++){
            service  = agentQuene.get(agentList.get(i)).getAgent();
            if(service!=null){
                System.out.println(PasIp.getIp(service.getRemoteAddress())+":开始服务,客服人数:"+agentQuene.size());
                //agentQuene.remove(agentList.get(i));
                agentService(agentList.get(i));
                return service;
            }
        }

        if (agentQuene.size()>0){
            if(service==null) {
                Set<String> set = agentQuene.keySet();
                List<String> list = new ArrayList(set);
                String key = list.get(0);
                System.out.println(key.toString());
                service = agentQuene.get(key).getAgent();
                agentService(key);
                //agentQuene.remove(key);
            }
            System.out.println(PasIp.getIp(service.getRemoteAddress())+":开始服务,客服人数:"+agentQuene.size());
            }
            return service;
        }

    public void saveAgent(String kefuid,AgBean service){
        System.out.println("kefuid:"+kefuid);
        System.out.println(service.toString());
        agentQuene.put(kefuid, service);
        System.out.println("客服加入服务队列:"+kefuid+"客服人数:"+ agentQuene.size());
    }
    public void removeAgent(String agentid){
        agentQuene.remove(agentid);
        System.out.println("客服离开服务队列,"+"客服人数:"+ agentQuene.size());
    }

    //客服服务人数+1
    public  void  agentService(String agentid){
        AgBean agBean = agentQuene.get(agentid);
        if(agBean==null) return;
        Integer servingnum = agBean.getServingnum();
        if(++servingnum>=agBean.getAccpetnum()){
            removeAgent(agentid);
        }
        else{
            agBean.setServingnum(servingnum);
            agentQuene.put(agentid, agBean);
            System.out.println(agentid+" 服务人数:"+servingnum);
        }
    }



    public SocketIOClient geClient(){
        SocketIOClient service = null;
        if (clientQuene.size()>0){
                Set<String> set = clientQuene.keySet();
                List<String> list = new ArrayList(set);
                String key = list.get(0);
                System.out.println(key.toString());
                service = clientQuene.get(key);
                clientQuene.remove(key);

        }
        return service;
    }

    public void saveClient(String kefuid,SocketIOClient client){
        System.out.println("客户进入服务队列");
        client.sendEvent("agentstatus", "客服正忙，您正在排队···");
        clientQuene.put(kefuid, client);
    }
    public void removeClient(String userid){
        System.out.println("客户离开就绪队列");
        clientQuene.remove(userid);
    }
    public  void finishAgent(String agentid){
        AgBean agBean = agentQuene.get(agentid);
        if(agBean==null) return;
        Integer servingnum = agBean.getServingnum();
        servingnum--;
        agBean.setServingnum(servingnum);
        agentQuene.put(agentid, agBean);
        System.out.println(agentid+" 服务人数:"+servingnum);
    }



}
