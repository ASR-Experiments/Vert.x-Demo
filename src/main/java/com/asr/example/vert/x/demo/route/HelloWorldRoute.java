package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.dto.http.response.SayingDto;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.concurrent.atomic.AtomicLong;

public class HelloWorldRoute {

  private static final AtomicLong COUNT = new AtomicLong(0L);

  public static void attach(Router web) {
    web
      .route("/hello-world")
      .handler(routingContext -> {
        routingContext.response()
          .putHeader("Content-Type", "application/json")
          .end(JsonObject.mapFrom(createResponse()).encode());
      });
  }

  private static SayingDto createResponse() {
    return new SayingDto(COUNT.getAndIncrement(), "Hello Stranger!");
  }
}
