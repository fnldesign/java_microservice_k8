package com.example.microservice.controller;

import com.example.microservice.model.Todo;
import com.example.microservice.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoController Unit Tests")
class TodoControllerTest {

  @Mock
  private TodoRepository todoRepository;

  @InjectMocks
  private TodoController todoController;

  private Todo testTodo;

  @BeforeEach
  void setUp() {
    testTodo = new Todo();
    testTodo.setId(1L);
    testTodo.setTitle("Test TODO");
    testTodo.setDescription("Test Description");
    testTodo.setCompleted(false);
  }

  @Test
  @DisplayName("Should create TODO successfully")
  void shouldCreateTodo() {
    when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

    ResponseEntity<Todo> response = todoController.createTodo(testTodo);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Test TODO", response.getBody().getTitle());
    verify(todoRepository, times(1)).save(any(Todo.class));
  }

  @Test
  @DisplayName("Should get all TODOs")
  void shouldGetAllTodos() {
    Todo todo2 = new Todo();
    todo2.setId(2L);
    todo2.setTitle("Second TODO");

    when(todoRepository.findAll()).thenReturn(Arrays.asList(testTodo, todo2));

    ResponseEntity<List<Todo>> response = todoController.getAllTodos(null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().size());
    verify(todoRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Should get TODOs filtered by completed status")
  void shouldGetTodosFilteredByStatus() {
    when(todoRepository.findByCompleted(true)).thenReturn(Arrays.asList(testTodo));

    ResponseEntity<List<Todo>> response = todoController.getAllTodos(true);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    verify(todoRepository, times(1)).findByCompleted(true);
    verify(todoRepository, never()).findAll();
  }

  @Test
  @DisplayName("Should get TODO by ID")
  void shouldGetTodoById() {
    when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));

    ResponseEntity<Todo> response = todoController.getTodoById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Test TODO", response.getBody().getTitle());
    verify(todoRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should return 404 when TODO not found by ID")
  void shouldReturn404WhenTodoNotFound() {
    when(todoRepository.findById(999L)).thenReturn(Optional.empty());

    ResponseEntity<Todo> response = todoController.getTodoById(999L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(todoRepository, times(1)).findById(999L);
  }

  @Test
  @DisplayName("Should update TODO successfully")
  void shouldUpdateTodo() {
    Todo updatedTodo = new Todo();
    updatedTodo.setTitle("Updated Title");
    updatedTodo.setDescription("Updated Description");
    updatedTodo.setCompleted(true);

    when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));
    when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

    ResponseEntity<Todo> response = todoController.updateTodo(1L, updatedTodo);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(todoRepository, times(1)).findById(1L);
    verify(todoRepository, times(1)).save(any(Todo.class));
  }

  @Test
  @DisplayName("Should return 404 when updating non-existent TODO")
  void shouldReturn404WhenUpdatingNonExistentTodo() {
    when(todoRepository.findById(999L)).thenReturn(Optional.empty());

    ResponseEntity<Todo> response = todoController.updateTodo(999L, testTodo);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(todoRepository, times(1)).findById(999L);
    verify(todoRepository, never()).save(any(Todo.class));
  }

  @Test
  @DisplayName("Should mark TODO as completed")
  void shouldMarkTodoAsCompleted() {
    when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));
    when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

    ResponseEntity<Todo> response = todoController.completeTodo(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getCompleted());
    verify(todoRepository, times(1)).findById(1L);
    verify(todoRepository, times(1)).save(any(Todo.class));
  }

  @Test
  @DisplayName("Should delete TODO successfully")
  void shouldDeleteTodo() {
    when(todoRepository.existsById(1L)).thenReturn(true);
    doNothing().when(todoRepository).deleteById(1L);

    ResponseEntity<Void> response = todoController.deleteTodo(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(todoRepository, times(1)).existsById(1L);
    verify(todoRepository, times(1)).deleteById(1L);
  }

  @Test
  @DisplayName("Should return 404 when deleting non-existent TODO")
  void shouldReturn404WhenDeletingNonExistentTodo() {
    when(todoRepository.existsById(999L)).thenReturn(false);

    ResponseEntity<Void> response = todoController.deleteTodo(999L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(todoRepository, times(1)).existsById(999L);
    verify(todoRepository, never()).deleteById(anyLong());
  }
}
