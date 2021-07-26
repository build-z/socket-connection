package com.xiaolvche.cloudconnection.util.client;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.common.collect.ArrayListMultimap;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class SocketAgentClient implements SocketClient{
	
	private ArrayListMultimap<String, SocketIOClient> agentClientsMap = ArrayListMultimap.create();
	
	public List<SocketIOClient> getClients(String key){
		System.out.println(agentClientsMap.toString());
		return agentClientsMap.get(key) ;
	}
	
	public void putClient(String key , SocketIOClient client){
		agentClientsMap.put(key, client) ;
	}
	
	public void removeClient(String key , String id){
		List<SocketIOClient> keyClients = this.getClients(key) ;
		for(SocketIOClient client : keyClients){
			if(client.getSessionId().equals(id)){
				keyClients.remove(client) ;
				break ;
			}
		}
		if(keyClients.size() == 0){
			agentClientsMap.removeAll(key) ;
		}
	}
}
