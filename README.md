# Training: API with Spring Boot


At the first of this training, audience will learn using Spring Boot CLI. 

Then audience may use Intellij IDEA to develop Spring Boot applications.

## Hello World

Create new project
	
```
$ spring init --groupId=com.nasrulhazim --artifactId=app --name=app --dependencies=web dir
```

Compile project

```
$ mvn clean install
```

Test the project	

```
$ java -jar target/app-0.0.1-SNAPSHOT.jar
```

Create a controller in `src/main/java/comp/nasrulhazim/app/controllers` called `HelloWorldController.java`:

```java
package com.nasrulhazim.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public Hello helloWorld() {
        return 'Hello World';
    }
}

```

Now compile and run your application. 
Then hit your browser at `http://localhost:8080`. 
You should see `Hello World` in your browser.

Now, as an API, we usually return as JSON or XML. In our case, we use JSON.

Here how we do in Spring Boot.

First, create an entity / model of a `Hello` class in `src/main/java/com/nasrulhazim/app/models`:

```java
package com.nasrulhazim.app.models;

public class Hello {
	private String name;
	public Hello(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String sayHello() {
		return "Hello: " + this.name;
	}
}
```

Then update your `HelloWorldController.java`, to import the above entity and replace the return with new `Hello` class object.

```java
package com.nasrulhazim.app.controllers;

import com.nasrulhazim.app.models.Hello;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public Hello helloWorld() {
        return new Hello("Nasrul");
    }
}
```

Now hit again your browser, you should see `Hello World` in `JSON` format right now.


## Create, Read, Update and Delete

Now you already know how to create a most simplest API with Spring Boot. 
Next, we going to learn about CRUD operation - with and without using database interaction.
We going to start with without using database interaction.

### Without Database

Create a `Task` model and `TaskController`

A simple model to store information about the task.

```java
package com.nasrulhazim.app.models;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Task implements Serializable {

	private static final AtomicInteger sequence = new AtomicInteger();

	private int id;

	private String name;

	private boolean is_done;

	public Task(String name) {
		this.id = sequence.incrementAndGet();
		this.name = name;
		this.is_done = false;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsDone() {
		return this.is_done;
	}
	
	public void setIsDone(boolean is_done) {
		this.is_done = is_done;
	}
}
```  

A simple controller to have list of tasks, show particular task, update task and delete task.

```java
package com.nasrulhazim.app.controllers;

import com.nasrulhazim.app.models.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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
```

Now build, run and hit again your browser, but this time the URL is `http://localhost:8080/tasks`.

You should see list of tasks, in JSON format.

Now go to `http://localhost:8080/tasks/1`. You should see task with `id` is `1`.

For the Update and Delete operation, I leave on purposed, as for assignment.

### With Database

Add dependencies: `jpa`, `mysql`

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
```

Configure application by open up `src/main/resources/application.properties`:


```
#spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/springboot
spring.datasource.username=root
spring.datasource.password=
spring.datasource.tomcat.max-wait=20000
spring.datasource.tomcat.max-active=50
spring.datasource.tomcat.max-idle=20
spring.datasource.tomcat.min-idle=15

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.id.new_generator_mappings=false
spring.jpa.properties.hibernate.format_sql=true

logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

#Security Configuration---
#security.user.password= concretepage
#prints default password---
#logging.level.org.springframework.boot.autoconfigure.security= INFO 
```

Next, open up MySQL editor - either by using PHPMyAdmin, Sequel Pro, depends on your preference.

Create a new database called `springboot`.

Then copy past the following SQL statements to create `tasks` table and seed some data:

```sql
CREATE TABLE `tasks` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `is_done` tinyint(1) DEFAULT 0,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tasks` (`id`, `name`, `is_done`, `created_at`, `updated_at`) VALUES
(1,'task a',0,'2017-12-01 17:07:19',NULL),
(2,'task b',1,'2017-12-01 17:07:29','2017-12-01 17:07:29');
```

#### Entity

> We going to map the entity / model to the table in our database.

```java
package com.nasrulhazim.app.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private boolean is_done;

	public Task() {
		this.is_done = false;
	}

	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsDone() {
		return this.is_done;
	}
	
	public void setIsDone(boolean is_done) {
		this.is_done = is_done;
	}
}
```

#### Repository

> JPA Repositories. The Java Persistence API (JPA) is the standard way of persisting Java objects into relational databases. 

The JPA consists of two parts: 

1. A mapping subsystem to map classes onto relational tables 
2. An EntityManager API to access the objects, define and execute queries, and more.

```java
package com.nasrulhazim.app.repositories;

import com.nasrulhazim.app.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> { 
    
}
```

#### Controller

> Use the repository to interact with out database.

```java
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
}
```

#### Seeder (optional)

```java
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
        // do insert manually...
    }
}
```


### TODO

- [ ] auth (basic)
- [ ] auth (jwt)
