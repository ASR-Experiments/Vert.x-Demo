package com.asr.example.vert.x.demo.repository;

import com.asr.example.vert.x.demo.domain.UserEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

public class UserRepository {
  private final Mutiny.SessionFactory sessionFactory;

  public UserRepository(Mutiny.SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public Uni<UserEntity> findById(Long id) {
    return sessionFactory.withTransaction(
      (session, tx) -> session.find(UserEntity.class, id)
    );
  }

  public Uni<Void> save(UserEntity user) {
    return sessionFactory.withTransaction(
      (session, tx) -> session.persist(user)
    );
  }

  public Uni<Integer> delete(UserEntity user) {
    CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
    CriteriaDelete<UserEntity> criteriaDelete = criteriaBuilder.createCriteriaDelete(UserEntity.class);
    Root<UserEntity> root = criteriaDelete.from(UserEntity.class);
    Predicate id = criteriaBuilder.equal(root.get("id"), user.getId());
    criteriaDelete.where(id);

    return sessionFactory.withTransaction(
      (session, tx) -> session.createQuery(criteriaDelete).executeUpdate()
    );
  }

  public Uni<UserEntity> update(UserEntity user) {
    return sessionFactory.withTransaction(
      (session, tx) -> session.merge(user)
    );
  }

  public Uni<List<UserEntity>> findAll(final int offset, final int limit) {
    return sessionFactory.withTransaction(
      (session, tx) -> {
        Mutiny.SelectionQuery<UserEntity> query = session.createQuery("SELECT u FROM UserEntity u", UserEntity.class);
        query.setFirstResult(Math.max(offset, 0));
        query.setMaxResults(limit < 1 ? 10 : limit);
        return query.getResultList();
      }
    );
  }
}
