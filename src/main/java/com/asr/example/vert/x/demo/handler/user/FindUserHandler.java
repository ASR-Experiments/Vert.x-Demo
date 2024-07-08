package com.asr.example.vert.x.demo.handler.user;

import com.asr.example.vert.x.demo.service.UserService;
import com.asr.example.vert.x.demo.util.ResponseUtil;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.function.Consumer;

public class FindUserHandler implements Consumer<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(FindUserHandler.class.getName());

  private final UserService userService;

  public FindUserHandler(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void accept(RoutingContext routingContext) {
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
    userService.findById(id)
      .subscribe()
      .with(
        user -> {
          if (user == null) {
            LOGGER.warn("User not found for id: " + id);
            ResponseUtil.addError(
              "User Not found for Id: " + id,
              routingContext.response()
                .setStatusCode(404)
            );
          } else {
            LOGGER.info("User found for id: " + id);
            routingContext.response()
              .putHeader("Content-Type", "application/json")
              .endAndForget(JsonObject.mapFrom(user).encode());
          }
        }
      );
  }
}
