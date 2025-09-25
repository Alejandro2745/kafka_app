package co.vinni.kafka.SBConsumidor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@SpringBootApplication
public class SbConsumidorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbConsumidorApplication.class, args);
	}

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "soat-placa-requests", groupId = "soat-service")
    public void onSoatRequest(String message) {
        // Parse super simple (o usa Jackson)
        // message = {"placa":"ABC123"}
        String placa = extraerPlaca(message);

        // Lógica mock: simular consulta (en real, BD o API)
        boolean vigente = Math.random() > 0.5;
        String respuesta = "{"
                + "\"placa\":\"" + placa + "\","
                + "\"vigente\":" + vigente + ","
                + "\"aseguradora\":\"AseguradoraX\","
                + "\"vence\":\"2026-03-10\""
                + "}";

        kafkaTemplate.send("soat-placa-responses", placa, respuesta);
    }

    private String extraerPlaca(String json) {
        // Demo fea pero funcional: {"placa":"ABC123"}
        int i = json.indexOf(":");
        int j = json.lastIndexOf("\"");
        return json.substring(i+2, j);
    }

    @Component
    public class KafkaSoatResponseListener {

        private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSoatResponseListener.class);

        @KafkaListener(topics = "soat-placa-responses", groupId = "ui-group")
        public void onSoatResponse(String message) {
            LOGGER.info("Respuesta SOAT: {}", message);
        }
    }


}
