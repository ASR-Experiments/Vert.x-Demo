package com.asr.example.vert.x.demo.handler;

import com.asr.example.vert.x.demo.domain.UserEntity;
import com.asr.example.vert.x.demo.service.UserService;
import com.asr.example.vert.x.demo.util.ResponseUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.RequestBody;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.Map;
import java.util.function.Consumer;

public record CreateUserHandler(UserService userService) implements Consumer<RoutingContext> {

  @Override
  public void accept(RoutingContext routingContext) {
    RequestBody body = routingContext.body();
    UserEntity request = body.asPojo(UserEntity.class);
    userService
      .save(request)
      .subscribe()
      .with(
        user -> routingContext.response()
          .setStatusCode(201)
          .putHeader("Content-Type", "application/json")
          .endAndForget(JsonObject.mapFrom(user).encode()),
        throwable ->
          ResponseUtil.addError(
            "Something went wrong",
            routingContext.response().setStatusCode(500),
            Map.of("cause", throwable.getMessage())
          )
      );
  }
}
