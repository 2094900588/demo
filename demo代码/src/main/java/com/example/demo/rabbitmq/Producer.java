package com.example.demo.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {

        String queueName = "xc_queue_name";
        String exechangeName = "xc_exchange_name";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.72");
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setPort(5672);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        /**
         * 创建交换机
         * 1.交换机名称
         * 2.交换机类型，direct，topic，fanout和headers
         * 3.指定交换机是否需要持久化，如果设置为true，那么交换机的元数据需要持久化
         * 4.指定交换机在没有队列绑定时，是否要删除，设置为false表示不删除
         * 5.Map<String, Object>类型，用来指定我们交换机其他的一些机构化参数
         */
        channel.exchangeDeclare(exechangeName,BuiltinExchangeType.DIRECT,true,false,null);

        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列是否需要持久化，但是注意，这里的持久化只是队列名称等元数据的持久化，不是队列消息中的持久化
         * 3.表示队列是不是私有的，如或是私有的，只有创建他的应用程序才能消费消息
         * 4.队列在没有消费者订阅的情况下是否自动删除
         * 5.队列的一些结构化信息，比如生命死信队列，磁盘队列会用例
         */

        channel.queueDeclare(queueName,true,false,false,null);

        /**
         * 将我们的交换机和队列绑定
         * 1.队列名称
         * 2.交换机名称
         * 3.路由器，在我们直连模式下，可以为我们的队列名称
         */

        channel.queueBind(queueName,exechangeName,queueName);

        // 发送消息
        String message = "hello rabbitmq1";

        /**
         * 发送消息
         * 1.发送到哪个交换机
         * 2.队列名称
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish(exechangeName,queueName,null,message.getBytes());

        channel.close();
        connection.close();

    }
}
