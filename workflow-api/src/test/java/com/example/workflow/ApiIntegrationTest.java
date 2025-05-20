package com.example.workflow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private HttpHeaders headers;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port;
        headers = new HttpHeaders();
        // 获取 API Key
        ResponseEntity<String> keyResp = restTemplate.getForEntity(baseUrl + "/api-key/generate", String.class);
        Assertions.assertEquals(HttpStatus.OK, keyResp.getStatusCode());
        String apiKey = keyResp.getBody();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testWorkflowApi() {
        // 1. 创建 workflow
        String workflowJson = "{\"name\":\"test\",\"description\":\"desc\",\"steps\":[]}";
        HttpEntity<String> createReq = new HttpEntity<>(workflowJson, headers);
        ResponseEntity<Map> createResp = restTemplate.postForEntity(baseUrl + "/workflows", createReq, Map.class);
        Assertions.assertEquals(HttpStatus.OK, createResp.getStatusCode());
        Map workflow = createResp.getBody();
        Assertions.assertNotNull(workflow);
        String id = (String) workflow.get("id");

        // 2. 查询所有 workflows
        HttpEntity<Void> getAllReq = new HttpEntity<>(headers);
        ResponseEntity<List> getAllResp = restTemplate.exchange(baseUrl + "/workflows", HttpMethod.GET, getAllReq, List.class);
        Assertions.assertEquals(HttpStatus.OK, getAllResp.getStatusCode());
        Assertions.assertTrue(getAllResp.getBody().size() > 0);

        // 3. 查询单个 workflow
        ResponseEntity<Map> getOneResp = restTemplate.exchange(baseUrl + "/workflows/" + id, HttpMethod.GET, getAllReq, Map.class);
        Assertions.assertEquals(HttpStatus.OK, getOneResp.getStatusCode());
        Assertions.assertEquals("test", getOneResp.getBody().get("name"));

        // 4. 更新 workflow
        String updateJson = "{\"name\":\"updated\",\"description\":\"desc2\",\"steps\":[]}";
        HttpEntity<String> updateReq = new HttpEntity<>(updateJson, headers);
        ResponseEntity<Map> updateResp = restTemplate.exchange(baseUrl + "/workflows/" + id, HttpMethod.PUT, updateReq, Map.class);
        Assertions.assertEquals(HttpStatus.OK, updateResp.getStatusCode());
        Assertions.assertEquals("updated", updateResp.getBody().get("name"));

        // 5. 执行 workflow
        ResponseEntity<String> execResp = restTemplate.exchange(baseUrl + "/workflows/" + id + "/execute", HttpMethod.POST, getAllReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, execResp.getStatusCode());
        Assertions.assertTrue(execResp.getBody().contains("执行"));

        // 6. 删除 workflow
        ResponseEntity<Void> delResp = restTemplate.exchange(baseUrl + "/workflows/" + id, HttpMethod.DELETE, getAllReq, Void.class);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, delResp.getStatusCode());
    }

    @Test
    public void testApiKeyGenerate() {
        ResponseEntity<String> keyResp = restTemplate.getForEntity(baseUrl + "/api-key/generate", String.class);
        Assertions.assertEquals(HttpStatus.OK, keyResp.getStatusCode());
        Assertions.assertNotNull(keyResp.getBody());
        Assertions.assertTrue(keyResp.getBody().length() > 20);
    }
}