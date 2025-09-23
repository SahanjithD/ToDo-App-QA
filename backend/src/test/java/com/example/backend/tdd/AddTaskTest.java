package com.example.backend.tdd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddTaskTest {
    @Autowired TestRestTemplate restTemplate;

    @Test
    void createTask_shouldReturn201_andPersistedFields() {
        Map<String,Object> payload = Map.of(
                "title","Write tests",
                "description","Cover core TDD",
                "priority","HIGH"
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> res = restTemplate.postForEntity("/api/tasks", new HttpEntity<>(payload, headers), Map.class);
        assertEquals(201, res.getStatusCodeValue());
        assertNotNull(res.getBody().get("id"));
        assertEquals("Write tests", res.getBody().get("title"));
        assertEquals("HIGH", res.getBody().get("priority"));
    }
}