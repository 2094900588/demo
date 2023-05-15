package com.example.demo.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.72");
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setPort(5672);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        // 接收消息的回调函数
        DeliverCallback deliverCallback = (consumerTage, message) -> {
            System.out.println("接收到消息" + new String(message.getBody()));
        };

        //取消消息的回调函数
        CancelCallback cancelCallback = consumerTage -> {
            System.out.println("消费信息被中断");
        };

        /**
         * 消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否需要自动应答，true：自动应答
         * 3.接收消息的回调函数
         * 4.取回消息的回调函数
         */
        channel.basicConsume("xc_queue_name",true,deliverCallback,cancelCallback);

    }
}
