package com.example.microservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Todo Model Unit Tests")
class TodoTest {

  private Todo todo;

  @BeforeEach
  void setUp() {
    todo = new Todo();
  }

  @Test
  @DisplayName("Should create TODO with default values")
  void shouldCreateTodoWithDefaults() {
    assertNull(todo.getId());
    assertNull(todo.getTitle());
    assertNull(todo.getDescription());
    assertFalse(todo.getCompleted());
    assertNull(todo.getCreatedAt());
    assertNull(todo.getUpdatedAt());
  }

  @Test
  @DisplayName("Should set and get title")
  void shouldSetAndGetTitle() {
    todo.setTitle("Test Title");
    assertEquals("Test Title", todo.getTitle());
  }

  @Test
  @DisplayName("Should set and get description")
  void shouldSetAndGetDescription() {
    todo.setDescription("Test Description");
    assertEquals("Test Description", todo.getDescription());
  }

  @Test
  @DisplayName("Should set and get completed status")
  void shouldSetAndGetCompleted() {
    todo.setCompleted(true);
    assertTrue(todo.getCompleted());

    todo.setCompleted(false);
    assertFalse(todo.getCompleted());
  }

  @Test
  @DisplayName("Should set and get id")
  void shouldSetAndGetId() {
    todo.setId(1L);
    assertEquals(1L, todo.getId());
  }

  @Test
  @DisplayName("Should set and get createdAt")
  void shouldSetAndGetCreatedAt() {
    LocalDateTime now = LocalDateTime.now();
    todo.setCreatedAt(now);
    assertEquals(now, todo.getCreatedAt());
  }

  @Test
  @DisplayName("Should set and get updatedAt")
  void shouldSetAndGetUpdatedAt() {
    LocalDateTime now = LocalDateTime.now();
    todo.setUpdatedAt(now);
    assertEquals(now, todo.getUpdatedAt());
  }

  @Test
  @DisplayName("Should have proper default completed value")
  void shouldHaveProperDefaultCompletedValue() {
    Todo newTodo = new Todo();
    assertNotNull(newTodo.getCompleted());
    assertFalse(newTodo.getCompleted());
  }
}
