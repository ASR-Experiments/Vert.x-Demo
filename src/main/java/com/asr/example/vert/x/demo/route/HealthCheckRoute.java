package com.asr.example.vert.x.demo.route;


import com.asr.example.vert.x.demo.config.BaseConfiguration;
import com.asr.example.vert.x.demo.config.DatabaseConfiguration;
import com.asr.example.vert.x.demo.handler.CustomDBHealthCheckHandler;
import com.asr.example.vert.x.demo.handler.health.CustomHealthCheckHandler;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.healthchecks.HealthCheckHandler;
import io.vertx.mutiny.ext.web.Router;

public record HealthCheckRoute() {

  public static void attach(final Router router, final Vertx vertx, final BaseConfiguration config, DatabaseConfiguration databaseConfiguration) {
    HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);
    String template = config.getTemplate();
    healthCheckHandler
      .register("template-check", new CustomHealthCheckHandler(template));
    healthCheckHandler
      .register("database-check",
        2000L,
        new CustomDBHealthCheckHandler(databaseConfiguration.getSessionFactory())
      );

    router
      .get("/health")
      .setName("health")
      .handler(healthCheckHandler);
  }
}
