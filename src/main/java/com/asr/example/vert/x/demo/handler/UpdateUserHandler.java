package com.asr.example.vert.x.demo.handler;

import com.asr.example.vert.x.demo.domain.UserEntity;
import com.asr.example.vert.x.demo.service.UserService;
import com.asr.example.vert.x.demo.util.ResponseUtil;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.function.Consumer;

public record UpdateUserHandler(UserService userService) implements Consumer<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserHandler.class);

  @Override
  public void accept(final RoutingContext context) {
    String idStr = context.pathParam("id");
    if (idStr == null || idStr.trim().isEmpty()) {
      ResponseUtil.addError(
        "Id is empty or missing from request",
        context.response().setStatusCode(400)
      );
      return;
    }
    long id = Long.parseLong(idStr.trim());
    LOGGER.info("Updating user with id: " + id);
    userService.update(id, context.body().asPojo(UserEntity.class))
      .subscribe()
      .with(entity -> {
        if (entity == null) {
          ResponseUtil.addError(
            "User not found for id: " + id,
            context.response().setStatusCode(404)
          );
        } else {
          LOGGER.info("User deleted successfully");
          context.response()
            .setStatusCode(200)
            .putHeader("content-type", "application/json")
            .endAndForget(JsonObject.mapFrom(entity).encode());
        }
      });
  }
}
