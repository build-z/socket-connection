package com.xiaolvche.cloudconnection.service.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.xiaolvche.cloudconnection.bean.Conversation;
import com.xiaolvche.cloudconnection.config.ApplicationContextProvider;
import com.xiaolvche.cloudconnection.util.PasIp;
import com.xiaolvche.cloudconnection.util.ServiceQuene;
import com.xiaolvche.cloudconnection.util.Tlaking;
import com.xiaolvche.cloudconnection.util.client.SocketClients;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.management.resources.agent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.UUID;

@Log4j
public class AgentEventHandler
{  
	protected SocketIOServer server;
    Tlaking tlaking = ApplicationContextProvider.getBean(Tlaking.class);
    ServiceQuene serviceQuene = ApplicationContextProvider.getBean(ServiceQuene.class);
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
        //SocketClients.getInstance().putAgentEventClient(user, client);
        SocketClients.getInstance().putAgentEventClient(ip, client);
        serviceQuene.saveAgent(ip, client);
        SocketIOClient userClient = serviceQuene.geClient();
        if(userClient!=null){
            Conversation conversation = new Conversation();
            conversation.setUserid(PasIp.getIp(userClient.getRemoteAddress()));
            conversation.setKefuid(PasIp.getIp(client.getRemoteAddress()));
            conversation.setAgent(client);
            conversation.setClient(userClient);
            conversation.setCreatetime(new Date());
            tlaking.addConversation(conversation);
            serviceQuene.removeAgent(ip);
            userClient.sendEvent("agentstatus", "客服连接成功");
        }
    }  
      
    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息  
    @OnDisconnect  
    public void onDisconnect(SocketIOClient client)  
    {
        String agentid = PasIp.getIp(client.getRemoteAddress());
		if(!StringUtils.isBlank(agentid)){
		    SocketClients.getInstance().removeAgentEventClient(PasIp.getIp(client.getRemoteAddress()), client.getSessionId().toString());
		    serviceQuene.removeAgent(PasIp.getIp(client.getRemoteAddress()));
		    tlaking.agentStop(PasIp.getIp(client.getRemoteAddress()));
            //SocketClients.getInstance().removeAgentEventClient(agentid , client.getSessionId().toString());
		}
    }  
      
    //消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
    @OnEvent(value = "service")
    public void  OnEvent(SocketIOClient client, AckRequest request, String data){
        System.out.println("客服应该重新加入队列");
        String ip = PasIp.getIp(client.getRemoteAddress());
        serviceQuene.saveAgent(ip, client);
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
            System.out.println("客服发的消息没人接收:"+data);
            //serviceQuene.saveClient(ip, client);
            //client.sendEvent("agentstatus", "客服正忙，您正在排队···");
        }
        System.out.println("图片长度:"+data.length);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        FileChannel channel = null;
        try {
            String filename = UUID.randomUUID().toString().replaceAll("-", "");
            channel = new FileOutputStream("D:\\picture\\temppicture\\"+filename+".png").getChannel();
            while (byteBuffer.hasRemaining()){
                channel.write(byteBuffer);
            }
            channel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



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
             SocketClients.getInstance().sendIMEventMessage(clientPos, "message", data);
    	}
    	else {
    	    client.sendEvent(ip,"clientstatus", "用户已断开连接");
    	    System.out.println("客服发送的消息石沉大海···");
        }

    }
}  