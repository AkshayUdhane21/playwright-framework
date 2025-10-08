package clients;

import config.MicroservicesConfig;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import com.microsoft.playwright.options.RequestOptions;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MicroservicesApiClient {
    private Playwright playwright;
    private APIRequestContext apiContext;
    private RequestSpecification restAssuredSpec;
    
    public MicroservicesApiClient(Playwright playwright) {
        this.playwright = playwright;
        this.apiContext = playwright.request().newContext();
        setupRestAssured();
    }
    
    private void setupRestAssured() {
        RestAssured.baseURI = MicroservicesConfig.getOpcUaServiceUrl();
        this.restAssuredSpec = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }
    
    // ================= OPC UA Connection Service APIs =================
    
    public APIResponse getOpcUaConnectionStatus() {
        return apiContext.get(MicroservicesConfig.getOpcUaServiceUrl() + "/api/connection/status");
    }
    
    public APIResponse initOpcUaConnection() {
        return apiContext.get(MicroservicesConfig.getOpcUaServiceUrl() + "/api/connection/init");
    }
    
    public APIResponse connectOpcUa() {
        return apiContext.get(MicroservicesConfig.getOpcUaServiceUrl() + "/api/connection/connect");
    }
    
    // ================= Read Data Service APIs =================
    
    public APIResponse browseTags(String startingNode) {
        String url = MicroservicesConfig.getReadDataServiceUrl() + "/api/read/browse";
        if (startingNode != null && !startingNode.isEmpty()) {
            url += "?startingNodeParam=" + startingNode;
        }
        return apiContext.get(url);
    }
    
    public APIResponse readValue(String nodeId) {
        return apiContext.get(MicroservicesConfig.getReadDataServiceUrl() + "/api/read/readValue?nodeId=" + nodeId);
    }
    
    public APIResponse subscribeToData() {
        return apiContext.get(MicroservicesConfig.getReadDataServiceUrl() + "/api/read/subscribeToData");
    }
    
    public APIResponse readNode(String nodeId) {
        String url = MicroservicesConfig.getReadDataServiceUrl() + "/api/read/read-node";
        if (nodeId != null && !nodeId.isEmpty()) {
            url += "?nodeId=" + nodeId;
        }
        return apiContext.get(url);
    }
    
    public APIResponse readNode2(String nodeId) {
        String url = MicroservicesConfig.getReadDataServiceUrl() + "/api/read/read-node2";
        if (nodeId != null && !nodeId.isEmpty()) {
            url += "?nodeId=" + nodeId;
        }
        return apiContext.get(url);
    }
    
    public APIResponse readTagValuesSimplified(String startingNode) {
        String url = MicroservicesConfig.getReadDataServiceUrl() + "/api/read/readTagValuesSimplified";
        if (startingNode != null && !startingNode.isEmpty()) {
            url += "?startingNode=" + startingNode;
        }
        return apiContext.get(url);
    }
    
    // ================= Write Data Service APIs =================
    
    public APIResponse writeNode(String nodeId, Object value) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("nodeId", nodeId);
        payload.put("value", value);
        
        return apiContext.post(MicroservicesConfig.getWriteDataServiceUrl() + "/api/write/write-node",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(payload));
    }
    
    // ================= Kafka Service APIs =================
    
    public APIResponse processBrowseData(String nodeId, Map<String, Object> browseData) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("nodeId", nodeId);
        payload.put("browseData", browseData);
        
        return apiContext.post(MicroservicesConfig.getKafkaServiceUrl() + "/api/kafkaBrowse/processBrowseData",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(payload));
    }
    
    public APIResponse hasChanged(Map<String, Object> previous, Map<String, Object> current) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("previous", previous);
        payload.put("current", current);
        
        return apiContext.post(MicroservicesConfig.getKafkaServiceUrl() + "/api/kafkaBrowse/hasChanged",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(payload));
    }
    
    // ================= Value Converter APIs =================
    
    public APIResponse convertValue(String variant) {
        return apiContext.post(MicroservicesConfig.getKafkaServiceUrl() + "/api/opcUaValueConverter/convertValue?variant=" + variant);
    }
    
    public APIResponse convertDataValue(String originalValue) {
        return apiContext.post(MicroservicesConfig.getKafkaServiceUrl() + "/api/opcUaValueConverter/convertDataValue?originalValue=" + originalValue);
    }
    
    // ================= Service Registry APIs =================
    
    public APIResponse getServiceRegistryStatus() {
        return apiContext.get(MicroservicesConfig.getServiceRegistryUrl() + "/actuator/health");
    }
    
    public APIResponse getRegisteredServices() {
        return apiContext.get(MicroservicesConfig.getServiceRegistryUrl() + "/eureka/apps");
    }
    
    // ================= Health Check APIs =================
    
    public APIResponse checkServiceHealth(String serviceName) {
        String baseUrl = getServiceBaseUrl(serviceName);
        return apiContext.get(baseUrl + "/actuator/health");
    }
    
    public APIResponse checkAllServicesHealth() {
        Map<String, Object> healthStatus = new HashMap<>();
        
        // Check each service
        String[] services = {"opcconnection", "readdata", "kafka", "writedata"};
        for (String service : services) {
            try {
                APIResponse response = checkServiceHealth(service);
                healthStatus.put(service, response.status() == 200 ? "UP" : "DOWN");
            } catch (Exception e) {
                healthStatus.put(service, "DOWN - " + e.getMessage());
            }
        }
        
        // Return a mock response with health status
        return new MockApiResponse(200, healthStatus);
    }
    
    // ================= Utility Methods =================
    
    private String getServiceBaseUrl(String serviceName) {
        switch (serviceName.toLowerCase()) {
            case "opcconnection":
                return MicroservicesConfig.getOpcUaServiceUrl();
            case "readdata":
                return MicroservicesConfig.getReadDataServiceUrl();
            case "kafka":
                return MicroservicesConfig.getKafkaServiceUrl();
            case "writedata":
                return MicroservicesConfig.getWriteDataServiceUrl();
            case "serviceregistry":
                return MicroservicesConfig.getServiceRegistryUrl();
            default:
                throw new IllegalArgumentException("Unknown service: " + serviceName);
        }
    }
    
    public void dispose() {
        if (apiContext != null) {
            apiContext.dispose();
        }
    }
    
    // ================= RestAssured Methods for Advanced Testing =================
    
    public Response getWithRestAssured(String endpoint) {
        return restAssuredSpec.get(endpoint);
    }
    
    public Response postWithRestAssured(String endpoint, Object body) {
        return restAssuredSpec.body(body).post(endpoint);
    }
    
    public Response putWithRestAssured(String endpoint, Object body) {
        return restAssuredSpec.body(body).put(endpoint);
    }
    
    public Response deleteWithRestAssured(String endpoint) {
        return restAssuredSpec.delete(endpoint);
    }
    
    // ================= Mock Response Class =================
    
    private static class MockApiResponse implements APIResponse {
        private final int status;
        private final Object body;
        private final Map<String, String> headers;
        
        public MockApiResponse(int status, Object body) {
            this.status = status;
            this.body = body;
            this.headers = new HashMap<>();
        }
        
        @Override
        public int status() {
            return status;
        }
        
        @Override
        public String text() {
            if (body == null) {
                return "";
            }
            return body.toString();
        }
        
        @Override
        public byte[] body() {
            if (body == null) {
                return new byte[0];
            }
            return body.toString().getBytes();
        }
        
        @Override
        public String url() {
            return "mock://localhost";
        }
        
        @Override
        public Map<String, String> headers() {
            return headers;
        }
        
        public String headerValue(String name) {
            return headers.get(name);
        }
        
        public List<String> headerValues(String name) {
            String value = headers.get(name);
            if (value == null) {
                return null;
            }
            return List.of(value);
        }
        
        public String headerValue(String name, String defaultValue) {
            return headers.getOrDefault(name, defaultValue);
        }
        
        @Override
        public boolean ok() {
            return status >= 200 && status < 300;
        }
        
        @Override
        public String statusText() {
            return "OK";
        }
        
        @Override
        public void dispose() {
            // No resources to dispose
        }
        
        @SuppressWarnings("unchecked")
        public <T> T fromJson(Class<T> clazz) {
            try {
                return (T) body;
            } catch (Exception e) {
                return null;
            }
        }
        
        public String fromJson(String path) {
            return text();
        }
        
        @SuppressWarnings("unchecked")
        public <T> T fromJson(String path, Class<T> clazz) {
            try {
                return (T) body;
            } catch (Exception e) {
                return null;
            }
        }

		@Override
		public List<HttpHeader> headersArray() {
			return headers.entrySet().stream()
					.map(entry -> {
						HttpHeader header = new HttpHeader();
						header.name = entry.getKey();
						header.value = entry.getValue();
						return header;
					})
					.collect(Collectors.toList());
		}
    }
}
