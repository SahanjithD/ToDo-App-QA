package com.example.backend.controller;

import com.example.backend.dto.TaskDTO;
import com.example.backend.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerValidationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    // Mock the service the controller depends on so this remains a pure controller test
    @MockBean
    private TaskService taskService;

    @Test
    void whenTitleEmpty_thenReturns400_withFriendlyMessage() throws Exception {
        TaskDTO dto = new TaskDTO();
        dto.setTitle("");
        dto.setDescription("");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[?(@.field=='title')].message").exists());
    }

    @Test
    void whenTitleTooLong_thenReturns400_withFriendlyMessage() throws Exception {
        TaskDTO dto = new TaskDTO();
        dto.setTitle("A".repeat(200));
        dto.setDescription("desc");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[?(@.field=='title')].message").exists());
    }
}
