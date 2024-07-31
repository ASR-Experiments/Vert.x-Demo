package com.asr.example.vert.x.demo.handler.user;

import com.asr.example.vert.x.demo.domain.UserEntity;
import com.asr.example.vert.x.demo.service.UserService;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.RequestBody;
import io.vertx.mutiny.ext.web.RoutingContext;

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
          .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .endAndForget(JsonObject.mapFrom(user).encode())
      );
  }
}
