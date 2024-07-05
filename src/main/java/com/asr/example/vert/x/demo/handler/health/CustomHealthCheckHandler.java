package com.asr.example.vert.x.demo.handler.health;

import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.Status;

import java.util.Map;

public class CustomHealthCheckHandler implements Handler<Promise<Status>> {

  private final String template;

  public CustomHealthCheckHandler(String template) {
    this.template = template;
  }

  @Override
  public void handle(Promise<Status> future) {
    if (template.isEmpty()) {
      future.complete(Status.KO().setData(new JsonObject(Map.of("template", "No template found"))));
    } else {
      future.complete(Status.OK().setData(new JsonObject(Map.of("template", template))));
    }
  }
}
