package com.asr.example.vert.x.demo.handler.common;

import com.asr.example.vert.x.demo.config.BaseConfiguration;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.function.Consumer;

public record PoweredByHandler(BaseConfiguration config) implements Consumer<RoutingContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(PoweredByHandler.class.getName());

  @Override
  public void accept(RoutingContext routingContext) {
    routingContext
      .response()
      .putHeader("X-Powered-By", config.getPoweredBy().orElse("ASR"));
    routingContext.next();
  }
}
