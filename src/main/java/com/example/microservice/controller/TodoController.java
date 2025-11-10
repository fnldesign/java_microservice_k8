package com.example.microservice.controller;

import com.example.microservice.model.Todo;
import com.example.microservice.repository.TodoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todos", description = "TODO items management API")
@SecurityRequirement(name = "apiKey")
public class TodoController {

  private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

  @Autowired
  private TodoRepository todoRepository;

  @GetMapping
  @Operation(summary = "Get all todos", description = "Returns a list of all TODO items")
  public ResponseEntity<List<Todo>> getAllTodos(
      @Parameter(description = "Filter by completion status") @RequestParam(required = false) Boolean completed) {
    logger.info("Fetching all todos, completed filter: {}", completed);
    List<Todo> todos = completed != null ? todoRepository.findByCompleted(completed) : todoRepository.findAll();
    return ResponseEntity.ok(todos);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get todo by ID", description = "Returns a single TODO item by its ID")
  public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
    logger.info("Fetching todo with id: {}", id);
    Optional<Todo> todo = todoRepository.findById(id);
    return todo.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create a new todo", description = "Creates a new TODO item")
  public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
    logger.info("Creating new todo: {}", todo.getTitle());
    Todo savedTodo = todoRepository.save(todo);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedTodo);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a todo", description = "Updates an existing TODO item")
  public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
    logger.info("Updating todo with id: {}", id);
    Optional<Todo> optionalTodo = todoRepository.findById(id);

    if (optionalTodo.isPresent()) {
      Todo todo = optionalTodo.get();
      todo.setTitle(todoDetails.getTitle());
      todo.setDescription(todoDetails.getDescription());
      todo.setCompleted(todoDetails.getCompleted());
      Todo updatedTodo = todoRepository.save(todo);
      return ResponseEntity.ok(updatedTodo);
    }

    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a todo", description = "Deletes a TODO item by its ID")
  public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
    logger.info("Deleting todo with id: {}", id);
    if (todoRepository.existsById(id)) {
      todoRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @PatchMapping("/{id}/complete")
  @Operation(summary = "Mark todo as completed", description = "Marks a TODO item as completed")
  public ResponseEntity<Todo> completeTodo(@PathVariable Long id) {
    logger.info("Marking todo {} as completed", id);
    Optional<Todo> optionalTodo = todoRepository.findById(id);

    if (optionalTodo.isPresent()) {
      Todo todo = optionalTodo.get();
      todo.setCompleted(true);
      Todo updatedTodo = todoRepository.save(todo);
      return ResponseEntity.ok(updatedTodo);
    }

    return ResponseEntity.notFound().build();
  }
}
