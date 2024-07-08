package com.asr.example.vert.x.demo.handler.user;

import com.asr.example.vert.x.demo.service.UserService;
import com.asr.example.vert.x.demo.util.ResponseUtil;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.function.Consumer;

public record DeleteUserHandler(UserService userService) implements Consumer<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserHandler.class);

  @Override
  public void accept(RoutingContext routingContext) {
    LOGGER.debug("DeleteUserHandler called");
    long id;
    try {
      id = ResponseUtil.parseLong(routingContext.pathParam("id"));
    } catch (IllegalArgumentException e) {
      LOGGER.warn("Invalid Id " + e.getMessage());
      ResponseUtil.addError(
        "Invalid Id " + e.getMessage(),
        routingContext.response()
          .setStatusCode(400),
        ResponseUtil.formError(e)
      );
      return;
    }
    LOGGER.debug("Deleting user with id: " + id);
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
            LOGGER.debug("User deleted successfully");
            routingContext.response()
              .setStatusCode(200)
              .putHeader("content-type", "application/json")
              .endAndForget(JsonObject.mapFrom(user).encode());
          }
        }
      );
  }
}
