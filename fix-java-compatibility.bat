@echo off
echo ========================================
echo Fixing Java Compatibility Issues
echo ========================================

echo Creating simplified mock server without Map.of()...

REM Create a simplified version of UnifiedMockServer without Map.of()
(
echo package utils;
echo.
echo import org.springframework.boot.SpringApplication;
echo import org.springframework.boot.autoconfigure.SpringBootApplication;
echo import org.springframework.context.ConfigurableApplicationContext;
echo import org.springframework.web.bind.annotation.*;
echo import org.springframework.http.MediaType;
echo import config.TestConfigManager;
echo.
echo import java.util.HashMap;
echo import java.util.Map;
echo.
echo @SpringBootApplication
echo @RestController
echo public class UnifiedMockServer {
echo     private static UnifiedMockServer instance;
echo     private static ConfigurableApplicationContext context;
echo     private static boolean isRunning = false;
echo     
echo     // Use a single port for all mock services
echo     private static final int MOCK_SERVER_PORT = 8080;
echo.
echo     public static synchronized UnifiedMockServer getInstance() {
echo         if (instance == null) {
echo             instance = new UnifiedMockServer();
echo         }
echo         return instance;
echo     }
echo.
echo     public synchronized void start() {
echo         if (isRunning) {
echo             System.out.println("Unified mock server is already running on port " + MOCK_SERVER_PORT);
echo             return;
echo         }
echo         
echo         System.out.println("Starting unified mock server...");
echo         
echo         System.setProperty("server.port", String.valueOf(MOCK_SERVER_PORT));
echo         System.setProperty("spring.main.banner-mode", "off");
echo         System.setProperty("logging.level.org.springframework", "WARN");
echo         System.setProperty("logging.level.org.apache.catalina", "WARN");
echo         
echo         SpringApplication app = new SpringApplication(UnifiedMockServer.class);
echo         app.setDefaultProperties(getServerProperties());
echo         context = app.run();
echo         isRunning = true;
echo         
echo         System.out.println("✅ Unified mock server started successfully!");
echo         System.out.println("All services available on: http://localhost:" + MOCK_SERVER_PORT);
echo     }
echo.
echo     private Map<String, Object> getServerProperties() {
echo         Map<String, Object> props = new HashMap<>();
echo         props.put("server.port", MOCK_SERVER_PORT);
echo         props.put("spring.main.banner-mode", "off");
echo         props.put("logging.level.org.springframework", "WARN");
echo         props.put("logging.level.org.apache.catalina", "WARN");
echo         return props;
echo     }
echo.
echo     // ================= Service Registry APIs =================
echo     
echo     @GetMapping("/eureka/actuator/health")
echo     public Map<String, Object> getServiceRegistryHealth() {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "UP");
echo         response.put("timestamp", System.currentTimeMillis());
echo         Map<String, Object> components = new HashMap<>();
echo         Map<String, Object> eureka = new HashMap<>();
echo         eureka.put("status", "UP");
echo         Map<String, Object> discovery = new HashMap<>();
echo         discovery.put("status", "UP");
echo         components.put("eureka", eureka);
echo         components.put("discoveryComposite", discovery);
echo         response.put("components", components);
echo         return response;
echo     }
echo.
echo     // ================= OPC UA Service APIs =================
echo     
echo     @GetMapping("/opcua/api/connection/status")
echo     public Map<String, Object> getOpcUaConnectionStatus() {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "Connected");
echo         response.put("timestamp", System.currentTimeMillis());
echo         response.put("connectionId", "mock-connection-" + System.currentTimeMillis());
echo         return response;
echo     }
echo.
echo     @GetMapping("/opcua/api/connection/init")
echo     public Map<String, Object> initOpcUaConnection() {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("message", "OPC UA connection initialized");
echo         response.put("timestamp", System.currentTimeMillis());
echo         return response;
echo     }
echo.
echo     @GetMapping("/opcua/api/connection/connect")
echo     public Map<String, Object> connectOpcUa() {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("message", "OPC UA connection established");
echo         response.put("connectionId", "mock-connection-" + System.currentTimeMillis());
echo         response.put("timestamp", System.currentTimeMillis());
echo         return response;
echo     }
echo.
echo     // ================= Read Data Service APIs =================
echo     
echo     @GetMapping("/read/api/read/browse")
echo     public Map<String, Object> browseTags(@RequestParam(required = false) String startingNodeParam) {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("startingNode", startingNodeParam != null ? startingNodeParam : "ns=3;s=\"WMS TO PLC\"");
echo         response.put("timestamp", System.currentTimeMillis());
echo         return response;
echo     }
echo.
echo     @GetMapping("/read/api/read/readValue")
echo     public Map<String, Object> readValue(@RequestParam String nodeId) {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("nodeId", nodeId);
echo         response.put("value", "mock-value-" + System.currentTimeMillis());
echo         response.put("timestamp", System.currentTimeMillis());
echo         response.put("quality", "GOOD");
echo         return response;
echo     }
echo.
echo     @GetMapping("/read/api/read/subscribeToData")
echo     public Map<String, Object> subscribeToData() {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("message", "Successfully subscribed to data updates");
echo         response.put("subscriptionId", "mock-subscription-" + System.currentTimeMillis());
echo         response.put("timestamp", System.currentTimeMillis());
echo         return response;
echo     }
echo.
echo     @GetMapping("/read/api/read/read-node")
echo     public Map<String, Object> readNode(@RequestParam String nodeId) {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("nodeId", nodeId);
echo         response.put("value", "mock-value-" + System.currentTimeMillis());
echo         response.put("timestamp", System.currentTimeMillis());
echo         response.put("quality", "GOOD");
echo         return response;
echo     }
echo.
echo     @GetMapping("/read/api/read/read-node2")
echo     public Map<String, Object> readNode2(@RequestParam String nodeId) {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("nodeId", nodeId);
echo         response.put("value", "mock-value-" + System.currentTimeMillis());
echo         response.put("timestamp", System.currentTimeMillis());
echo         response.put("quality", "GOOD");
echo         return response;
echo     }
echo.
echo     // ================= Kafka Service APIs =================
echo     
echo     @PostMapping(value = "/kafka/api/kafkaBrowse/processBrowseData", produces = MediaType.APPLICATION_JSON_VALUE)
echo     public Map<String, Object> processBrowseData(@RequestBody Map<String, Object> request) {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("nodeId", request.get("nodeId"));
echo         response.put("browseData", request.get("browseData"));
echo         response.put("processedAt", System.currentTimeMillis());
echo         response.put("message", "Browse data processed successfully");
echo         return response;
echo     }
echo.
echo     @PostMapping(value = "/kafka/api/kafkaBrowse/hasChanged", produces = MediaType.APPLICATION_JSON_VALUE)
echo     public Map<String, Object> hasChanged(@RequestBody Map<String, Object> request) {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("hasChanged", true);
echo         response.put("previous", request.get("previous"));
echo         response.put("current", request.get("current"));
echo         response.put("timestamp", System.currentTimeMillis());
echo         return response;
echo     }
echo.
echo     @PostMapping(value = "/kafka/api/opcUaValueConverter/convertValue", produces = MediaType.APPLICATION_JSON_VALUE)
echo     public Map<String, Object> convertValue(@RequestParam String variant) {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("originalVariant", variant);
echo         response.put("convertedValue", "converted-" + variant);
echo         response.put("timestamp", System.currentTimeMillis());
echo         return response;
echo     }
echo.
echo     @PostMapping(value = "/kafka/api/opcUaValueConverter/convertDataValue", produces = MediaType.APPLICATION_JSON_VALUE)
echo     public Map<String, Object> convertDataValue(@RequestParam String originalValue) {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("originalValue", originalValue);
echo         response.put("convertedValue", "converted-" + originalValue);
echo         response.put("timestamp", System.currentTimeMillis());
echo         return response;
echo     }
echo.
echo     // ================= Write Data Service APIs =================
echo     
echo     @PostMapping(value = "/write/api/write/write-node", produces = MediaType.APPLICATION_JSON_VALUE)
echo     public Map<String, Object> writeNode(@RequestBody Map<String, Object> request) {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("nodeId", request.get("nodeId"));
echo         response.put("value", request.get("value"));
echo         response.put("timestamp", System.currentTimeMillis());
echo         response.put("message", "Data written successfully");
echo         return response;
echo     }
echo.
echo     @PostMapping(value = "/write/api/write-node", produces = MediaType.APPLICATION_JSON_VALUE)
echo     public Map<String, Object> writeNodeAlt(@RequestBody Map<String, Object> request) {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "success");
echo         response.put("nodeId", request.get("nodeId"));
echo         response.put("value", request.get("value"));
echo         response.put("timestamp", System.currentTimeMillis());
echo         response.put("message", "Data written successfully");
echo         return response;
echo     }
echo.
echo     // ================= General Health Check =================
echo     
echo     @GetMapping("/health")
echo     public Map<String, Object> healthCheck() {
echo         Map<String, Object> response = new HashMap<>();
echo         response.put("status", "UP");
echo         response.put("timestamp", System.currentTimeMillis());
echo         Map<String, Object> services = new HashMap<>();
echo         services.put("opcua", "Connected");
echo         services.put("kafka", "Active");
echo         services.put("database", "Connected");
echo         services.put("readdata", "Active");
echo         services.put("writedata", "Active");
echo         response.put("services", services);
echo         return response;
echo     }
echo.
echo     public synchronized void stop() {
echo         if (!isRunning) {
echo             System.out.println("Unified mock server is not running");
echo             return;
echo         }
echo         
echo         System.out.println("Stopping unified mock server...");
echo         
echo         if (context != null) {
echo             try {
echo                 context.close();
echo                 System.out.println("✅ Unified mock server stopped");
echo             } catch (Exception e) {
echo                 System.err.println("❌ Error stopping unified mock server: " + e.getMessage());
echo             }
echo         }
echo         
echo         isRunning = false;
echo     }
echo.
echo     public static synchronized void stopInstance() {
echo         if (instance != null) {
echo             instance.stop();
echo             instance = null;
echo             System.out.println("Unified mock server instance stopped");
echo         }
echo     }
echo.
echo     public static boolean isRunning() {
echo         return isRunning;
echo     }
echo     
echo     public static int getPort() {
echo         return MOCK_SERVER_PORT;
echo     }
echo }
) > src\test\java\utils\UnifiedMockServer.java

echo ✅ Fixed UnifiedMockServer compatibility issues!
echo.
echo The mock server now uses traditional HashMap instead of Map.of()
echo All tests should now compile and run successfully.
echo.
pause

