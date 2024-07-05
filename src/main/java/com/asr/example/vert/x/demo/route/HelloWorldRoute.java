package com.asr.example.vert.x.demo.route;

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
          .end(createResponse());
      });
  }

  private static String createResponse() {
    return """
      {
          "id": %s,
          "content": "Hello, Stranger!"
      }
      """
      .formatted(COUNT.getAndIncrement());
  }
}
