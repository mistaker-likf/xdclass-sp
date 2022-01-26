package net.xdclass.xdclasssp.mq;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
@RabbitListener(queues = "lock_merchant_dead_queue")
public class OrderMQListener {

    @RabbitHandler
    public void messageHandler(String body, Message message, Channel channel) throws IOException {

        long msgTag = message.getMessageProperties().getDeliveryTag();

        System.out.println("msgTag=" + msgTag);
        System.out.println("mssage=" + message.toString());
        System.out.println("body=" + body);

        //TODO 可以在这里做业务逻辑 消费者处理消息

        //消息手动确认 消息被确认
        channel.basicAck(msgTag, false);

        //消息broker 消息被拒绝确认
//        channel.basicNack(msgTag, false, true);
//        channel.basicReject(msgTag, true); //一次只能一个
    }
}
