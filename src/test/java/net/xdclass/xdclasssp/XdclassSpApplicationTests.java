package net.xdclass.xdclasssp;

import net.xdclass.xdclasssp.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XdclassSpApplicationTests {

	@Autowired
	private RabbitTemplate template;

	@Test
	void testSend(){
//		for(int i=0; i<5; i++){
			template.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "order.new", "新订单");
//		}
	}

	@Test
	void contextLoads() {
	}

}
