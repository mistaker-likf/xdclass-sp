package net.xdclass.xdclasssp.controller;


import net.xdclass.xdclasssp.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/merchant")
public class MerChantAccountController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("check")
    public Object check(){
        //TODO 修改数据库商家账号状态

        rabbitTemplate.convertAndSend(RabbitMQConfig.NEW_MERCHANT_EXCHANGE,
                RabbitMQConfig.NEW_MERCHANT_ROUTING_KEY, "商家账号通过审核");
        Map<String, Object> map = new HashMap<>();

        map.put("code", 0);
        map.put("msg", "账号审核通过 请在10秒内上传一个商品");
        return map;
    }

}
