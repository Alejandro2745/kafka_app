package co.vinni.kafka.SBProveedor.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    private Map<String, String> commonConfigs() {
        Map<String, String> configurations = new HashMap<>();
        configurations.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE);
        configurations.put(TopicConfig.RETENTION_MS_CONFIG, "86400000"); // 1 día
        configurations.put(TopicConfig.SEGMENT_BYTES_CONFIG, "1073741824"); // 1 GB
        configurations.put(TopicConfig.MAX_MESSAGE_BYTES_CONFIG, "1048576"); // 1 MB
        return configurations;
    }

    @Bean
    public NewTopic placasSoatTopic() {
        return TopicBuilder.name("placas-soat")
                .partitions(1)
                .replicas(1)
                .configs(commonConfigs())
                .build();
    }

    @Bean
    public NewTopic placasSoatRespuestasTopic() {
        return TopicBuilder.name("placas-soat-respuestas")
                .partitions(1)
                .replicas(1)
                .configs(commonConfigs())
                .build();
    }
}
