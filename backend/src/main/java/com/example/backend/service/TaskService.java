package com.example.backend.service;

import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
    
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }
    
    public Task saveTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }
    
    public Optional<Task> completeTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setCompleted(true);
            task.setCompletedAt(LocalDateTime.now());
            return Optional.of(taskRepository.save(task));
        }
        return Optional.empty();
    }
    
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
