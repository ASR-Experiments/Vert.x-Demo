package com.asr.example.vert.x.demo.route;


import com.asr.example.vert.x.demo.config.BaseConfiguration;
import com.asr.example.vert.x.demo.handler.health.CustomHealthCheckHandler;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.healthchecks.HealthCheckHandler;
import io.vertx.mutiny.ext.web.Router;

public record HealthCheckRoute() {

  public static void attach(final Router router, final Vertx vertx, final BaseConfiguration config) {
    HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);
    String template = config.getTemplate();
    healthCheckHandler
      .register("template-check", new CustomHealthCheckHandler(template));

    router
      .get("/health")
      .setName("health")
      .handler(healthCheckHandler);
  }
}
