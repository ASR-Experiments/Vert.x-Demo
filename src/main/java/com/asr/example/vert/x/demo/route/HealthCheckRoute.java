package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.config.BaseConfiguration;
import com.asr.example.vert.x.demo.handler.health.CustomHealthCheckHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.web.Router;

public class HealthCheckRoute {

  public static void attach(final Vertx vertx, final Router web, final BaseConfiguration config) {
    HealthCheckHandler healthCheckHandler = HealthCheckHandler.
      createWithHealthChecks(HealthChecks.create(vertx));

    String template = config.getTemplate();

    healthCheckHandler.register(
      "template-check",
      new CustomHealthCheckHandler(template));

    web
      .get("/health")
      .setName("health")
      .handler(healthCheckHandler);
  }

}
