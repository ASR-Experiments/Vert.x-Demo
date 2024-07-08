package com.asr.example.vert.x.demo.handler;

import com.asr.example.vert.x.demo.service.UserService;
import com.asr.example.vert.x.demo.util.ResponseUtil;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.Map;
import java.util.function.Consumer;

public record DeleteUserHandler(UserService userService) implements Consumer<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserHandler.class);

  @Override
  public void accept(RoutingContext routingContext) {
    LOGGER.info("DeleteUserHandler called");
    String idStr = routingContext.pathParam("id");
    if (idStr == null || idStr.trim().isEmpty()) {
      LOGGER.warn("Id is empty or missing from request");
      ResponseUtil.addError(
        "Id is empty or missing from request",
        routingContext.response()
          .setStatusCode(400)
      );
      return;
    }
    long id = Long.parseLong(idStr.trim());
    LOGGER.info("Deleting user with id: " + id);
    userService.delete(id)
      .subscribe()
      .with(
        user -> {
          if (user == null) {
            LOGGER.warn("User Not found for Id: " + id);
            ResponseUtil.addError(
              "User Not found for Id: " + id,
              routingContext.response()
                .setStatusCode(404)
            );
          } else {
            LOGGER.info("User deleted successfully");
            routingContext.response()
              .setStatusCode(200)
              .putHeader("content-type", "application/json")
              .endAndForget(JsonObject.mapFrom(user).encode());
          }
        },
        throwable -> ResponseUtil.addError(
          "Something went wrong",
          routingContext.response()
            .setStatusCode(500),
          Map.of("cause", throwable.getMessage()))
      );
  }
}
