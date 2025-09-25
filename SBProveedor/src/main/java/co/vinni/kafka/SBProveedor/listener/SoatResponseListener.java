package co.vinni.kafka.SBProveedor.listener;

import co.vinni.kafka.SBProveedor.store.SoatResponseStore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SoatResponseListener {

    private static final Logger log = LoggerFactory.getLogger(SoatResponseListener.class);
    private final SoatResponseStore store;
    private final ObjectMapper mapper = new ObjectMapper();

    public SoatResponseListener(SoatResponseStore store) {
        this.store = store;
    }

    @KafkaListener(topics = "placas-soat-respuestas", groupId = "${spring.kafka.consumer.group-id}")
    public void onResponse(String message) {
        try {
            Map<String, Object> json = mapper.readValue(message, new TypeReference<Map<String, Object>>() {});
            String placa = String.valueOf(json.get("placa"));
            store.put(placa, json);
            log.info("Respuesta SOAT almacenada para placa {} -> {}", placa, json);
        } catch (Exception e) {
            log.error("No se pudo parsear respuesta [{}]: {}", message, e.getMessage(), e);
        }
    }
}
