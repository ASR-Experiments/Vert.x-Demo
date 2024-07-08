package com.asr.example.vert.x.demo.handler;

import io.smallrye.mutiny.operators.AbstractUni;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.Status;
import org.hibernate.reactive.stage.Stage;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CustomDBHealthCheckHandler extends AbstractUni<Status> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomDBHealthCheckHandler.class.getName());

  private final Stage.SessionFactory sessionFactory;

  public CustomDBHealthCheckHandler(Stage.SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void subscribe(UniSubscriber<? super Status> subscriber) {
    sessionFactory
      .withSession(
        session -> {
          LOGGER.debug("Session created");
          session
            .createNativeQuery("SELECT 1")
            .executeUpdate()
            .handle(
              (integer, throwable) -> {
                if (throwable != null) {
                  LOGGER.error("Error while testing database connection", throwable);
                  subscriber.onItem(
                    Status.KO()
                      .setData(JsonObject.mapFrom(Map.of(
                        "message", "Error while testing database connection",
                        "error", throwable.getMessage()
                      )))
                  );
                } else {
                  LOGGER.info("Database connection test successful");
                  subscriber.onItem(Status.OK());
                }
                return null;
              });
          return CompletableFuture.completedFuture(null);
        }
      );
  }
}
