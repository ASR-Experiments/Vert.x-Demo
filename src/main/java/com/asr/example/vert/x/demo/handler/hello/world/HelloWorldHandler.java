package com.asr.example.vert.x.demo.handler.hello.world;

import com.asr.example.vert.x.demo.config.BaseConfiguration;
import com.asr.example.vert.x.demo.dto.http.response.SayingDto;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.concurrent.atomic.AtomicLong;

public class HelloWorldHandler implements Handler<RoutingContext> {

  private final BaseConfiguration config;

  private static final AtomicLong COUNT = new AtomicLong(0L);

  public HelloWorldHandler(BaseConfiguration config) {
    this.config = config;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    final String name = routingContext
      .queryParam("name")
      .stream()
      .filter(param -> !param.isEmpty())
      .findFirst()
      .orElse(config.getDefaultName());
    routingContext
      .response()
      .putHeader("Content-Type", "application/json")
      .end(
        JsonObject
          .mapFrom(createResponse(config.getTemplate(), name))
          .encode());
  }

  private static SayingDto createResponse(String template, String name) {
    return new SayingDto(COUNT.getAndIncrement(), template.formatted(name));
  }
}
