package com.example.codecool.service;


import com.example.codecool.exception.ToDoNotFoundException;
import com.example.codecool.model.Status;
import com.example.codecool.model.ToDo;
import com.example.codecool.repository.ToDoRepository;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;

    @Transactional(readOnly = true)
    public String getAllToDoByStatus(String status) throws JSONException {
        if (status.equals("active")) {
            List<ToDo> activeTodo = toDoRepository.findAllByStatus(Status.ACTIVE.name());
            return getString(activeTodo);
        } else if (status.equals("complete")) {
            List<ToDo> completeTodo = toDoRepository.findAllByStatus(Status.COMPLETE.name());
            return getString(completeTodo);
        } else {
            List<ToDo> allTodo = toDoRepository.findAll();
            return getString(allTodo);
        }
    }

    private String getString(List<ToDo> activeTodo) throws JSONException {
        JSONArray arr = new JSONArray();
        for (ToDo dao : activeTodo) {
            JSONObject jo = new JSONObject();
            jo.put("id", dao.getId());
            jo.put("title", dao.getTitle());
            jo.put("completed", dao.isCompleted());
            arr.put(jo);
        }
        return arr.toString(2);
    }
    @Transactional(readOnly = true)
    public String getToDoById(Long id) {
        ToDo toDo = toDoRepository.findById(id).orElseThrow(() -> new ToDoNotFoundException(String.format("No ToDo Found with id %s", id)));
        return toDo.getTitle();
    }

    @Transactional
    public void addNewToDo(String todoTitle) {
        ToDo newTodo = new ToDo();
        newTodo.setTitle(todoTitle);
        newTodo.setStatus(Status.ACTIVE);
        toDoRepository.save(newTodo);
    }

    @Transactional
    public int removeToDo(Long id) {
        return toDoRepository.deleteToDoById(id);
    }
    @Transactional
    public int deleteAllByStatusCompleted() {
        return toDoRepository.deleteAllByStatus();
    }

    @Transactional
    public void toggleAll(boolean complete) {
        List<ToDo> all = toDoRepository.findAll();
        for (ToDo toDo : all) {
            if (complete) {
                toDo.setStatus(Status.COMPLETE);
            } else {
                toDo.setStatus(Status.ACTIVE);
            }
            toDoRepository.save(toDo);
        }
    }

    @Transactional
    public void updateToDo(Long id, String todoTitle) {
        ToDo toDoById = toDoRepository.findById(id).orElseThrow(() -> new ToDoNotFoundException(String.format("No ToDo Found with id %s", id)));
        toDoById.setTitle(todoTitle);
        toDoRepository.save(toDoById);
    }

    @Transactional
    public void updateCompleted(Long id, boolean isComplete) {
        ToDo toDoById = toDoRepository.findById(id).orElseThrow(() -> new ToDoNotFoundException(String.format("No ToDo Found with id %s", id)));
        if (isComplete) {
            toDoById.setStatus(Status.COMPLETE);
        } else {
            toDoById.setStatus(Status.ACTIVE);
        }
        toDoRepository.save(toDoById);
    }

    public List<ToDo> getAllTodos() {
        return toDoRepository.findAll();
    }
}
