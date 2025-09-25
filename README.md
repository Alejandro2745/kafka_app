## Requisitos
- Java 17+
- Kafka instalado localmente (ej.: C:\kafka con Kafka 4.x KRaft 1 nodo)
- (Opcional) Postman o curl para probar los endpoints

## Si es primera vez que se inicia:
mkdir C:\kafka\data\kafka-logs
mkdir C:\kafka\data\controller

Genera un UUID y cópialo (salida en una sola línea)
bin\windows\kafka-storage.bat random-uuid

Formatea el storage con ese UUID y el config KRaft
bin\windows\kafka-storage.bat format -t <PEGA_AQUI_EL_UUID> -c config\server.properties

bin\windows\kafka-server-start.bat config\server.properties


## 1) Levantar Kafka
- Windows: ejecutar `scripts\kafka-start.bat` (editar la ruta si no es C:\kafka)
- Verifica: `bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092`

## 2) Levantar las apps
- Primero consumidor:  
  - Maven: `./mvnw -pl SBConsumidor -am spring-boot:run`  
  - Gradle: `./gradlew :SBConsumidor:bootRun`
- Luego proveedor:  
  - Maven: `./mvnw -pl SBProveedor -am spring-boot:run`  
  - Gradle: `./gradlew :SBProveedor:bootRun`

## 3) Probar el flujo SOAT
- Enviar placa: `POST http://localhost:8068/api/soat/enviar?placa=ABC123`
- Consultar resultado: `GET http://localhost:8068/api/soat/resultado?placa=ABC123`

Deberías ver en el consumidor logs del mensaje entrante y de la respuesta publicada.
