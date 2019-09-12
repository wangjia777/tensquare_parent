package com.tensquare.rabbit.com.tensquare.rabbit.customer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues ="itheima")//监听器，变为消费者
public class Customer2 {

    @RabbitHandler
    public  void  getMsg(String msg){

        System.out.println("itheima："+msg);
    }
}
