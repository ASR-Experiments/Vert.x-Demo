package com.asr.example.vert.x.demo.repository;

import com.asr.example.vert.x.demo.domain.UserEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

  public CompletionStage<Void> save(UserEntity user) {
    return sessionFactory.withTransaction(
      (session, tx) -> session.persist(user)
    );
  }

  public CompletionStage<Integer> delete(UserEntity user) {
    CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
    CriteriaDelete<UserEntity> criteriaDelete = criteriaBuilder.createCriteriaDelete(UserEntity.class);
    Root<UserEntity> root = criteriaDelete.from(UserEntity.class);
    Predicate id = criteriaBuilder.equal(root.get("id"), user.getId());
    criteriaDelete.where(id);

    return sessionFactory.withTransaction(
      (session, tx) -> session.createQuery(criteriaDelete).executeUpdate()
    );
  }

  public CompletionStage<UserEntity> update(UserEntity user) {
    return sessionFactory.withTransaction(
      (session, tx) -> session.merge(user)
    );
  }
}
