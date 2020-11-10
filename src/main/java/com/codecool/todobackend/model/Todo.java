package com.codecool.todobackend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Todo {

    @Id
    @GeneratedValue
    private long Id;

    @ManyToOne
    private UserData userData;

    private String todo;

    private TodoStatus todoStatus;
}
