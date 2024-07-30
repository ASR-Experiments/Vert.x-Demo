package com.asr.example.vert.x.demo.handler.employee;

import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.RoutingContext;
import io.vertx.mutiny.ext.web.client.WebClient;

import java.util.function.Consumer;

public record GetEmployeeHandler(WebClient webClient) implements Consumer<RoutingContext> {
  /**
   * Performs this operation on the given argument.
   *
   * @param routingContext the input argument
   */
  @Override
  public void accept(RoutingContext routingContext) {
    webClient
      .get(80, "jsonplaceholder.typicode.com", "/users")
      .timeout(5000)
      .send()
      .subscribe()
      .with(
        bufferHttpResponse -> routingContext.response()
          .setStatusCode(200)
          .putHeader("Content-Type", "application/json")
          .endAndForget(bufferHttpResponse.bodyAsBuffer()),
        throwable -> routingContext
          .response()
          .setStatusCode(500)
          .putHeader("Content-Type", "application/json")
          .endAndForget(JsonObject.of("error", throwable.getMessage(), "type", throwable.getClass()).encode())
      );
  }
}
