package com.asr.example.vert.x.demo.repository;

import com.asr.example.vert.x.demo.domain.UserEntity;
import org.hibernate.reactive.stage.Stage;

import java.util.concurrent.CompletionStage;

public class UserRepository {
  private final Stage.SessionFactory sessionFactory;

  public UserRepository(Stage.SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public CompletionStage<UserEntity> findById(Long id) {
    return sessionFactory.withTransaction(
      (session, tx) -> session.find(UserEntity.class, id)
    );
  }
}
