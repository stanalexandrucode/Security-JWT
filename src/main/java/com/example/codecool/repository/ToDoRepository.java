package com.example.codecool.repository;


import com.example.codecool.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    @Query(
            value = "SELECT * FROM to_do WHERE status=?1",
            nativeQuery = true)
    List<ToDo> findAllByStatus(String status);

    @Query("SELECT t FROM ToDo t WHERE t.id=?1")
    Optional<ToDo> findById(Long id);

    @Query(
            value = "SELECT * FROM to_do",
            nativeQuery = true)
    List<ToDo> findAll();

    @Transactional
    @Modifying
    @Query("DELETE  FROM ToDo t WHERE t.id=?1")
    int deleteToDoById(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM ToDo t WHERE t.status='COMPLETE'")
    int deleteAllByStatus();

}
