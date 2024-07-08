package com.asr.example.vert.x.demo.service;

import com.asr.example.vert.x.demo.domain.UserEntity;
import com.asr.example.vert.x.demo.repository.UserRepository;
import io.smallrye.mutiny.Uni;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Uni<UserEntity> findById(Long id) {
    return Uni
      .createFrom()
      .completionStage(() -> userRepository.findById(id));
  }
}
