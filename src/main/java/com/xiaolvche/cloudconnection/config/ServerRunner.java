package com.xiaolvche.cloudconnection.config;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.xiaolvche.cloudconnection.service.handler.AgentEventHandler;
import com.xiaolvche.cloudconnection.service.handler.IMEventHandler;
import com.xiaolvche.cloudconnection.util.DataContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Component
public class ServerRunner implements CommandLineRunner {
    private final SocketIOServer server;
    private final SocketIONamespace imSocketNameSpace ;
    private final SocketIONamespace agentSocketIONameSpace ;
    private final SocketIONamespace entIMSocketIONameSpace ;
    private final SocketIONamespace aiIMSocketIONameSpace ;
    private final SocketIONamespace callCenterSocketIONameSpace ;
    //@Resource
   // IMEventHandler imEventHandler;
    
    @Autowired
    public ServerRunner(SocketIOServer server) {  
        this.server = server;  
        imSocketNameSpace = server.addNamespace(DataContext.NameSpaceEnum.IM.getNamespace())  ;
        agentSocketIONameSpace = server.addNamespace(DataContext.NameSpaceEnum.AGENT.getNamespace()) ;
        entIMSocketIONameSpace = server.addNamespace(DataContext.NameSpaceEnum.ENTIM.getNamespace()) ;
        aiIMSocketIONameSpace = server.addNamespace(DataContext.NameSpaceEnum.AIIM.getNamespace()) ;
        if(DataContext.model.get("callcenter") !=null && DataContext.model.get("callcenter") == true){
        	callCenterSocketIONameSpace  = server.addNamespace(DataContext.NameSpaceEnum.CALLCENTER.getNamespace()) ;
        }else{
        	callCenterSocketIONameSpace = null ;
        }
    }
    
    @Bean(name="imNamespace")
    public SocketIONamespace getIMSocketIONameSpace(SocketIOServer server ){
    	imSocketNameSpace.addListeners(new IMEventHandler(server));
        //imSocketNameSpace.addListeners(imEventHandler);
    	return imSocketNameSpace  ;
    }
    
    @Bean(name="agentNamespace")
    public SocketIONamespace getAgentSocketIONameSpace(SocketIOServer server){
    	agentSocketIONameSpace.addListeners(new AgentEventHandler(server));
    	return agentSocketIONameSpace;
    }

    /*@Bean(name="entimNamespace")
    public SocketIONamespace getEntIMSocketIONameSpace(SocketIOServer server){
    	entIMSocketIONameSpace.addListeners(new EntIMEventHandler(server));
    	return entIMSocketIONameSpace;
    }
    
    @Bean(name="aiimNamespace")
    public SocketIONamespace getAiIMSocketIONameSpace(SocketIOServer server){
    	aiIMSocketIONameSpace.addListeners(new AiIMEventHandler(server));
    	return aiIMSocketIONameSpace;
    }*/
    
    @Bean(name="callCenterNamespace")
    public SocketIONamespace getCallCenterIMSocketIONameSpace(SocketIOServer server){
    	if(DataContext.model.get("callcenter") !=null && DataContext.model.get("callcenter") == true){
    		Constructor<?> constructor;
			try {
				constructor = Class.forName("com.ukefu.webim.util.server.handler.CallCenterEventHandler").getConstructor(new Class[]{SocketIOServer.class});
				callCenterSocketIONameSpace.addListeners(constructor.newInstance(server));
			} catch (NoSuchMethodException | SecurityException
					| ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
    	}
    	return callCenterSocketIONameSpace;
    }

    public void run(String... args) throws Exception { 
        server.start();  
        DataContext.setIMServerStatus(true);	//IMServer 启动成功
    }  
}  