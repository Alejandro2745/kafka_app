package co.vinni.kafka.SBConsumidor.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Component
public class KafkaConsumerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerListener.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public KafkaConsumerListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "placas-soat", groupId = "${spring.kafka.consumer.group-id}")
    public void listener(String placa){
        LOGGER.info("Mensaje recibido (placa): {}", placa);

        // --- Simulación simple de verificación SOAT ---
        // Regla de demo: si la suma de códigos de la placa es par => "VIGENTE", si no => "VENCIDO"
        String estado = esPar(placa) ? "VIGENTE" : "VENCIDO";

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("placa", placa);
        respuesta.put("soat", estado);
        respuesta.put("ts", Instant.now().toString());
        respuesta.put("fuente", "simulado");

        try {
            String json = mapper.writeValueAsString(respuesta);

            // Bloqueante para la demo: espera a que el broker confirme el envío
            var sendResult = kafkaTemplate.send("placas-soat-respuestas", placa, json).get();
            var md = sendResult.getRecordMetadata();

            LOGGER.info("Respuesta publicada -> topic={}, partition={}, offset={}", md.topic(), md.partition(), md.offset());
        } catch (Exception e) {
            LOGGER.error("Error publicando respuesta para placa {}: {}", placa, e.getMessage(), e);
        }
    }

    private boolean esPar(String placa) {
        int sum = 0;
        for (char c : placa.toCharArray()) sum += c;
        return (sum % 2) == 0;
        // Puedes reemplazarlo por cualquier lógica: llamada HTTP real, DB, etc.
    }
}
