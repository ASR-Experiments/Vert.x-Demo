package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.config.BaseConfiguration;
import com.asr.example.vert.x.demo.dto.http.response.SayingDto;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.concurrent.atomic.AtomicLong;

public class HelloWorldRoute {

  private static final AtomicLong COUNT = new AtomicLong(0L);

  public static void attach(Router web, BaseConfiguration result) {
    web
      .route("/hello-world")
      .handler(routingContext -> routingContext
        .response()
        .putHeader("Content-Type", "application/json")
        .end(
          JsonObject
            .mapFrom(createResponse(result.getTemplate(), result.getDefaultName()))
            .encode()
        ));
  }

  private static SayingDto createResponse(String template, String defaultName) {
    return new SayingDto(COUNT.getAndIncrement(), template.formatted(defaultName));
  }
}
