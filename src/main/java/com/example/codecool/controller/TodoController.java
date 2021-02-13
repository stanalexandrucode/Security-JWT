package com.example.codecool.controller;


import com.example.codecool.model.ToDo;
import com.example.codecool.service.ToDoService;
import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @hasRole('ROLE_) @hasAnyRole('ROLE_) @hasAuthority('permission') @hasAnyAuthority('permission')
 */


@AllArgsConstructor
@RestController
@RequestMapping()
public class TodoController {

    private final ToDoService toDoService;

    /**
     * Add an item and display it in the list.
     */
    @PostMapping("/addTodo")
    public void addToDo(@RequestParam("todo-title") String todoTitle) {
        toDoService.addNewToDo(todoTitle);
    }

    /**
     * Save an item in edit.
     */
    @PutMapping("/todos/{id}")
    public void updateToDoById(@PathVariable Long id, @RequestParam("todo-title") String todoTitle) {
        toDoService.updateToDo(id, todoTitle);
    }

    /**
     * Cancel the item editing mode.
     */
    @GetMapping("todos/{id}")
    public String cancelEdit(@PathVariable Long id) {
        return toDoService.getToDoById(id);
    }

    /**
     * Remove the data and elements related to an Item.
     */
    @DeleteMapping("/todos/{id}")
    public void removeToDoById(@PathVariable Long id) {
        toDoService.removeToDo(id);
    }

    /**
     * Remove all completed items.
     */
    @DeleteMapping("/todos/completed")
    public int RemoveAllCompleted() {
        return toDoService.deleteAllByStatusCompleted();
    }


    /**
     * Update an item in based on the state of completed.
     */
    @PutMapping("todos/{id}/toggle_status")
    public void updateItemCompleted(@PathVariable Long id, @RequestParam("status") boolean status) {
        toDoService.updateCompleted(id, status);

    }

    // Toggle all status
    @PutMapping("/todos/toggle_all")
    public void toggleAllStatus(@RequestParam("toggle-all") boolean toggleAll) {
        toDoService.toggleAll(toggleAll);
    }

    /**
     * Refresh the list based on the current route.
     */
    @PostMapping("/list")
    private String listToDosByStatus(@RequestParam("status") String status) throws JSONException {
        return toDoService.getAllToDoByStatus(status);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<ToDo>> getAllTodos(){
        List<ToDo> toDos = toDoService.getAllTodos();
        return new ResponseEntity<>(toDos, HttpStatus.OK);
    }


}
