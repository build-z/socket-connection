package com.xiaolvche.cloudconnection.util;


import com.corundumstudio.socketio.SocketIOClient;
import com.google.common.collect.ArrayListMultimap;
import com.xiaolvche.cloudconnection.bean.Conversation;
import com.xiaolvche.cloudconnection.config.ApplicationContextProvider;
import com.xiaolvche.cloudconnection.util.onlineAgent.OnlineAgentUtil;
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

    private  static ConcurrentHashMap<String,SocketIOClient> agentQuene = new ConcurrentHashMap();
    private  static ConcurrentHashMap<String,SocketIOClient> clientQuene = new ConcurrentHashMap();
   /* public static ServiceQuene getInstance(){
        return serviceQuene ;
    }*/
    public SocketIOClient getAgent(String kefuid){
        SocketIOClient service = null;
        if (agentQuene.size()>0){
            if(kefuid!=null)
            service = agentQuene.get(kefuid);
            if(service==null) {
                Set<String> set = agentQuene.keySet();
                List<String> list = new ArrayList(set);
                String key = list.get(0);
                System.out.println(key.toString());
                service = agentQuene.get(key);
                agentQuene.remove(key);
            }
            else agentQuene.remove(kefuid);

            System.out.println(PasIp.getIp(service.getRemoteAddress())+":开始服务,客服人数:"+agentQuene.size());
            }
            return service;
        }

    public void saveAgent(String kefuid,SocketIOClient service){
        System.out.println("kefuid:"+kefuid);
        System.out.println(service.toString());
        agentQuene.put(kefuid, service);
        System.out.println("客服加入服务队列:"+kefuid+"客服人数:"+ agentQuene.size());
    }
    public void removeAgent(String agentid){
        agentQuene.remove(agentid);
        System.out.println("客服离开服务队列,"+"客服人数:"+ agentQuene.size());
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

}
