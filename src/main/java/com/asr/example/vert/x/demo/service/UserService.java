package com.asr.example.vert.x.demo.service;

import com.asr.example.vert.x.demo.domain.UserEntity;
import com.asr.example.vert.x.demo.repository.UserRepository;
import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Uni<UserEntity> findById(final Long id) {
    return userRepository.findById(id);
  }

  public Uni<UserEntity> save(UserEntity user) {
    return userRepository.save(user)
      .onItem()
      .transform(unused -> user);
  }

  public Uni<UserEntity> update(final long id, final UserEntity requestedUser) {
    return userRepository.findById(id)
      .onItem()
      .transformToUni((foundEntity, uniEmitter) -> {
        if (foundEntity == null) {
          throw new IllegalArgumentException("User not found");
        }
        requestedUser.setId(id);
        userRepository.update(requestedUser)
          .subscribe()
          .with(
            updateEntity -> {
              LOGGER.debug("User updated successfully with id: " + id);
              uniEmitter.complete(requestedUser);
            }
          );
      });
  }

  public Uni<UserEntity> delete(final long id) {
    return userRepository.findById(id)
      .onItem()
      .transformToUni((foundEntity, uniEmitter) -> {
        if (foundEntity == null) {
          LOGGER.warn("User not found for id: " + id);
          uniEmitter.complete(null);
        } else {
          userRepository.delete(foundEntity)
            .subscribe()
            .with(status -> {
                if (status > 0) {
                  LOGGER.debug("User deleted successfully with status : " + status);
                  uniEmitter.complete(foundEntity);
                } else {
                  LOGGER.warn("Invalid status " + status + " while deleting user with id: " + id);
                  uniEmitter.complete(null);
                }
              }
            );
        }
      });
  }
}
