package com.asr.example.vert.x.demo.handler;

import com.asr.example.vert.x.demo.service.UserService;
import com.asr.example.vert.x.demo.util.ResponseUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.Map;
import java.util.function.Consumer;

public class FindUserHandler implements Consumer<RoutingContext> {

  private final UserService userService;

  public FindUserHandler(UserService userService) {
    this.userService = userService;
  }

  /**
   * Performs this operation on the given argument.
   *
   * @param routingContext the input argument
   */
  @Override
  public void accept(RoutingContext routingContext) {
    String idStr = routingContext.pathParam("id");
    if (idStr == null || idStr.trim().isEmpty()) {
      ResponseUtil.addError(
        "Id is empty or missing from request",
        routingContext.response().setStatusCode(400)
      );
      return;
    }
    Long id = Long.parseLong(idStr.trim());
    userService.findById(id)
      .subscribe()
      .with(
        user -> {
          if (user == null) {
            ResponseUtil.addError(
              "User Not found for Id" + id,
              routingContext.response()
                .setStatusCode(404)
            );
          } else {
            routingContext.response()
              .putHeader("Content-Type", "application/json")
              .endAndForget(JsonObject.mapFrom(user).encode());
          }
        },
        throwable ->
          ResponseUtil.addError(
            throwable.getMessage(),
            routingContext.response().setStatusCode(500),
            Map.of("cause", throwable.getMessage())
          )
      );
  }
}
