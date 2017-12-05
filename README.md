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

## Documentation

Install the dependencies:

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.6.1</version>
    <scope>compile</scope>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.6.1</version>
    <scope>compile</scope>
</dependency>
```

Then add `Swagger` Configuration file by adding at `src/main/java/com/nasrulhazim/app/config/SwaggerConfiguration`:

```java
package com.nasrulhazim.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiDoc()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nasrulhazim.app"))
                .paths(PathSelectors.any())
                .build();
    }
}
```

Once you have done setup you may go to the browser and go to `http://localhost:8080/swagger-ui.html/`.

## Security

We going to implement authentication in this section - Basic Authentication and JWT.

### Basic Authentication

Install the dependency - open `pom.xml`, and update the `<dependencies>` element.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
</dependency>
```

Now build and run your application. Try to hit your browser / Postman on `http://localhost:8080/tasks`.

You should received something like:

```json
{
    "timestamp": 1512330753858,
    "status": 401,
    "error": "Unauthorized",
    "message": "Full authentication is required to access this resource",
    "path": "/tasks"
}
```

Yes, that's correct. you get HTTP Status Code 401, which means unauthorized access.

> You may read more details about HTTP Status Code [here](https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html).

Now create an `config/ApplicationConfig.java` and add the following:

```java
package com.nasrulhazim.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ApplicationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BasicAuthenticationPoint basicAuthenticationPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF - this should for an API only
        http.csrf()
                .disable();
        // permit access to landing page
        http.authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .anyRequest()
                .authenticated();
        // Purpose of the BasicAuthenticationEntryPoint class is to set
        // the "WWW-Authenticate" header to the response.
        // So, web browsers will display a dialog to enter usename and password
        // based on basic authentication mechanism(WWW-Authenticate header)
        http.httpBasic()
                .authenticationEntryPoint(basicAuthenticationPoint);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // added username, password, and userole for the in-memory user
        auth.inMemoryAuthentication()
                .withUser("nasrul")
                .password("password")
                .roles("USER");
    }
}
```

Then add `config/BasicAuthenticatoinPoint.java`:

```java
package com.nasrulhazim.app.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BasicAuthenticationPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authEx)
            throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.println("{ \"error\":\"" + authEx.getMessage() + "\",\"code\":\"401\"}");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("Nasrul");
        super.afterPropertiesSet();
    }
}
```

### JWT Authentication

#### Preparation

Create `users` table with following SQL command:

```sql
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'image/default.png',
  `verified` tinyint(1) NOT NULL DEFAULT 0,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_email_unique` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

Create a model for user `models/User.java`:

```java
package com.nasrulhazim.app.models;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "verified")
    private boolean verified;

    @Column(name = "token")
    private String token;

    @Column(name = "remember_token")
    private String remember_token;

    private Date deleted_at;
    private Date created_at;
    private Date updated_at;

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean getVerified() {
        return this.verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRememberToken() {
        return this.remember_token;
    }

    public void setRememberToken(String remember_token) {
        this.remember_token = remember_token;
    }

    public Date getDeletedAt() {
        return this.deleted_at;
    }

    public void setDeletedAt(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public Date getCreatedAt() {
        return this.created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdatedAt() {
        return this.updated_at;
    }

    public void setUpdatedAt(Date updated_at) {
        this.updated_at = updated_at;
    }
}
```

Create a repository for user, `repositories/UserRespository.java`:

```java
package com.nasrulhazim.app.repositories;

import com.nasrulhazim.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
}
```

Create a controller for user, `controllers/UserController.java`:

```java
package com.nasrulhazim.app.controllers;

import com.nasrulhazim.app.models.User;
import com.nasrulhazim.app.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> index() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User show(@PathVariable long id) {
        return userRepository.findOne(id);
    }

    @PostMapping("/users")
    public List<User> store(@RequestBody User user) {
        userRepository.save(user);
        return userRepository.findAll();
    }
}
```

Now create a new user using Postman:

URL: `http://localhost:8080/sign-up`

HTTP Method: `POST`

Headers: `Content-Type` - `application/json`

Content: `raw`

```json
{
	"name":"nasrulhazim",
	"password":"password",
	"email":"nasrulhazim.m@gmail.com"
}
```

#### Implementation

Add dependency:

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.0</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.8.9</version>
</dependency>
```


