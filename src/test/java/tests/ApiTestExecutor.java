package tests;

import base.ApiTestBase;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import config.TestConfigManager;
import config.MicroservicesConfig;
import org.testng.Assert;
import org.testng.annotations.*;

public class ApiTestExecutor extends ApiTestBase {
    private APIRequestContext request;

    @BeforeClass
    public void setup() {
        // Use the inherited playwright instance from ApiTestBase
        String baseUrl;
        if (TestConfigManager.isMockMode()) {
            baseUrl = "http://localhost:" + TestConfigManager.getMockServerPort();
        } else {
            baseUrl = MicroservicesConfig.getOpcUaServiceUrl();
        }
        
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(baseUrl));
        
        System.out.println("ApiTestExecutor configured with base URL: " + baseUrl);
    }

    @AfterClass
    public void teardown() {
        if (request != null) request.dispose();
    }

    // ================= OPC UA Connection APIs =================

    @Test
    public void testOpcConnectionStatus() {
        String url = "/opcua/api/connection/status";
        System.out.println("Testing URL: " + url);
        APIResponse response = request.get(url);
        System.out.println("Response status: " + response.status());
        System.out.println("Response body: " + response.text());
        Assert.assertEquals(response.status(), 200);
        String body = response.text();
        Assert.assertTrue(body.contains("Connected") || body.contains("Disconnected"));
    }

    @Test
    public void testOpcInit() {
        APIResponse response = request.get("/opcua/api/connection/init");
        Assert.assertEquals(response.status(), 200);
    }

    @Test
    public void testOpcConnect() {
        APIResponse response = request.get("/opcua/api/connection/connect");
        Assert.assertEquals(response.status(), 200);
    }

    // ================= Read Data APIs =================

    @Test
    public void testBrowseTags() {
        APIResponse response = request.get("/read/api/read/browse?startingNodeParam=ns=3;s=\"WMS TO PLC\"");
        Assert.assertEquals(response.status(), 200);
        System.out.println("Browse tags: " + response.text());
    }

    @Test
    public void testReadValue() {
        APIResponse response = request.get("/read/api/read/readValue?nodeId=ns=3;s=\"PLC_To_WMS\"");
        Assert.assertEquals(response.status(), 200);
        System.out.println("Read value: " + response.text());
    }

    @Test
    public void testSubscribeToData() {
        APIResponse response = request.get("/read/api/read/subscribeToData");
        Assert.assertEquals(response.status(), 200);
    }

    @Test
    public void testReadNode() {
        APIResponse response = request.get("/read/api/read/read-node?nodeId=ns=3;s=DataBlocksGlobal");
        Assert.assertEquals(response.status(), 200);
        System.out.println("Read node values: " + response.text());
    }

    @Test
    public void testReadNode2() {
        APIResponse response = request.get("/read/api/read/read-node2?nodeId=MCOM");
        Assert.assertEquals(response.status(), 200);
        System.out.println("Read node2 values: " + response.text());
    }

    // ================= Write Data APIs =================

    @Test
    public void testWriteNode() {
        String payload = "{ \"nodeId\": \"ns=3;s=DataBlocksGlobal\", \"value\": 123 }";
        APIResponse response = request.post("/write/api/write/write-node",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(payload));
        Assert.assertEquals(response.status(), 200);
        System.out.println("Write node response: " + response.text());
    }

    // ================= Kafka APIs =================

    @Test
    public void testKafkaProcessBrowseData() {
        String payload = "{ \"nodeId\": \"ns=3;s=DataBlocksGlobal\", \"browseData\": {\"tag\": \"PLC_To_WMS\"} }";
        APIResponse response = request.post("/kafka/api/kafkaBrowse/processBrowseData",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(payload));
        Assert.assertEquals(response.status(), 200);
    }

    @Test
    public void testKafkaHasChanged() {
        String payload = "{ \"previous\": {\"a\": 1}, \"current\": {\"a\": 2} }";
        APIResponse response = request.post("/kafka/api/kafkaBrowse/hasChanged",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(payload));
        Assert.assertEquals(response.status(), 200);
        System.out.println("HasChanged result: " + response.text());
    }

    // ================= Value Converter APIs =================

    @Test
    public void testConvertValue() {
        APIResponse response = request.post("/kafka/api/opcUaValueConverter/convertValue?variant=42");
        Assert.assertEquals(response.status(), 200);
        System.out.println("ConvertValue: " + response.text());
    }

    @Test
    public void testConvertDataValue() {
        APIResponse response = request.post("/kafka/api/opcUaValueConverter/convertDataValue?originalValue=100");
        Assert.assertEquals(response.status(), 200);
        System.out.println("ConvertDataValue: " + response.text());
    }
}
