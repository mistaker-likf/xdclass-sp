package net.xdclass.xdclasssp;

import net.xdclass.xdclasssp.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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
			template.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "order.new", "新订单777");
//		}
	}

	@Test
	void testConfirmCallBack(){

		template.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

			/**
			 *
			 * @param correlationData 配置
			 * @param ack 交换机是否收到消息，true是成功 false是失败
			 * @param cause 失败的原因
			 */
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				System.out.println("confirm=====>");
				System.out.println("correlationData=====>"+correlationData);
				System.out.println("confirm==== ack="+ack);
				System.out.println("confirm==== cause="+cause);

				if(ack){
					System.out.println("发送成功，记录到日志或数据库");
					//TODO 更新数据库 消息的状态为成功
				}else {
					System.out.println("发送失败，记录到日志或数据库");
					//TODO 更新数据库 消息的状态为失败
				}
			}
		});

		//TODO 发送消息之前 在数据库新增一个消息记录 状态是发送

		//发送消息
		template.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "order.new", "新订单");
	}

	@Test
	void testReturnCallback() {
		template.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
			@Override
			public void returnedMessage(ReturnedMessage returned) {
				int code = returned.getReplyCode();
				System.out.println("code="+code);
				System.out.println("returned="+returned.toString());
			}
		});

		template.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "xdorder.new", "新订单ReturnCallback");
	}

}
