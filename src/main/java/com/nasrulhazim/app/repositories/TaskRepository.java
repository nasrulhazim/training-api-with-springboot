package com.nasrulhazim.app.repositories;

import com.nasrulhazim.app.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
}
