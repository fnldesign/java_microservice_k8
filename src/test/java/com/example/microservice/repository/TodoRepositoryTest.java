package com.example.microservice.repository;

import com.example.microservice.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("TodoRepository Tests")
class TodoRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private TodoRepository todoRepository;

  @BeforeEach
  void setUp() {
    todoRepository.deleteAll();
  }

  @Test
  @DisplayName("Should save and find TODO by ID")
  void shouldSaveAndFindTodoById() {
    Todo todo = new Todo();
    todo.setTitle("Test TODO");
    todo.setDescription("Test Description");
    todo.setCompleted(false);

    Todo saved = todoRepository.save(todo);
    entityManager.flush();

    Optional<Todo> found = todoRepository.findById(saved.getId());

    assertTrue(found.isPresent());
    assertEquals("Test TODO", found.get().getTitle());
    assertEquals("Test Description", found.get().getDescription());
    assertFalse(found.get().getCompleted());
  }

  @Test
  @DisplayName("Should find TODOs by completed status")
  void shouldFindTodosByCompletedStatus() {
    Todo todo1 = new Todo();
    todo1.setTitle("Completed TODO");
    todo1.setCompleted(true);
    todoRepository.save(todo1);

    Todo todo2 = new Todo();
    todo2.setTitle("Incomplete TODO");
    todo2.setCompleted(false);
    todoRepository.save(todo2);

    Todo todo3 = new Todo();
    todo3.setTitle("Another Completed TODO");
    todo3.setCompleted(true);
    todoRepository.save(todo3);

    entityManager.flush();

    List<Todo> completedTodos = todoRepository.findByCompleted(true);
    List<Todo> incompleteTodos = todoRepository.findByCompleted(false);

    assertEquals(2, completedTodos.size());
    assertEquals(1, incompleteTodos.size());
    assertTrue(completedTodos.stream().allMatch(Todo::getCompleted));
    assertFalse(incompleteTodos.stream().anyMatch(Todo::getCompleted));
  }

  @Test
  @DisplayName("Should find all TODOs")
  void shouldFindAllTodos() {
    Todo todo1 = new Todo();
    todo1.setTitle("TODO 1");
    todoRepository.save(todo1);

    Todo todo2 = new Todo();
    todo2.setTitle("TODO 2");
    todoRepository.save(todo2);

    entityManager.flush();

    List<Todo> allTodos = todoRepository.findAll();

    assertEquals(2, allTodos.size());
  }

  @Test
  @DisplayName("Should update TODO")
  void shouldUpdateTodo() {
    Todo todo = new Todo();
    todo.setTitle("Original Title");
    todo.setCompleted(false);
    Todo saved = todoRepository.save(todo);
    entityManager.flush();

    saved.setTitle("Updated Title");
    saved.setCompleted(true);
    todoRepository.save(saved);
    entityManager.flush();

    Optional<Todo> updated = todoRepository.findById(saved.getId());

    assertTrue(updated.isPresent());
    assertEquals("Updated Title", updated.get().getTitle());
    assertTrue(updated.get().getCompleted());
  }

  @Test
  @DisplayName("Should delete TODO")
  void shouldDeleteTodo() {
    Todo todo = new Todo();
    todo.setTitle("TODO to Delete");
    Todo saved = todoRepository.save(todo);
    entityManager.flush();

    todoRepository.deleteById(saved.getId());
    entityManager.flush();

    Optional<Todo> deleted = todoRepository.findById(saved.getId());

    assertFalse(deleted.isPresent());
  }

  @Test
  @DisplayName("Should check if TODO exists by ID")
  void shouldCheckIfTodoExistsById() {
    Todo todo = new Todo();
    todo.setTitle("Test TODO");
    Todo saved = todoRepository.save(todo);
    entityManager.flush();

    assertTrue(todoRepository.existsById(saved.getId()));
    assertFalse(todoRepository.existsById(999L));
  }

  @Test
  @DisplayName("Should count TODOs")
  void shouldCountTodos() {
    Todo todo1 = new Todo();
    todo1.setTitle("TODO 1");
    todoRepository.save(todo1);

    Todo todo2 = new Todo();
    todo2.setTitle("TODO 2");
    todoRepository.save(todo2);

    entityManager.flush();

    long count = todoRepository.count();

    assertEquals(2, count);
  }

  @Test
  @DisplayName("Should return empty when TODO not found")
  void shouldReturnEmptyWhenTodoNotFound() {
    Optional<Todo> notFound = todoRepository.findById(999L);

    assertFalse(notFound.isPresent());
  }
}
