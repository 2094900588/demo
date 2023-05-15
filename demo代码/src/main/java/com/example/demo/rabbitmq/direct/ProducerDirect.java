package com.example.demo.rabbitmq.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerDirect {
    public static void main(String[] args) throws IOException, TimeoutException {


        String exechangeName = "xc_exchange_name";

        String queueName_1 = "xc_queue_name_1";
        String queueName_2 = "xc_queue_name_2";
        String queueName_3 = "xc_queue_name_3";
        String queueName_4 = "xc_queue_name_4";

        String key_1 = "key_1";
//        String key_2 = "key_2";
        String key_3 = "key_3";
        String key_4 = "key_4";

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

        channel.queueDeclare(queueName_1,true,false,false,null);
        channel.queueDeclare(queueName_2,true,false,false,null);
        channel.queueDeclare(queueName_3,true,false,false,null);
        channel.queueDeclare(queueName_4,true,false,false,null);


        /**
         * 将我们的交换机和队列绑定
         * 1.队列名称
         * 2.交换机名称
         * 3.路由器，在我们直连模式下，可以为我们的队列名称
         */

        channel.queueBind(queueName_1,exechangeName,key_1);
        channel.queueBind(queueName_2,exechangeName,key_1);
//        channel.queueBind(queueName_2,exechangeName,key_2);
        channel.queueBind(queueName_3,exechangeName,key_3);
        channel.queueBind(queueName_4,exechangeName,key_4);


        // 发送消息
        String message = "hello rabbitmq1";

        /**
         * 发送消息
         * 1.发送到哪个交换机
         * 2.队列名称
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish(exechangeName,key_1,null,"key1 message".getBytes());
//        channel.basicPublish(exechangeName,key_2,null,"key2 message".getBytes());
        channel.basicPublish(exechangeName,key_3,null,"key3 message".getBytes());
        channel.basicPublish(exechangeName,key_4,null,"key4 message".getBytes());

        channel.close();
        connection.close();

    }
}
