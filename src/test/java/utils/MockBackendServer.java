package utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import config.TestConfigManager;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class MockBackendServer {
    private static MockBackendServer instance;
    private static ConfigurableApplicationContext context;
    private final int PORT = TestConfigManager.getMockServerPort();
    private static boolean isRunning = false;

    public static synchronized MockBackendServer getInstance() {
        if (instance == null) {
            instance = new MockBackendServer();
        }
        return instance;
    }

    public synchronized void start() {
        if (isRunning) {
            System.out.println("Mock backend server is already running on port " + PORT);
            return;
        }
        
        System.setProperty("server.port", String.valueOf(PORT));
        System.setProperty("spring.main.banner-mode", "off");
        System.setProperty("logging.level.org.springframework", "WARN");
        System.setProperty("logging.level.org.apache.catalina", "WARN");
        
        SpringApplication app = new SpringApplication(MockBackendServer.class);
        app.setDefaultProperties(getServerProperties());
        context = app.run();
        isRunning = true;
        System.out.println("Mock backend server started on port " + PORT);
    }

    private Map<String, Object> getServerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("server.port", PORT);
        props.put("spring.main.banner-mode", "off");
        props.put("logging.level.org.springframework", "WARN");
        props.put("logging.level.org.apache.catalina", "WARN");
        return props;
    }

    // ================= OPC UA Connection APIs =================
    
    @GetMapping("/api/connection/status")
    public Map<String, Object> getConnectionStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Connected");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @GetMapping("/api/connection/init")
    public Map<String, Object> initConnection() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "initialized");
        response.put("message", "OPC UA connection initialized successfully");
        return response;
    }
    
    @GetMapping("/api/connection/connect")
    public Map<String, Object> connect() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "connected");
        response.put("message", "OPC UA connection established");
        return response;
    }
    
    // ================= Read Data APIs =================
    
    @GetMapping("/api/read/browse")
    public Map<String, Object> browseTags() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("nodes", new Object[]{
            Map.of("nodeId", "ns=3;s=WMS_TO_PLC", "displayName", "WMS TO PLC", "nodeClass", "Variable"),
            Map.of("nodeId", "ns=3;s=PLC_TO_WMS", "displayName", "PLC TO WMS", "nodeClass", "Variable")
        });
        return response;
    }
    
    @GetMapping("/api/read/readValue")
    public Map<String, Object> readValue() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("value", 42);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @GetMapping("/api/read/subscribeToData")
    public Map<String, Object> subscribeToData() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "subscribed");
        response.put("subscriptionId", "sub_12345");
        return response;
    }
    
    @GetMapping("/api/read/read-node")
    public Map<String, Object> readNode() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("nodeId", "ns=3;s=DataBlocksGlobal");
        response.put("value", Map.of("temperature", 25.5, "pressure", 101.3, "active", true));
        return response;
    }
    
    @GetMapping("/api/read/read-node2")
    public Map<String, Object> readNode2() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("nodeId", "MCOM");
        response.put("value", "Communication Active");
        return response;
    }
    
    @GetMapping("/api/read/readTagValuesSimplified")
    public Map<String, Object> readTagValuesSimplified(@org.springframework.web.bind.annotation.RequestParam(required = false) String startingNode) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("startingNode", startingNode != null ? startingNode : "ns=3;s=DataBlocksGlobal");
        response.put("values", new Object[]{
            Map.of("nodeId", "ns=3;s=WMS_TO_PLC", "value", 42, "timestamp", System.currentTimeMillis()),
            Map.of("nodeId", "ns=3;s=PLC_TO_WMS", "value", "Active", "timestamp", System.currentTimeMillis())
        });
        return response;
    }
    
    // ================= Write Data APIs =================
    
    @org.springframework.web.bind.annotation.PostMapping("/api/write/write-node")
    public Map<String, Object> writeNode(@org.springframework.web.bind.annotation.RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Node value written successfully");
        response.put("nodeId", payload.get("nodeId"));
        response.put("writtenValue", payload.get("value"));
        return response;
    }
    
    // ================= Kafka APIs =================
    
    @org.springframework.web.bind.annotation.PostMapping("/api/kafkaBrowse/processBrowseData")
    public Map<String, Object> processBrowseData(@org.springframework.web.bind.annotation.RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "processed");
        response.put("message", "Browse data processed successfully");
        response.put("processedData", payload);
        return response;
    }
    
    @org.springframework.web.bind.annotation.PostMapping("/api/kafkaBrowse/hasChanged")
    public Map<String, Object> hasChanged(@org.springframework.web.bind.annotation.RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("hasChanged", true);
        response.put("differences", Map.of("field", "a", "oldValue", 1, "newValue", 2));
        return response;
    }
    
    // ================= Value Converter APIs =================
    
    @org.springframework.web.bind.annotation.PostMapping("/api/opcUaValueConverter/convertValue")
    public Map<String, Object> convertValue() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("convertedValue", "42.0");
        response.put("dataType", "Double");
        return response;
    }
    
    @org.springframework.web.bind.annotation.PostMapping("/api/opcUaValueConverter/convertDataValue")
    public Map<String, Object> convertDataValue() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("convertedDataValue", "100.0");
        response.put("sourceDataType", "Integer");
        response.put("targetDataType", "Double");
        return response;
    }

    // ================= Health Check Endpoint =================
    
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        response.put("services", Map.of(
            "opcua", "Connected",
            "kafka", "Active",
            "database", "Connected"
        ));
        return response;
    }

    // ================= Legacy Backend APIs (for future use) ================="
    
    @GetMapping("/fiat-backend-0.0.1/masterReasonDetails/fetchMasterReasonDetails")
    public Map<String, Object> getMasterReasonDetails() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", new Object[]{
            Map.of("id", 1, "reason", "Test Reason 1"),
            Map.of("id", 2, "reason", "Test Reason 2")
        });
        return response;
    }

    @GetMapping("/fiat-backend-0.0.1/masterProductVariant/fetchAllMasterProductVariant")
    public Map<String, Object> getAllMasterProductVariant() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", new Object[]{
            Map.of("id", 1, "name", "Product 1", "variant", "Variant A"),
            Map.of("id", 2, "name", "Product 2", "variant", "Variant B")
        });
        return response;
    }

    @GetMapping("/fiat-backend-0.0.1/masterEquipmentDetails/{id}/status")
    public Map<String, Object> getEquipmentStatus(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("equipmentStatus", "ACTIVE");
        response.put("lastUpdated", "2024-01-01T00:00:00Z");
        return response;
    }

    public synchronized void stop() {
        if (context != null) {
            context.close();
            context = null;
            isRunning = false;
            System.out.println("Mock backend server stopped");
        }
    }

    public static synchronized void stopInstance() {
        if (instance != null && context != null) {
            context.close();
            context = null;
            isRunning = false;
            instance = null;
            System.out.println("Mock backend server instance stopped");
        }
    }

    public static boolean isRunning() {
        return isRunning;
    }
} 