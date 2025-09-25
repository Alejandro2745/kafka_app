package co.vinni.kafka.SBConsumidor.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerListener.class);

    @KafkaListener(topics = "placas-soat", groupId = "${spring.kafka.consumer.group-id}")
    public void listener(String message){
        // Aquí podrías “consultar” un servicio SOAT; por ahora se simula el procesamiento:
        LOGGER.info("Mensaje recibido (placa): {}", message);
    }
}
