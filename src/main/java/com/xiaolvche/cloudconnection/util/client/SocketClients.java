package com.xiaolvche.cloudconnection.util.client;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SocketClients {
	
	private static SocketClients clients = new SocketClients();
	@Autowired
	private SocketIMClient imClients;
	@Autowired
	private SocketAgentClient agentClients;
	//@Autowired
	//private SocketIMClient entIMClients;

	//private NettyCallCenterClient callCenterClients = new NettyCallCenterClient();
	
	public static SocketClients getInstance(){
		return clients ;
	}
	
	/*public NettyCallCenterClient getCallCenterClients(){
		return this.callCenterClients ;
	}*/

	public void setImClients(SocketIMClient imClients) {
		this.imClients = imClients;
	}
	public void putIMEventClient(String id , SocketIOClient userClient){

		imClients.putClient(id, userClient);
	}

	public void removeIMEventClient(String id , String sessionid){
		System.out.println("客户离开");
		imClients.removeClient(id, sessionid);
	}
	public void sendIMEventMessage(String id , String event , Object data){
		List<SocketIOClient> userClients = imClients.getClients(id) ;
		for(SocketIOClient userClient : userClients){
			userClient.sendEvent(event, data);
		}
	}
	
	public void setAgentClients(SocketAgentClient agentClients) {
		this.agentClients = agentClients;
	}
	public void putAgentEventClient(String id , SocketIOClient agentClient){
		agentClients.putClient(id, agentClient);
	}
	public void removeAgentEventClient(String id , String sessionid){
		System.out.println("有一个客服离开");
		agentClients.removeClient(id, sessionid);
	}
	public void sendAgentEventMessage(String id , String event , Object data){
		List<SocketIOClient> agents = agentClients.getClients(id) ;
		for(SocketIOClient agentClient : agents){
			agentClient.sendEvent(event, data);
		}
	}
	
	/*public void setEntImClients(SocketIMClient entIMClients) {
		this.entIMClients = entIMClients;
	}
	public void putEntIMEventClient(String id , SocketIOClient userClient){
		entIMClients.putClient(id, userClient);
	}
	public void removeEntIMEventClient(String id , String sessionid){
		entIMClients.removeClient(id, sessionid);
	}
	public void sendEntIMEventMessage(String id , String event , Object data){
		List<SocketIOClient> entims = entIMClients.getClients(id) ;
		for(SocketIOClient userClient : entims){
			userClient.sendEvent(event, data);
		}
	}
	public int getEntIMClientsNum(String user){
		return entIMClients.getClients(user)!=null ? entIMClients.getClients(user).size() : 0;
	}*/
	

}
