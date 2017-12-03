package com.nasrulhazim.app.controllers;

import com.nasrulhazim.app.models.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
public class TaskController {
    private List<Task> tasks;

    public TaskController() {
        tasks = new ArrayList<>();

        tasks.add(new Task("task 1"));
        tasks.add(new Task("task 2"));
        tasks.add(new Task("task 3"));
    }

    @GetMapping("/tasks")
    public List<Task> index() {
        return tasks;
    }

    @GetMapping("/tasks/{id}")
    public Task show(@PathVariable int id) {
        Task result = null;
        for (Task task : tasks) {
            if (id == task.getId()) {
                result = task;
                break;
            }
        }
        return result;
    }

    @PostMapping("/tasks")
    public List<Task> store(@RequestBody Task task) {
        tasks.add(task);
        return tasks;
    }
}
