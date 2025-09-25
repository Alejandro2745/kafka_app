package co.vinni.kafka.SBProveedor.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SoatResponseStore {

    private final Map<String, Map<String, Object>> lastByPlaca = new ConcurrentHashMap<>();

    public void put(String placa, Map<String, Object> json) {
        lastByPlaca.put(placa, json);
    }

    public Map<String, Object> get(String placa) {
        return lastByPlaca.get(placa);
    }
}
