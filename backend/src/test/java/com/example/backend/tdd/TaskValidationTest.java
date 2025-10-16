package com.example.backend.tdd;

import com.example.backend.dto.TaskDTO;
import com.example.backend.model.TaskPriority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskValidationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createTask_withEmptyTitle_shouldReturn400() {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("");  // Empty title
        taskDTO.setDescription("Test Description");
        taskDTO.setPriority(TaskPriority.HIGH);

        // Act
        ResponseEntity<Object> response = restTemplate.postForEntity("/api/tasks", taskDTO, Object.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Title cannot be empty"));
    }
}