## Requisitos
- Java 17+
- Kafka instalado localmente (ej.: C:\kafka con Kafka 4.x)
- (Opcional) Postman o curl para probar los endpoints (https://reqbin.com/)

## Si es primera vez que se inicia:
`mkdir C:\kafka\data\kafka-logs`

`mkdir C:\kafka\data\controller`

Generar un UUID y cópialo (salida en una sola línea)
`bin\windows\kafka-storage.bat random-uuid`

Formatear el storage con ese UUID y el config KRaft
`bin\windows\kafka-storage.bat format -t <PEGA_AQUI_EL_UUID> -c config\server.properties`

Iniciar server
`bin\windows\kafka-server-start.bat config\server.properties`


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

El GET obtenido debe ser algo asi:

`{
  "placa": "ABC123",
  "soat": "VIGENTE",
  "ts": "2025-09-25T07:30:12.345Z",
  "fuente": "simulado"
}`

Deberia ver en el consumidor logs del mensaje entrante y de la respuesta publicada.

## 4) Aumentar Particiones Topic
`bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --alter --topic placas-soat --partitions 3`
- Enviar varias placas:
`POST http://localhost:8068/api/soat/enviar?placa=AAA111
POST http://localhost:8068/api/soat/enviar?placa=BBB222
POST http://localhost:8068/api/soat/enviar?placa=AAA111
POST http://localhost:8068/api/soat/enviar?placa=CCC333`

Los mensajes de AAA111 mantendrán orden y caerán siempre en la misma partición.
- Ver particiones en peticiones:
`bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic placas-soat --from-beginning --property print.key=true --property print.partition=true`


