package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.handler.CreateUserHandler;
import com.asr.example.vert.x.demo.handler.DeleteUserHandler;
import com.asr.example.vert.x.demo.handler.FindUserHandler;
import com.asr.example.vert.x.demo.handler.UpdateUserHandler;
import com.asr.example.vert.x.demo.service.UserService;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mutiny.ext.web.handler.BodyHandler;

public record UserRoute(UserService userService) {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserRoute.class);

  public void attach(io.vertx.mutiny.ext.web.Router router) {
    router
      .get("/api/user/:id")
      .setName("find-user")
      .handler(new FindUserHandler(userService));

    router
      .delete("/api/user/:id")
      .setName("delete-user")
      .handler(new DeleteUserHandler(userService));

    BodyHandler bodyHandler = BodyHandler.create();

    router
      .post("/api/user")
      .setName("create-user")
      .handler(bodyHandler)
      .handler(new CreateUserHandler(userService));

    router
      .put("/api/user/:id")
      .setName("update-user")
      .handler(bodyHandler)
      .handler(new UpdateUserHandler(userService));
  }
}
