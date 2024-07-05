package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.config.BaseConfiguration;
import com.asr.example.vert.x.demo.dto.http.response.SayingDto;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.concurrent.atomic.AtomicLong;

public class HelloWorldRoute {

  private static final AtomicLong COUNT = new AtomicLong(0L);

  public static void attach(final Router web, final BaseConfiguration result) {
    web
      .route("/hello-world")
      .handler(routingContext -> {
        final String name = routingContext
          .queryParam("name")
          .stream()
          .filter(param -> !param.isEmpty())
          .findFirst()
          .orElse(result.getDefaultName());
        routingContext
          .response()
          .putHeader("Content-Type", "application/json")
          .end(
            JsonObject
              .mapFrom(createResponse(result.getTemplate(), name))
              .encode());
      });
  }

  private static SayingDto createResponse(String template, String name) {
    return new SayingDto(COUNT.getAndIncrement(), template.formatted(name));
  }
}
