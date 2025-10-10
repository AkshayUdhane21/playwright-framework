package utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import config.TestConfigManager;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class UnifiedMockServer {
    private static UnifiedMockServer instance;
    private static ConfigurableApplicationContext context;
    private static boolean isRunning = false;
    
    // Use a single port for all mock services
    private static final int MOCK_SERVER_PORT = 8085;

    public static synchronized UnifiedMockServer getInstance() {
        if (instance == null) {
            instance = new UnifiedMockServer();
        }
        return instance;
    }

    public synchronized void start() {
        if (isRunning) {
            System.out.println("Unified mock server is already running on port " + MOCK_SERVER_PORT);
            return;
        }
        
        System.out.println("Starting unified mock server...");
        
        System.setProperty("server.port", String.valueOf(MOCK_SERVER_PORT));
        System.setProperty("spring.main.banner-mode", "off");
        System.setProperty("logging.level.org.springframework", "WARN");
        System.setProperty("logging.level.org.apache.catalina", "WARN");
        
        SpringApplication app = new SpringApplication(UnifiedMockServer.class);
        app.setDefaultProperties(getServerProperties());
        context = app.run();
        isRunning = true;
        
        System.out.println("✅ Unified mock server started successfully!");
        System.out.println("All services available on: http://localhost:" + MOCK_SERVER_PORT);
        System.out.println("- Service Registry: http://localhost:" + MOCK_SERVER_PORT + "/eureka");
        System.out.println("- OPC UA Service: http://localhost:" + MOCK_SERVER_PORT + "/opcua");
        System.out.println("- Read Data Service: http://localhost:" + MOCK_SERVER_PORT + "/read");
        System.out.println("- Kafka Service: http://localhost:" + MOCK_SERVER_PORT + "/kafka");
        System.out.println("- Write Data Service: http://localhost:" + MOCK_SERVER_PORT + "/write");
    }

    private Map<String, Object> getServerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("server.port", MOCK_SERVER_PORT);
        props.put("spring.main.banner-mode", "off");
        props.put("logging.level.org.springframework", "WARN");
        props.put("logging.level.org.apache.catalina", "WARN");
        return props;
    }

    // ================= Service Registry APIs =================
    
    @GetMapping("/eureka/actuator/health")
    public Map<String, Object> getServiceRegistryHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        
        Map<String, Object> components = new HashMap<>();
        Map<String, Object> eureka = new HashMap<>();
        eureka.put("status", "UP");
        Map<String, Object> discovery = new HashMap<>();
        discovery.put("status", "UP");
        components.put("eureka", eureka);
        components.put("discoveryComposite", discovery);
        response.put("components", components);
        
        return response;
    }
    
    @GetMapping("/eureka/apps")
    public Map<String, Object> getEurekaApps() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> applications = new HashMap<>();
        applications.put("versions__delta", "1");
        applications.put("apps__hashcode", "UP_1_");
        applications.put("application", new Object[0]);
        response.put("applications", applications);
        return response;
    }

    // ================= OPC UA Service APIs =================
    
    @GetMapping("/opcua/actuator/health")
    public Map<String, Object> getOpcUaHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        
        Map<String, Object> components = new HashMap<>();
        Map<String, Object> opcua = new HashMap<>();
        opcua.put("status", "UP");
        Map<String, Object> connection = new HashMap<>();
        connection.put("status", "UP");
        components.put("opcua", opcua);
        components.put("connection", connection);
        response.put("components", components);
        
        return response;
    }
    
    @GetMapping("/opcua/api/connection/status")
    public Map<String, Object> getOpcUaConnectionStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Connected");
        response.put("timestamp", System.currentTimeMillis());
        response.put("connectionId", "mock-connection-" + System.currentTimeMillis());
        return response;
    }
    
    @GetMapping("/opcua/api/connection/init")
    public Map<String, Object> initOpcUaConnection() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "OPC UA connection initialized");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @GetMapping("/opcua/api/connection/connect")
    public Map<String, Object> connectOpcUa() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "OPC UA connection established");
        response.put("connectionId", "mock-connection-" + System.currentTimeMillis());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @PostMapping(value = "/opcua/api/connect", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> connectOpcUaPost(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "OPC UA connection established");
        response.put("connectionId", "mock-connection-" + System.currentTimeMillis());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    // ================= Read Data Service APIs =================
    
    @GetMapping("/read/actuator/health")
    public Map<String, Object> getReadDataHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        
        Map<String, Object> components = new HashMap<>();
        Map<String, Object> readdata = new HashMap<>();
        readdata.put("status", "UP");
        Map<String, Object> database = new HashMap<>();
        database.put("status", "UP");
        components.put("readdata", readdata);
        components.put("database", database);
        response.put("components", components);
        
        return response;
    }
    
    @GetMapping("/read/api/read/browse")
    public Map<String, Object> browseTags(@RequestParam(required = false) String startingNodeParam) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("startingNode", startingNodeParam != null ? startingNodeParam : "ns=3;s=\"WMS TO PLC\"");
        
        Object[] tags = new Object[3];
        Map<String, Object> tag1 = new HashMap<>();
        tag1.put("nodeId", "ns=3;s=\"PLC_To_WMS\"");
        tag1.put("name", "PLC_To_WMS");
        tag1.put("type", "String");
        tags[0] = tag1;
        
        Map<String, Object> tag2 = new HashMap<>();
        tag2.put("nodeId", "ns=3;s=\"WMS_To_PLC\"");
        tag2.put("name", "WMS_To_PLC");
        tag2.put("type", "String");
        tags[1] = tag2;
        
        Map<String, Object> tag3 = new HashMap<>();
        tag3.put("nodeId", "ns=3;s=DataBlocksGlobal");
        tag3.put("name", "DataBlocksGlobal");
        tag3.put("type", "Object");
        tags[2] = tag3;
        
        response.put("tags", tags);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @GetMapping("/read/api/read/readValue")
    public Map<String, Object> readValue(@RequestParam String nodeId) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("nodeId", nodeId);
        response.put("value", "mock-value-" + System.currentTimeMillis());
        response.put("timestamp", System.currentTimeMillis());
        response.put("quality", "GOOD");
        return response;
    }
    
    @GetMapping("/read/api/read/subscribeToData")
    public Map<String, Object> subscribeToData() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Successfully subscribed to data updates");
        response.put("subscriptionId", "mock-subscription-" + System.currentTimeMillis());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @GetMapping("/read/api/read/read-node")
    public Map<String, Object> readNode(@RequestParam String nodeId) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("nodeId", nodeId);
        response.put("value", "mock-value-" + System.currentTimeMillis());
        response.put("timestamp", System.currentTimeMillis());
        response.put("quality", "GOOD");
        return response;
    }
    
    @GetMapping("/read/api/read/read-node2")
    public Map<String, Object> readNode2(@RequestParam String nodeId) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("nodeId", nodeId);
        response.put("value", "mock-value-" + System.currentTimeMillis());
        response.put("timestamp", System.currentTimeMillis());
        response.put("quality", "GOOD");
        return response;
    }
    
    @PostMapping(value = "/read/api/read-node", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> readNodePost(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("nodeId", request.get("nodeId"));
        response.put("value", "mock-value-" + System.currentTimeMillis());
        response.put("timestamp", System.currentTimeMillis());
        response.put("quality", "GOOD");
        return response;
    }

    // ================= Kafka Service APIs =================
    
    @GetMapping("/kafka/actuator/health")
    public Map<String, Object> getKafkaHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        
        Map<String, Object> components = new HashMap<>();
        Map<String, Object> kafka = new HashMap<>();
        kafka.put("status", "UP");
        Map<String, Object> producer = new HashMap<>();
        producer.put("status", "UP");
        Map<String, Object> consumer = new HashMap<>();
        consumer.put("status", "UP");
        components.put("kafka", kafka);
        components.put("producer", producer);
        components.put("consumer", consumer);
        response.put("components", components);
        
        return response;
    }
    
    @PostMapping(value = "/kafka/api/kafkaBrowse/processBrowseData", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> processBrowseData(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("nodeId", request.get("nodeId"));
        response.put("browseData", request.get("browseData"));
        response.put("processedAt", System.currentTimeMillis());
        response.put("message", "Browse data processed successfully");
        return response;
    }
    
    @PostMapping(value = "/kafka/api/kafkaBrowse/hasChanged", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> hasChanged(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("hasChanged", true);
        response.put("previous", request.get("previous"));
        response.put("current", request.get("current"));
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @PostMapping(value = "/kafka/api/opcUaValueConverter/convertValue", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> convertValue(@RequestParam String variant) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("originalVariant", variant);
        response.put("convertedValue", "converted-" + variant);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @PostMapping(value = "/kafka/api/opcUaValueConverter/convertDataValue", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> convertDataValue(@RequestParam String originalValue) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("originalValue", originalValue);
        response.put("convertedValue", "converted-" + originalValue);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @PostMapping(value = "/kafka/api/process-value", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> processValue(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("originalValue", request.get("value"));
        response.put("processedValue", "processed-" + request.get("value"));
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    // ================= Write Data Service APIs =================
    
    @GetMapping("/write/actuator/health")
    public Map<String, Object> getWriteDataHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        
        Map<String, Object> components = new HashMap<>();
        Map<String, Object> writedata = new HashMap<>();
        writedata.put("status", "UP");
        Map<String, Object> database = new HashMap<>();
        database.put("status", "UP");
        components.put("writedata", writedata);
        components.put("database", database);
        response.put("components", components);
        
        return response;
    }
    
    @PostMapping(value = "/write/api/write/write-node", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> writeNode(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("nodeId", request.get("nodeId"));
        response.put("value", request.get("value"));
        response.put("timestamp", System.currentTimeMillis());
        response.put("message", "Data written successfully");
        return response;
    }
    
    @PostMapping(value = "/write/api/write-node", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> writeNodeAlt(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("nodeId", request.get("nodeId"));
        response.put("value", request.get("value"));
        response.put("timestamp", System.currentTimeMillis());
        response.put("message", "Data written successfully");
        return response;
    }

    // ================= General Health Check =================
    
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        
        Map<String, Object> services = new HashMap<>();
        services.put("opcua", "Connected");
        services.put("kafka", "Active");
        services.put("database", "Connected");
        services.put("readdata", "Active");
        services.put("writedata", "Active");
        response.put("services", services);
        
        return response;
    }

    public synchronized void stop() {
        if (!isRunning) {
            System.out.println("Unified mock server is not running");
            return;
        }
        
        System.out.println("Stopping unified mock server...");
        
        if (context != null) {
            try {
                context.close();
                System.out.println("SUCCESS: Unified mock server stopped");
            } catch (Exception e) {
                System.err.println("ERROR: Error stopping unified mock server: " + e.getMessage());
            }
        }
        
        isRunning = false;
    }

    public static synchronized void stopInstance() {
        if (instance != null) {
            instance.stop();
            instance = null;
            System.out.println("Unified mock server instance stopped");
        }
    }

    public static boolean isRunning() {
        return isRunning;
    }
    
    public static int getPort() {
        return MOCK_SERVER_PORT;
    }
}