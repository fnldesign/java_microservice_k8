package com.example.microservice.controller;

import com.example.microservice.model.Todo;
import com.example.microservice.repository.TodoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("TodoController Integration Tests")
class TodoControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TodoRepository todoRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Value("${api.key}")
  private String API_KEY;

  @BeforeEach
  void setUp() {
    todoRepository.deleteAll();
  }

  @Test
  @DisplayName("Should create a new TODO")
  void shouldCreateTodo() throws Exception {
    Todo todo = new Todo();
    todo.setTitle("Test TODO");
    todo.setDescription("Test Description");
    todo.setCompleted(false);

    mockMvc.perform(post("/api/todos")
        .header("X-API-Key", API_KEY)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(todo)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.title").value("Test TODO"))
        .andExpect(jsonPath("$.description").value("Test Description"))
        .andExpect(jsonPath("$.completed").value(false))
        .andExpect(jsonPath("$.createdAt").exists())
        .andExpect(jsonPath("$.updatedAt").exists());
  }

  @Test
  @DisplayName("Should fail to create TODO without authentication")
  void shouldFailToCreateTodoWithoutAuth() throws Exception {
    Todo todo = new Todo();
    todo.setTitle("Test TODO");
    todo.setDescription("Test Description");

    mockMvc.perform(post("/api/todos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(todo)))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Should get all TODOs")
  void shouldGetAllTodos() throws Exception {
    // Create test data
    Todo todo1 = new Todo();
    todo1.setTitle("TODO 1");
    todo1.setDescription("Description 1");
    todo1.setCompleted(false);
    todoRepository.save(todo1);

    Todo todo2 = new Todo();
    todo2.setTitle("TODO 2");
    todo2.setDescription("Description 2");
    todo2.setCompleted(true);
    todoRepository.save(todo2);

    mockMvc.perform(get("/api/todos")
        .header("X-API-Key", API_KEY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title").value("TODO 1"))
        .andExpect(jsonPath("$[1].title").value("TODO 2"));
  }

  @Test
  @DisplayName("Should get TODOs by completed status")
  void shouldGetTodosByCompletedStatus() throws Exception {
    // Create test data
    Todo todo1 = new Todo();
    todo1.setTitle("Completed TODO");
    todo1.setCompleted(true);
    todoRepository.save(todo1);

    Todo todo2 = new Todo();
    todo2.setTitle("Incomplete TODO");
    todo2.setCompleted(false);
    todoRepository.save(todo2);

    mockMvc.perform(get("/api/todos?completed=true")
        .header("X-API-Key", API_KEY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].title").value("Completed TODO"))
        .andExpect(jsonPath("$[0].completed").value(true));
  }

  @Test
  @DisplayName("Should get TODO by ID")
  void shouldGetTodoById() throws Exception {
    Todo todo = new Todo();
    todo.setTitle("Test TODO");
    todo.setDescription("Test Description");
    todo.setCompleted(false);
    Todo saved = todoRepository.save(todo);

    mockMvc.perform(get("/api/todos/" + saved.getId())
        .header("X-API-Key", API_KEY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(saved.getId()))
        .andExpect(jsonPath("$.title").value("Test TODO"));
  }

  @Test
  @DisplayName("Should return 404 for non-existent TODO")
  void shouldReturn404ForNonExistentTodo() throws Exception {
    mockMvc.perform(get("/api/todos/999")
        .header("X-API-Key", API_KEY))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Should update TODO")
  void shouldUpdateTodo() throws Exception {
    Todo todo = new Todo();
    todo.setTitle("Original Title");
    todo.setDescription("Original Description");
    todo.setCompleted(false);
    Todo saved = todoRepository.save(todo);

    Todo updated = new Todo();
    updated.setTitle("Updated Title");
    updated.setDescription("Updated Description");
    updated.setCompleted(true);

    mockMvc.perform(put("/api/todos/" + saved.getId())
        .header("X-API-Key", API_KEY)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Title"))
        .andExpect(jsonPath("$.description").value("Updated Description"))
        .andExpect(jsonPath("$.completed").value(true));
  }

  @Test
  @DisplayName("Should mark TODO as completed")
  void shouldMarkTodoAsCompleted() throws Exception {
    Todo todo = new Todo();
    todo.setTitle("TODO to Complete");
    todo.setCompleted(false);
    Todo saved = todoRepository.save(todo);

    mockMvc.perform(patch("/api/todos/" + saved.getId() + "/complete")
        .header("X-API-Key", API_KEY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.completed").value(true));
  }

  @Test
  @DisplayName("Should delete TODO")
  void shouldDeleteTodo() throws Exception {
    Todo todo = new Todo();
    todo.setTitle("TODO to Delete");
    Todo saved = todoRepository.save(todo);

    mockMvc.perform(delete("/api/todos/" + saved.getId())
        .header("X-API-Key", API_KEY))
        .andExpect(status().isNoContent());

    // Verify deletion
    mockMvc.perform(get("/api/todos/" + saved.getId())
        .header("X-API-Key", API_KEY))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Should return empty list when no TODOs exist")
  void shouldReturnEmptyListWhenNoTodos() throws Exception {
    mockMvc.perform(get("/api/todos")
        .header("X-API-Key", API_KEY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }
}
