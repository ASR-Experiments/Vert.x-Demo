package com.asr.example.vert.x.demo.handler.common;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.Optional;
import java.util.function.Consumer;


public record IdempotencyHandler() implements Consumer<RoutingContext> {

  @Override
  public void accept(RoutingContext routingContext) {
    String idempotencyToken = routingContext
      .request()
      .getHeader("X-Idempotency-Token");
    Optional<JsonObject> response = validateToken(idempotencyToken);
    if (response.isPresent()) {
      routingContext
        .response()
        .setStatusCode(400)
        .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .endAndForget(response.get().encode());
      return;
    }
    response = crossCheckToken(idempotencyToken);
    if (response.isPresent()) {
      routingContext
        .response()
        .setStatusCode(409)
        .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .endAndForget(response.get().encode());
      return;
    }
    routingContext.next();
  }

  private Optional<JsonObject> crossCheckToken(String idempotencyToken) {
    return Optional.empty();
  }

  private Optional<JsonObject> validateToken(String idempotencyToken) {
    if (idempotencyToken == null || idempotencyToken.isEmpty()) {
      return Optional.of(JsonObject.of(
        "type", "error",
        "message", "X-Idempotency-Token header is missing"
      ));
    }
    return Optional.empty();
  }
}
