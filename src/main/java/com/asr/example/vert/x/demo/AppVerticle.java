package com.asr.example.vert.x.demo;


import com.asr.example.vert.x.demo.config.BaseConfiguration;
import com.asr.example.vert.x.demo.config.DatabaseConfiguration;
import com.asr.example.vert.x.demo.handler.common.FailureHandler;
import com.asr.example.vert.x.demo.handler.common.IdempotencyHandler;
import com.asr.example.vert.x.demo.handler.common.PoweredByHandler;
import com.asr.example.vert.x.demo.repository.UserRepository;
import com.asr.example.vert.x.demo.route.EmployeeRoute;
import com.asr.example.vert.x.demo.route.HealthCheckRoute;
import com.asr.example.vert.x.demo.route.HelloWorldRoute;
import com.asr.example.vert.x.demo.route.UserRoute;
import com.asr.example.vert.x.demo.service.UserService;
import com.asr.example.vert.x.demo.util.IdempotencyCache;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.config.ConfigRetriever;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.client.WebClient;

public class AppVerticle extends AbstractVerticle {

  private static final int APP_PORT = 8080;

  private static final Logger LOGGER = LoggerFactory.getLogger(AppVerticle.class.getName());

  @Override
  public Uni<Void> asyncStart() {
    final Router router = Router.router(vertx);
    return setUpConfig()
      .onItem()
      .transformToUni(
        jsonConfig ->
        {
          BaseConfiguration config = jsonConfig.mapTo(BaseConfiguration.class);

          Uni<DatabaseConfiguration> databaseSetupFuture = Uni.createFrom().deferred(() -> {
            LOGGER.debug("Starting Hibernate Reactive");
            return Uni.createFrom().item(DatabaseConfiguration.create(config));
          });

          return vertx
            .executeBlocking(databaseSetupFuture)
            .onItem()
            .transformToUni(
              databaseConfiguration -> {
                LOGGER.debug("Database setup completed");

                // Initializing services
                // Handlers
                final IdempotencyCache idempotencyCache = new IdempotencyCache(60);
                final IdempotencyHandler idempotencyHandler = new IdempotencyHandler(idempotencyCache);
                final FailureHandler failureHandler = new FailureHandler();
                final PoweredByHandler poweredByHandler = new PoweredByHandler(config);

                // Web client
                final WebClient webClient = WebClient.create(vertx);

                // Repos
                final UserRepository userRepository = new UserRepository(
                  databaseConfiguration.getSessionFactory());

                // Services
                final UserService userService = new UserService(userRepository);

                // Attaching routes
                final Router apiRoute = Router.router(vertx);
                router.route("/api/*")
                  .handler(poweredByHandler)
                  .handler(idempotencyHandler)
                  .failureHandler(failureHandler)
                  .subRouter(apiRoute);

                LOGGER.debug("Attaching routes");
                HealthCheckRoute.attach(router, vertx, config, databaseConfiguration);

                HelloWorldRoute.attach(apiRoute, config);
                EmployeeRoute.attach(apiRoute, webClient);
                new UserRoute(userService).attach(apiRoute);

                // Start the server
                LOGGER.debug("Starting the server");
                return vertx
                  .createHttpServer()
                  .requestHandler(router)
                  .listen(APP_PORT)
                  .onItem()
                  .transform(httpServer -> {
                    LOGGER.info("Server started on port: " + httpServer.actualPort());
                    return httpServer;
                  })
                  .onFailure()
                  .invoke(throwable -> LOGGER.error("Error while starting the server", throwable));
              }
            );
        }
      )
      .replaceWithVoid();

  }

  private Uni<JsonObject> setUpConfig() {
    LOGGER.debug("Setting up configuration");
    ConfigStoreOptions localYamlStore = new ConfigStoreOptions()
      .setFormat("yaml")
      .setType("file")
      .setOptional(true)
      .setConfig(new JsonObject().put("path", "application-local.yaml"));
    ConfigStoreOptions defaultYamlStore = new ConfigStoreOptions()
      .setFormat("yaml")
      .setType("file")
      .setOptional(true)
      .setConfig(new JsonObject().put("path", "application.yaml"));
    ConfigStoreOptions configStoreOptions = new ConfigStoreOptions()
      .setFormat("properties")
      .setType("file")
      .setConfig(new JsonObject().put("path", "application.properties").put("raw-data", true));
    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
      .addStore(localYamlStore)
      .addStore(defaultYamlStore)
      .addStore(configStoreOptions);
    return ConfigRetriever
      .create(vertx, options)
      .getConfig()
      .onSubscription()
      .invoke(uniSubscription -> LOGGER.debug("Config reading..."))
      .onFailure()
      .invoke(throwable -> LOGGER.error("Error while setting up configuration", throwable));
  }
}
