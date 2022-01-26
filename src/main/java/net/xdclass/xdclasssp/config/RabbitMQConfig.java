package net.xdclass.xdclasssp.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 新商家审核通过 -> new_merchant_queue -> 死信交换机 -> 死信队列
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 死信队列
     */
    public static final String LOCK_MERCHANT_DEAD_QUEUE = "lock_merchant_dead_queue";

    /**
     * 死信交换机
     */
    public static final String LOCK_MERCHANT_DEAD_EXCHANGE = "lock_merchant_dead_exchange";

    /**
     * 死信队列路由key
     */
    public static final String LOCK_MERCHANT_ROUTING_KEY = "lock_merchant_routing_key";

    /**
     * 创建死信交换机
     */
    @Bean
    public Exchange lockMerchantDeadExchange(){

        return new TopicExchange(LOCK_MERCHANT_DEAD_EXCHANGE, true, false);
    }

    /**
     * 创建死信队列
     * @return
     */
    @Bean
    public Queue lockMerchantDeadQueue(){
        return QueueBuilder.durable(LOCK_MERCHANT_DEAD_QUEUE).build();
    }

    /**
     * 绑定死信交换机和死信队列
     *
     */
    @Bean
    public Binding lockMerchantBinding(){
        return new Binding(LOCK_MERCHANT_DEAD_QUEUE, Binding.DestinationType.QUEUE,
                LOCK_MERCHANT_DEAD_EXCHANGE, LOCK_MERCHANT_ROUTING_KEY, null);
    }

    /**
     * 普通队列，绑定死信交换机
     */
    public static final String NEW_MERCHANT_QUEUE = "new_merchant_queue";

    /**
     * 普通topic交换机
     */
    public static final String NEW_MERCHANT_EXCHANGE= "new_merchant_exchange";

    /**
     * 普通routing key
     */
    public static final String NEW_MERCHANT_ROUTING_KEY= "new_merchant_routing_key";


    /**
     * 创建普通交换机
     */
    @Bean
    public Exchange newMerchantExchange(){

        return new TopicExchange(NEW_MERCHANT_EXCHANGE, true, false);
    }


    /**
     * 创建普通队列
     */
    @Bean
    public Queue newMerchantQueue(){

        Map<String, Object> args = new HashMap<>(3);
        //消息过期后，进入到死信交换机
        args.put("x-dead-letter-exchange",LOCK_MERCHANT_DEAD_EXCHANGE);

        //消息过期后，进入到死信交换机的路由key
        args.put("x-dead-letter-routing-key",LOCK_MERCHANT_ROUTING_KEY);

        //过期时间，单位毫秒
        args.put("x-message-ttl",10000);

        return QueueBuilder.durable(NEW_MERCHANT_QUEUE).withArguments(args).build();

    }

    /**
     * 绑定交换机和队列
     * @return
     */
    @Bean
    public Binding newMerchantBinding(){
        return new Binding(NEW_MERCHANT_QUEUE, Binding.DestinationType.QUEUE,
                NEW_MERCHANT_EXCHANGE, NEW_MERCHANT_ROUTING_KEY, null);
    }

}
