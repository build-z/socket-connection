package com.xiaolvche.cloudconnection.service.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.xiaolvche.cloudconnection.bean.Conversation;
import com.xiaolvche.cloudconnection.config.ApplicationContextProvider;
import com.xiaolvche.cloudconnection.util.PasImg;
import com.xiaolvche.cloudconnection.util.PasIp;
import com.xiaolvche.cloudconnection.util.ServiceQuene;
import com.xiaolvche.cloudconnection.util.Tlaking;
import com.xiaolvche.cloudconnection.util.client.SocketClients;
import com.xiaolvche.cloudconnection.vo.AgBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.management.resources.agent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;


public class AgentEventHandler
{  
	protected SocketIOServer server;
    Tlaking tlaking = ApplicationContextProvider.getBean(Tlaking.class);
    ServiceQuene serviceQuene = ApplicationContextProvider.getBean(ServiceQuene.class);
    SocketClients socketClients = ApplicationContextProvider.getBean(SocketClients.class);
    @Autowired
    public AgentEventHandler(SocketIOServer server)   
    {  
    	this.server = server ;
    }

    
    @OnConnect  
    public void onConnect(SocketIOClient client)  
    {
    	System.out.println("客服已经连接~~");
        String ip = PasIp.getIp(client.getRemoteAddress());
        //socketClients.putAgentEventClient(user, client);
        socketClients.putAgentEventClient(ip, client);
        AgBean agBean = new AgBean(client,7,0);
        serviceQuene.saveAgent(ip, agBean);
        SocketIOClient userClient = serviceQuene.geClient();
        if(userClient!=null){
            Conversation conversation = new Conversation();
            conversation.setUserid(PasIp.getIp(userClient.getRemoteAddress()));
            conversation.setKefuid(PasIp.getIp(client.getRemoteAddress()));
            conversation.setAgent(client);
            conversation.setClient(userClient);
            conversation.setCreatetime(new Date());
            tlaking.addConversation(conversation);
            serviceQuene.agentService(ip);
            userClient.sendEvent("agentstatus", "客服连接成功");
        }
    }  
      
    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息  
    @OnDisconnect  
    public void onDisconnect(SocketIOClient client)  
    {
        String agentid = PasIp.getIp(client.getRemoteAddress());
		if(!StringUtils.isBlank(agentid)){
		    socketClients.removeAgentEventClient(PasIp.getIp(client.getRemoteAddress()), client.getSessionId().toString());
		    serviceQuene.removeAgent(PasIp.getIp(client.getRemoteAddress()));
		    tlaking.agentStop(PasIp.getIp(client.getRemoteAddress()));
            //socketClients.removeAgentEventClient(agentid , client.getSessionId().toString());
		}
    }  
      
    //消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
    @OnEvent(value = "service")
    public void  OnEvent(SocketIOClient client, AckRequest request, String data){
        System.out.println("客服应该重新加入队列");
        String ip = PasIp.getIp(client.getRemoteAddress());
        serviceQuene.saveAgent(ip, new AgBean(client,7,0));
    }


    @OnEvent(value = "clientout")
    public void reEvent(SocketIOClient client, AckRequest request, String data){

    }

    @OnEvent(value = "new")
    public void Event(SocketIOClient client, AckRequest request, String data){
        System.out.println("客服应该重新加入队列");
    }

    @OnEvent(value = "img")
    public void loadImg(SocketIOClient client, byte[] data, AckRequest ackRequest){
        String ip = PasIp.getIp(client.getRemoteAddress());
        SocketIOClient client1 = tlaking.getClient(ip);
        if(client1!=null){
            client1.sendEvent("img", data);
        }
        else{
            System.out.println("客服发的消息没人接收:");
            //serviceQuene.saveClient(ip, client);
            //client.sendEvent("agentstatus", "客服正忙，您正在排队···");
        }
        PasImg.generateImage(data);




    }

  //消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
    @OnEvent(value = "message")  
    public void onEvent(SocketIOClient client, AckRequest request, String data)
    {

        String ip = PasIp.getIp(client.getRemoteAddress());
        System.out.println("客服寻找:"+PasIp.getIp(client.getRemoteAddress()));
        String clientPos = tlaking.getClientPos(ip);
    	System.out.println("客户收到的消息："+data);
    	if(clientPos!=null){
            System.out.println("发给"+clientPos+"用户");
             socketClients.sendIMEventMessage(clientPos, "message", data);
    	}
    	else {
    	    client.sendEvent(ip,"clientstatus", "用户已断开连接");
    	    System.out.println("客服发送的消息石沉大海···");
        }

    }
}  