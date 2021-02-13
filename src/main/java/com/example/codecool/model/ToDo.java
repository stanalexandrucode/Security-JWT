package com.example.codecool.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity(name = "ToDo")
@Table(name = "to_do")
public class ToDo {

    @Id
    @SequenceGenerator(
            name = "toDo_sequence",
            sequenceName = "toDo_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "toDo_sequence"
    )
    private Long id;
    @NotBlank(message = "Post Name cannot be empty or Null")
    private String title;
    @Enumerated(EnumType.STRING)
    private Status status;


    public ToDo(String title, Status status) {
        this.title = title;
        this.status = status;
    }

    public boolean isCompleted() {
        return this.status == Status.COMPLETE;
    }


}
