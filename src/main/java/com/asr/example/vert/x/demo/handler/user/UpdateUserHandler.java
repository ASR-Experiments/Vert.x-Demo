package com.asr.example.vert.x.demo.handler.user;

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
    long id;
    try {
      id = ResponseUtil.parseLong(context.pathParam("id"));
    } catch (IllegalArgumentException e) {
      LOGGER.warn("Invalid Id " + e.getMessage());
      ResponseUtil.addError(
        "Invalid Id " + e.getMessage(),
        context.response()
          .setStatusCode(400),
        ResponseUtil.formError(e)
      );
      return;
    }
    LOGGER.debug("Updating user with id: " + id);
    userService.update(id, context.body().asPojo(UserEntity.class))
      .subscribe()
      .with(entity -> {
        if (entity == null) {
          LOGGER.warn("User not found for id: " + id);
          ResponseUtil.addError(
            "User not found for id: " + id,
            context.response().setStatusCode(404)
          );
        } else {
          LOGGER.debug("User deleted successfully");
          context.response()
            .setStatusCode(200)
            .putHeader("content-type", "application/json")
            .endAndForget(JsonObject.mapFrom(entity).encode());
        }
      });
  }
}
