package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.config.BaseConfiguration;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;

import java.util.Map;

public class HealthCheckRoute {

  public static void attach(final Vertx vertx, final Router web, final BaseConfiguration config) {
    HealthCheckHandler healthCheckHandler = HealthCheckHandler.
      createWithHealthChecks(HealthChecks.create(vertx));

    String template = config.getTemplate();

    healthCheckHandler.register(
      "template-check",
      future -> {
        if (template.isEmpty()) {
          future.complete(Status.KO().setData(new JsonObject(Map.of("template", "No template found"))));
        } else {
          future.complete(Status.OK().setData(new JsonObject(Map.of("template", template))));
        }
      });

    web
      .get("/health")
      .setName("health")
      .handler(healthCheckHandler);
  }

}
