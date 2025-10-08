package clients;

import config.MicroservicesConfig;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.microsoft.playwright.options.HttpHeader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Lightweight API client using Playwright's APIRequestContext.
 * - Uses URL encoding for query params.
 * - Includes a simple mock response for aggregated health check.
 */
public class SimpleApiClient {
    private final Playwright playwright;
    private final APIRequestContext apiContext;

    public SimpleApiClient(Playwright playwright) {
        this.playwright = Objects.requireNonNull(playwright, "playwright cannot be null");
        this.apiContext = playwright.request().newContext();
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
            url += "?startingNodeParam=" + encode(startingNode);
        }
        return apiContext.get(url);
    }

    public APIResponse readValue(String nodeId) {
        String url = MicroservicesConfig.getReadDataServiceUrl() + "/api/read/readValue";
        if (nodeId != null && !nodeId.isEmpty()) {
            url += "?nodeId=" + encode(nodeId);
        }
        return apiContext.get(url);
    }

    public APIResponse subscribeToData() {
        return apiContext.get(MicroservicesConfig.getReadDataServiceUrl() + "/api/read/subscribeToData");
    }

    public APIResponse readNode(String nodeId) {
        String url = MicroservicesConfig.getReadDataServiceUrl() + "/api/read/read-node";
        if (nodeId != null && !nodeId.isEmpty()) {
            url += "?nodeId=" + encode(nodeId);
        }
        return apiContext.get(url);
    }

    public APIResponse readNode2(String nodeId) {
        String url = MicroservicesConfig.getReadDataServiceUrl() + "/api/read/read-node2";
        if (nodeId != null && !nodeId.isEmpty()) {
            url += "?nodeId=" + encode(nodeId);
        }
        return apiContext.get(url);
    }

    public APIResponse readTagValuesSimplified(String startingNode) {
        String url = MicroservicesConfig.getReadDataServiceUrl() + "/api/read/readTagValuesSimplified";
        if (startingNode != null && !startingNode.isEmpty()) {
            url += "?startingNode=" + encode(startingNode);
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
        String url = MicroservicesConfig.getKafkaServiceUrl() + "/api/opcUaValueConverter/convertValue";
        if (variant != null && !variant.isEmpty()) {
            url += "?variant=" + encode(variant);
        }
        return apiContext.post(url);
    }

    public APIResponse convertDataValue(String originalValue) {
        String url = MicroservicesConfig.getKafkaServiceUrl() + "/api/opcUaValueConverter/convertDataValue";
        if (originalValue != null && !originalValue.isEmpty()) {
            url += "?originalValue=" + encode(originalValue);
        }
        return apiContext.post(url);
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

    /**
     * Aggregates health status for configured services and returns a simple mock-like APIResponse.
     * Note: This uses a local SimpleMockResponse (not a network call).
     */
    public APIResponse checkAllServicesHealth() {
        Map<String, Object> healthStatus = new LinkedHashMap<>();

        // Check each service
        String[] services = {"opcconnection", "readdata", "kafka", "writedata"};
        for (String service : services) {
            try {
                APIResponse response = checkServiceHealth(service);
                healthStatus.put(service, response.status() >= 200 && response.status() < 300 ? "UP" : "DOWN");
            } catch (Exception e) {
                healthStatus.put(service, "DOWN - " + e.getMessage());
            }
        }

        // Return a simple mock response
        return new SimpleMockResponse(200, healthStatus);
    }

    // ================= Utility Methods =================

    private String getServiceBaseUrl(String serviceName) {
        if (serviceName == null) {
            throw new IllegalArgumentException("serviceName cannot be null");
        }
        switch (serviceName.toLowerCase(Locale.ROOT)) {
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

    private static String encode(String value) {
        if (value == null) return "";
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // UTF-8 always supported - fallback (shouldn't happen)
            return value;
        }
    }

    public void dispose() {
        try {
            if (apiContext != null) {
                apiContext.dispose();
            }
        } finally {
            // do not close Playwright here; caller may manage it.
        }
    }

    // ================= Simple Mock Response Class =================

    /**
     * A tiny in-memory mock implementation used for returning aggregated health results.
     * It implements enough of APIResponse used by client code (status(), text(), body(), ok(), headers()).
     *
     * NOTE: Playwright's APIResponse API evolves across versions. If your Playwright version's
     * APIResponse interface requires additional methods, you may need to add them here.
     */
    private static class SimpleMockResponse implements APIResponse {
        private final int status;
        private final Object body;
        private final Map<String, String> headers;

        public SimpleMockResponse(int status, Object body) {
            this.status = status;
            this.body = body;
            this.headers = Collections.emptyMap();
        }

        @Override
        public int status() {
            return status;
        }

        @Override
        public String text() {
            if (body == null) return "";
            return body.toString();
        }

        @Override
        public byte[] body() {
            if (body == null) return new byte[0];
            return text().getBytes(StandardCharsets.UTF_8);
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
            String v = headers.get(name);
            return v == null ? Collections.emptyList() : Collections.singletonList(v);
        }

        public String headerValue(String name, String defaultValue) {
            return headers.getOrDefault(name, defaultValue);
        }

        @Override
        public List<HttpHeader> headersArray() {
            return headers.entrySet().stream()
                    .map(entry -> {
                        HttpHeader header = new HttpHeader();
                        // Note: HttpHeader constructor might not accept parameters
                        // This is a simplified implementation
                        return header;
                    })
                    .collect(Collectors.toList());
        }

        @Override
        public boolean ok() {
            return status >= 200 && status < 300;
        }

        @Override
        public String statusText() {
            return ok() ? "OK" : "ERROR";
        }

        @Override
        public void dispose() {
            // nothing to clean up
        }

        // Some Playwright versions add JSON helpers on APIResponse; these are optional.
        // If your Playwright version defines them on APIResponse, add compatible implementations.
        @SuppressWarnings("unchecked")
        public <T> T fromJson(Class<T> clazz) {
            // best-effort: if body is already the required type, cast; otherwise return null
            try {
                if (clazz.isInstance(body)) {
                    return (T) body;
                }
            } catch (Exception ignored) {
            }
            return null;
        }

        public String fromJson(String path) {
            // Not implementing JSONPath in mock — return full text
            return text();
        }

        @SuppressWarnings("unchecked")
        public <T> T fromJson(String path, Class<T> clazz) {
            // Not implementing JSONPath in mock — attempt cast
            return fromJson(clazz);
        }
    }
}
