package com.aitongyi.rabbitmq.topic.quiz;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TopicSend {
	
	private static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) {
		Connection connection = null;
		Channel channel = null;
		
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			connection = factory.newConnection();
			channel = connection.createChannel();
			
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			
			String[] routingKeys = new String[] {
					"",
					"..",
					"horse",
					"rabbit.",
					".fox",
					"a.orange.fox",
					"a",
					"a.",
					"a.b.",
					"a.b.c"
			};
			
			for(String severity : routingKeys) {
                String message = "From "+severity+" routingKey' s message!";  
                channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
                System.out.println("TopicSend [x] Sent '" + severity + "':'" + message + "'");  
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

}
