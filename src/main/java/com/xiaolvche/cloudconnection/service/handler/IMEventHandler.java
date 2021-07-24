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
import com.xiaolvche.cloudconnection.util.onlineAgent.OnlineAgentUtil;
import com.xiaolvche.cloudconnection.vo.AgBean;
import org.springframework.beans.factory.annotation.Autowired;
import sun.management.resources.agent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

//@Component
public class IMEventHandler     
{


	protected SocketIOServer server;
	Tlaking tlaking = ApplicationContextProvider.getBean(Tlaking.class);
	OnlineAgentUtil onlineAgentUtil = ApplicationContextProvider.getBean(OnlineAgentUtil.class);
	ServiceQuene serviceQuene = ApplicationContextProvider.getBean(ServiceQuene.class);
	SocketClients socketClients = ApplicationContextProvider.getBean(SocketClients.class);



	@Autowired
    public IMEventHandler(SocketIOServer server)   
    {
		//onlineAgentUtil = new OnlineAgentUtil();
    	this.server = server ;
    }


    
    @OnConnect  
    public void onConnect(SocketIOClient client)  
    {






        try {
			String ip = PasIp.getIp(client.getRemoteAddress());
			System.out.println("有新的客户连接~~~"+ip);
				/**
				 * 用户进入到对话连接 ， 排队用户请求 , 如果返回失败，表示当前坐席全忙，用户进入排队状态，当前提示信息 显示 当前排队的队列位置，不可进行对话，用户发送的消息作为留言处理
				 */

				//String zid = "1";
				socketClients.putIMEventClient(ip, client);
				//String onlineAgent = onlineAgentUtil.getOnlineAgent(ip,zid);
				//SocketIOClient agent = serviceQuene.getAgent(onlineAgent);
				SocketIOClient agent = serviceQuene.getAgent(ip);
				if(agent==null){
					System.out.println("没有客服在服务");
					//socketClients.sendIMEventMessage(ip, "status", "客服正忙，您正在排队···");
					serviceQuene.saveClient(ip, client);
				}
				else {
					Conversation conversation = new Conversation();
					conversation.setUserid(ip);
					conversation.setKefuid(PasIp.getIp(agent.getRemoteAddress()));
					conversation.setAgent(agent);
					conversation.setCreatetime(new Date());
					conversation.setClient(client);
					tlaking.addConversation(conversation);
					System.out.println("已经为您分配客服");
					client.sendEvent("agentstatus", "客服连接成功");
				}



		} catch (Exception e) {
			e.printStackTrace();
		}
    }  
      
    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息  
    @OnDisconnect  
    public void onDisconnect(SocketIOClient client)  
    {


		String ip = PasIp.getIp(client.getRemoteAddress());
		if(ip!=null){
			try {
				/**
				 * 用户主动断开服务
				 */
				String zid = "1";
				socketClients.removeIMEventClient(ip, client.getSessionId().toString());
				serviceQuene.removeClient(ip);
				SocketIOClient agent = tlaking.getAgent(ip);
				tlaking.stop(ip);
				//agent.sendEvent("new", "用户走了···");
				if (agent != null){
					serviceQuene.saveAgent(PasIp.getIp(agent.getRemoteAddress()), new AgBean(agent,7,0));
				SocketIOClient user = serviceQuene.geClient();
				if (user != null) {
					String userid = PasIp.getIp(user.getRemoteAddress());
					//String onlineAgent = onlineAgentUtil.getOnlineAgent(userid,zid);
					SocketIOClient agent1 = serviceQuene.getAgent(userid);
					String agentid = PasIp.getIp(agent1.getRemoteAddress());
					Conversation conversation = null;
					if (agent1 != null) {
						serviceQuene.removeAgent(agentid);
						serviceQuene.removeClient(userid);
						conversation = new Conversation();
						conversation.setClient(user);
						conversation.setAgent(agent1);
						conversation.setKefuid(agentid);
						conversation.setCreatetime(new Date());
						conversation.setUserid(userid);
						tlaking.addConversation(conversation);
						user.sendEvent("agentstatus", "排队等来了客服");
						agent1.sendEvent("new", "新的用户连接···");
					}
				}
			}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
    }  
      

	@OnEvent(value = "new")
	public void Event(SocketIOClient client, AckRequest request, String data){

	}

	//消息接收入口，坐席状态更新
    @OnEvent(value = "agentstatus")
	public void OnEvent(SocketIOClient client, AckRequest request, String data){

	}
	@OnEvent(value = "img")
	public void loadImg(SocketIOClient client, byte[] data, AckRequest ackRequest){
		String ip = PasIp.getIp(client.getRemoteAddress());
		SocketIOClient agent = tlaking.getAgent(ip);
		if(agent!=null){
			agent.sendEvent("img", data);
		}
		else{
			System.out.println("客户发的消息没人接收:");
			serviceQuene.saveClient(ip, client);
			//client.sendEvent("agentstatus", "客服正忙，您正在排队···");
		}
		PasImg.generateImage(data);


	}

    //消息接收入口，收发消息，用户向坐席发送消息和 坐席向用户发送消息  
    @OnEvent(value = "message")  
    public void onEvent(SocketIOClient client, AckRequest request, String data)
    {

		String ip = PasIp.getIp(client.getRemoteAddress());

		String agent = tlaking.getAgentPos(ip);
		if(agent!=null){
			System.out.println("发给"+agent+"客服");
        	socketClients.sendAgentEventMessage(agent, "message", data);
			System.out.println("客服收到客户的数据:"+data);
		}
		else{
			System.out.println("客户发的消息没人接收:"+data);
			serviceQuene.saveClient(ip, client);
			//client.sendEvent("agentstatus", "客服正忙，您正在排队···");
		}

    } 
}  