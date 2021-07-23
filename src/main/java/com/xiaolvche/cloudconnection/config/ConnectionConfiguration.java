package com.xiaolvche.cloudconnection.config;



import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.xiaolvche.cloudconnection.service.handler.IMEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;


@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = { "com.xiaolvche.cloudconnection.util"})
public class ConnectionConfiguration
{  	
	@Value("${im.server.host}")
    private String host;  
  
    @Value("${im.server.port}")
    private Integer port;
    
    @Value("${web.upload-path}")
    private String path;


    private SocketIOServer server ;
    
    @Bean(name="webimport")
    public Integer getWebIMPort() {   

    	return port;   
    }  
    
    @Bean
    public SocketIOServer socketIOServer() throws NoSuchAlgorithmException, IOException   
    {  
    	Configuration config = new Configuration();
		config.setPort(port);
        config.setMaxFramePayloadLength(1024 * 1024);
		config.setWorkerThreads(100);
		config.setAuthorizationListener(new AuthorizationListener() {
			public boolean isAuthorized(HandshakeData data) {
				return true;
			}
		});

        return server = new SocketIOServer(config);

    }
    
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {


        return new SpringAnnotationScanner(socketServer);  
    }  
    
    @PreDestroy  
    public void destory() { 
		server.stop();
	}
}  