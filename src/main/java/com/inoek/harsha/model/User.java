package com.inoek.harsha.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "User_Sequence")
    @SequenceGenerator(name = "User_Sequence", sequenceName = "User_Sequence", allocationSize = 1)
    private Long id;

    private String name;

    public User(String name) {
        this.name = name + ", thanks so much for clicking the button! You really do love me!";
    }


}
