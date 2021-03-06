package com.aitongyi.rabbitmq.routing;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiveLogsDirect2 {
	
	private static final String EXCHANGE_NAME = "direct_logs";
	
	private static final String[] routingKeys = new String[] {"error"};

	public static void main(String[] args) throws Exception {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		String queueName = channel.queueDeclare().getQueue();
		
		for(String severity : routingKeys) {
			channel.queueBind(queueName, EXCHANGE_NAME, severity);
            System.out.println("ReceiveLogsDirect1 exchange:"+EXCHANGE_NAME+", queue:"+queueName+", BindRoutingKey:" + severity);
		}
		
        System.out.println("ReceiveLogsDirect2 [*] Waiting for messages. To exit press CTRL+C");  
        
        Consumer consumer = new DefaultConsumer(channel) {
        	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        		String message = new String(body, "UTF-8");
        		System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
        	}
        };
		
        channel.basicConsume(queueName, true, consumer);
	}

}
