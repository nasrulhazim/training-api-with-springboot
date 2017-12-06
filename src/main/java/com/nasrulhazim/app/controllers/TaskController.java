package com.nasrulhazim.app.controllers;

import com.nasrulhazim.app.models.Task;
import com.nasrulhazim.app.repositories.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    private TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/tasks")
    public List<Task> index() {
        return taskRepository.findAll();
    }

    @GetMapping("/tasks/{id}")
    public Task show(@PathVariable long id) {
        return taskRepository.findOne(id);
    }

    @PostMapping("/tasks")
    public List<Task> store(@RequestBody Task task) {
        taskRepository.save(task);
        return taskRepository.findAll();
    }

    @PutMapping("/Tasks/{id}")
    public Task update(@RequestBody Task task) {
        // taskRepository... update..
        return task;
    }

}
