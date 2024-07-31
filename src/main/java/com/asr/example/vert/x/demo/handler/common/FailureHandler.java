package com.asr.example.vert.x.demo.handler.common;

import com.asr.example.vert.x.demo.util.ResponseUtil;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.function.Consumer;

public record FailureHandler() implements Consumer<RoutingContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(FailureHandler.class.getName());

  @Override
  public void accept(RoutingContext routingContext) {
    if (routingContext.response().ended()) {
      return;
    }
    LOGGER.error("Error while processing request", routingContext.failure());
    ResponseUtil.addError(
      "Internal Server Error",
      routingContext.response().setStatusCode(500),
      ResponseUtil.formError(routingContext.failure())
    );
  }
}
