package co.vinni.kafka.SBProveedor.api;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/soat")
public class SoatProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SoatProducerController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // POST /api/soat/enviar?placa=ABC123
    @PostMapping("/enviar")
    public String publicarPlaca(@RequestParam String placa) {
        kafkaTemplate.send("placas-soat", placa);
        return "Enviado a Kafka: " + placa;
    }
}
