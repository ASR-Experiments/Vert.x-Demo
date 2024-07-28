package com.asr.example.vert.x.demo.handler.user;

import com.asr.example.vert.x.demo.service.UserService;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.function.Consumer;

public class FindAllUserHandler implements Consumer<RoutingContext> {

  private static final Logger LOGGER = LoggerFactory.getLogger(FindAllUserHandler.class.getName());

  private final UserService userService;

  public FindAllUserHandler(UserService userService) {
    this.userService = userService;
  }

  /**
   * Performs this operation on the given argument.
   *
   * @param routingContext the input argument
   */
  @Override
  public void accept(RoutingContext routingContext) {
    Integer offset = routingContext.queryParam("offset").stream()
      .findFirst()
      .map(Integer::parseInt)
      .orElse(0);
    Integer limit = routingContext.queryParam("limit").stream()
      .findFirst()
      .map(Integer::parseInt)
      .orElse(10);
    userService.findAll(offset, limit)
      .subscribe()
      .with(
        users -> {
          if (users.isEmpty()) {
            LOGGER.warn("No users found");
            routingContext.response()
              .setStatusCode(404)
              .end();
          } else {
            LOGGER.info("Users found");
            routingContext.response()
              .putHeader("Content-Type", "application/json")
              .endAndForget(
                users
                  .stream()
                  .map(JsonObject::mapFrom)
                  .collect(JsonArray::new, JsonArray::add, JsonArray::addAll)
                  .encode()
              );
          }
        }
      );
  }
}
