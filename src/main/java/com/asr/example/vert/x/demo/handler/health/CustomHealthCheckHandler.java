package com.asr.example.vert.x.demo.handler.health;

import io.smallrye.mutiny.operators.AbstractUni;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.Status;

import java.util.Map;

public class CustomHealthCheckHandler extends AbstractUni<Status> {

  private final String template;

  public CustomHealthCheckHandler(String template) {
    this.template = template;
  }

  @Override
  public void subscribe(UniSubscriber<? super Status> subscriber) {
    if (template.isEmpty()) {
      subscriber.onItem(Status.KO().setData(new JsonObject(Map.of("template", "No template found"))));
    } else {
      subscriber.onItem(Status.OK().setData(new JsonObject(Map.of("template", template))));
    }
  }
}
