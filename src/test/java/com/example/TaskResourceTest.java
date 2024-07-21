package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TaskResourceTest {


    @BeforeEach
    @Transactional
    public void cleanDatabase() {
        Task.deleteAll();
    }

    @Test
    public void testGetAllTasksEndpoint() {
        cleanDatabase();

        given()
                .when().get("/tasks")
                .then()
                .statusCode(200)
                .body("$.size()", is(0));
    }

    @Test
    public void testCreateTaskEndpoint() {
        Task task = new Task();
        task.title = "New Task";
        task.description = "Task description";
        task.creationDate = "2023-07-20";
        task.dueDate = "2023-07-21";
        task.status = "Pendente";

        given()
                .contentType(ContentType.JSON)
                .body(task)
                .when().post("/tasks")
                .then()
                .statusCode(201)
                .body("title", is("New Task"))
                .body("description", is("Task description"))
                .body("status", is("Pendente"));
    }

    @Test
    public void testUpdateTaskEndpoint() {
        Task task = new Task();
        task.title = "Task to be updated";
        task.description = "Task description";
        task.creationDate = "2023-07-20";
        task.dueDate = "2023-07-21";
        task.status = "Pendente";

        // Primeiro cria a tarefa
        Task createdTask = given()
                .contentType(ContentType.JSON)
                .body(task)
                .when().post("/tasks")
                .then()
                .statusCode(201)
                .extract().as(Task.class);

        createdTask.title = "Updated Task Title";

        // Em seguida, atualiza a tarefa
        given()
                .contentType(ContentType.JSON)
                .body(createdTask)
                .when().put("/tasks/" + createdTask.id)
                .then()
                .statusCode(200)
                .body("title", is("Updated Task Title"));
    }

    @Test
    public void testDeleteTaskEndpoint() {
        Task task = new Task();
        task.title = "Task to be deleted";
        task.description = "Task description";
        task.creationDate = "2023-07-20";
        task.dueDate = "2023-07-21";
        task.status = "Pendente";

        // Primeiro cria a tarefa
        Task createdTask = given()
                .contentType(ContentType.JSON)
                .body(task)
                .when().post("/tasks")
                .then()
                .statusCode(201)
                .extract().as(Task.class);

        // Em seguida, exclui a tarefa
        given()
                .when().delete("/tasks/" + createdTask.id)
                .then()
                .statusCode(204);
    }
}
