package com.xiaolvche.cloudconnection.util.client;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.common.collect.ArrayListMultimap;

import java.util.List;

public class SocketIMClient implements SocketClient{
	
	private ArrayListMultimap<String, SocketIOClient> imClientsMap = ArrayListMultimap.create();
	
	public List<SocketIOClient> getClients(String key){
		return imClientsMap.get(key) ;
	}
	
	public void putClient(String key , SocketIOClient client){

		imClientsMap.put(key, client) ;
		System.out.println("当前客户人数"+imClientsMap.size());
	}
	
	public void removeClient(String key , String id){
		List<SocketIOClient> keyClients = this.getClients(key) ;
		for(SocketIOClient client : keyClients){
			if(client.getSessionId().toString().equals(id)){
				keyClients.remove(client) ;
				break ;
			}
		}
		if(keyClients.size() == 0){
			imClientsMap.removeAll(key) ;
		}
	}
}
