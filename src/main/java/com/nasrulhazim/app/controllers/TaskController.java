package com.nasrulhazim.app.controllers;

import com.nasrulhazim.app.models.Task;
import com.nasrulhazim.app.repositories.TaskRepository;
import com.nasrulhazim.app.transformers.ApiResponse;
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

    @PutMapping("/tasks/{id}")
    public Task update(@PathVariable long id, @RequestBody Task request) {
        Task task = taskRepository.findOne(id);
        task.setIsDone(request.getIsDone());
        task.setName(request.getName());
        taskRepository.save(task);
        return task;
    }

    @DeleteMapping("/tasks/{id}")
    public ApiResponse delete(@PathVariable long id) {
        // Delete by Task Object
        // Task task = taskRepository.findOne(id);
        // taskRepository.delete(task);

        // Delete by id, type of long
        taskRepository.delete(id);

        return new ApiResponse("Task removed", "SUCCESS");
    }


}
