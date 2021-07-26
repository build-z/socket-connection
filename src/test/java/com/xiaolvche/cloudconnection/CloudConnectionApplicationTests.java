package com.xiaolvche.cloudconnection;

import com.xiaolvche.cloudconnection.service.ConversationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CloudConnectionApplicationTests {
	@Autowired
	ConversationService conversationService;
	@Test
	void contextLoads() {
	}
	@Test

	public void test(){
		List<String> list = conversationService.getKefuId("10.33.60.8:4096");
		list.forEach(System.out::println);

	}

}
