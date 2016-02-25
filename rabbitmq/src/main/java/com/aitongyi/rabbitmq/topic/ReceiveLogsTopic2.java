package com.aitongyi.rabbitmq.topic;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiveLogsTopic2 {
	
	private static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) throws Exception {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		String queueName = channel.queueDeclare().getQueue();
		
		String[] routingKeys = new String[] {"*.*.rabbit", "lazy.#"};
		for(String bindingKey : routingKeys) {
			channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
            System.out.println("ReceiveLogsTopic2 exchange:"+EXCHANGE_NAME+", queue:"+queueName+", BindRoutingKey:" + bindingKey); 
		}

        System.out.println("ReceiveLogsTopic2 [*] Waiting for messages. To exit press CTRL+C");  
        
        Consumer consumer = new DefaultConsumer(channel) {
        	@Override
        	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        		String message = new String(body, "UTF-8");
                System.out.println("ReceiveLogsTopic2 [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
        	}
        };
		
        channel.basicConsume(queueName, true, consumer);
	}

}
