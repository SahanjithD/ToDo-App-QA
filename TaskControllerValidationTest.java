@WebMvcTest(TaskController.class)
public class TaskControllerValidationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void createTask_emptyTitle_shouldReturn400WithMessage() throws Exception {
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"\",\"description\":\"test\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Title is required"));
    }
}