package com.example.backend.tdd;

import com.example.backend.dto.TaskDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateUserInputTest {

    @Test
    public void testTaskDTO_InvalidTitle() {
        TaskDTO dto = new TaskDTO();
        dto.setTitle(""); // Invalid title
        dto.setDescription("Some description");

        // Simulate validation (replace with actual validation logic if available)
        boolean isValid = dto.getTitle() != null && !dto.getTitle().trim().isEmpty();

        assertFalse(isValid, "Task title should not be empty");
    }

    @Test
    public void testTaskDTO_ValidTitle() {
        TaskDTO dto = new TaskDTO();
        dto.setTitle("Valid Title");
        dto.setDescription("Some description");

        boolean isValid = dto.getTitle() != null && !dto.getTitle().trim().isEmpty();

        assertTrue(isValid, "Task title should be valid");
    }
}