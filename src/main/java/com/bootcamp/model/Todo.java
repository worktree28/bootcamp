package com.bootcamp.model;

import lombok.Data;

import jakarta.persistence.*;
@Data
@Entity
@Table(name = "todos")
public class Todo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_name", nullable = false)
  private String userName;

  @Column(name = "message", nullable = false)
  private String message;

  @Column(name = "user_id", nullable = false)
  private Integer userId;
}