package com.asr.example.vert.x.demo;

import com.asr.example.vert.x.demo.route.HelloWorldRoute;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;

public class AppVerticle extends AbstractVerticle {

  private static final int APP_PORT = 8080;

  private final Logger LOGGER = LoggerFactory.getLogger(AppVerticle.class.getName());

  @Override
  public void start(Promise<Void> startPromise) {

    Router router = Router.router(vertx);
    HelloWorldRoute.attach(router);

    vertx
      .createHttpServer()
      .requestHandler(router)
      .exceptionHandler(error -> LOGGER.error("HTTP server error: ", error))
      .listen(APP_PORT, http -> {
          if (http.succeeded()) {
            startPromise.complete();
            LOGGER.info("HTTP server started on port " + APP_PORT);
          } else {
            startPromise.fail(http.cause());
          }
        }
      );
  }
}
