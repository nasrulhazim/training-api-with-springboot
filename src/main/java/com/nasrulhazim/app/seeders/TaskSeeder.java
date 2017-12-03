package com.nasrulhazim.app.seeders;

import com.nasrulhazim.app.repositories.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

@Component
public class TaskSeeder implements CommandLineRunner {
    private TaskRepository taskRepository;

    public TaskSeeder(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Logger.getLogger("com.nasrulhazim.app.seeders.TaskSeeder").info("seeding default tasks...");
    }
}
