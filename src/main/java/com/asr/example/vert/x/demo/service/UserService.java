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
    return Uni
      .createFrom()
      .completionStage(() -> userRepository.findById(id));
  }

  public Uni<UserEntity> save(UserEntity user) {
    return Uni
      .createFrom()
      .completionStage(() -> userRepository.save(user))
      .onItem()
      .transform(unused -> user);
  }

  public Uni<UserEntity> update(final long id, final UserEntity updatedUser) {
    return Uni
      .createFrom()
      .completionStage(() -> userRepository.findById(id))
      .onItem()
      .transformToUni((entity, uniEmitter) -> {
        if (entity == null) {
          throw new IllegalArgumentException("User not found");
        }
        updatedUser.setId(id);
        userRepository.update(updatedUser)
          .handle(
            (unused, throwable) -> {
              if (throwable != null) {
                LOGGER.error("Error while updating user", throwable);
                uniEmitter.fail(throwable);
              } else {
                LOGGER.debug("User updated successfully");
                uniEmitter.complete(updatedUser);
              }
              return updatedUser;
            }
          );
      });
  }

  public Uni<UserEntity> delete(final long id) {
    return Uni
      .createFrom()
      .completionStage(() -> userRepository.findById(id))
      .onItem()
      .transformToUni((userEntity, uniEmitter) -> {
        if (userEntity == null) {
          LOGGER.warn("User not found for id: " + id);
          uniEmitter.complete(null);
        } else {
          userRepository.delete(userEntity)
            .handle((status, throwable) -> {
              if (throwable != null) {
                LOGGER.error("Error while deleting user", throwable);
                uniEmitter.fail(throwable);
              } else {
                if (status > 0) {
                  LOGGER.debug("User deleted successfully with status : " + status);
                  uniEmitter.complete(userEntity);
                } else {
                  LOGGER.warn("Invalid status " + status + " while deleting user");
                  uniEmitter.complete(null);
                }
              }
              return userEntity;
            });
        }
      });
  }
}
