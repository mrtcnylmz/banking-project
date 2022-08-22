package com.mrtcnylmz.bankingsystem.Configurations;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	
	private static final Logger logger = LogManager.getLogger(Consumer.class);
	
    //Kafka listens for logs.
	@KafkaListener(topics = {"logs"}, groupId = "logs_group")
    public void listenTransfer(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition
    ){
		
		//Kafka listens and sents logs to Logj4.
		Level logLevel = Level.forName("LOGS", 400);
		logger.log(logLevel, message);
		
    }
}
