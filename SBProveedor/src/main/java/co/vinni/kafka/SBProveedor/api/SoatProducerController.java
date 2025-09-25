package co.vinni.kafka.SBProveedor.api;

import co.vinni.kafka.SBProveedor.store.SoatResponseStore;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/soat")
public class SoatProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SoatResponseStore store;

    public SoatProducerController(KafkaTemplate<String, String> kafkaTemplate, SoatResponseStore store) {
        this.kafkaTemplate = kafkaTemplate;
        this.store = store;
    }

    // Enviar placa al tópico de solicitudes
    // POST http://localhost:8068/api/soat/enviar?placa=ABC123
    @PostMapping("/enviar")
    public String publicarPlaca(@RequestParam String placa) {
        kafkaTemplate.send("placas-soat", placa);
        return "Enviado a Kafka: " + placa;
    }

    // Consultar el último resultado recibido para una placa
    // GET http://localhost:8068/api/soat/resultado?placa=ABC123
    @GetMapping("/resultado")
    public Object consultarResultado(@RequestParam String placa) {
        var res = store.get(placa);
        if (res == null) {
            return new ApiMessage("Sin resultado todavía para placa " + placa + ". Intenta de nuevo en unos segundos.");
        }
        return res; // es un Map<String, Object> con placa, soat, ts, fuente
    }

    record ApiMessage(String message) {}
}
