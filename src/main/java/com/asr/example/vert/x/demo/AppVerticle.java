package com.asr.example.vert.x.demo;

import com.asr.example.vert.x.demo.config.BaseConfiguration;
import com.asr.example.vert.x.demo.route.HelloWorldRoute;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class AppVerticle extends AbstractVerticle {

  private static final int APP_PORT = 8080;

  private final Logger LOGGER = LoggerFactory.getLogger(AppVerticle.class.getName());

  @Override
  public void start(Promise<Void> startPromise) {

    Router router = Router.router(vertx);

    // Configuration setup
    setUpConfig()
      .onComplete(json -> {
          BaseConfiguration result = json.result().mapTo(BaseConfiguration.class);
          HelloWorldRoute.attach(router, result);
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
      );


  }

  private Future<JsonObject> setUpConfig() {
    ConfigStoreOptions configStoreOptions = new ConfigStoreOptions()
      .setFormat("properties")
      .setType("file")
      .setConfig(new JsonObject().put("path", "application.properties").put("raw-data", true));
    ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(configStoreOptions);
    return ConfigRetriever.create(vertx, options).getConfig();
  }
}
