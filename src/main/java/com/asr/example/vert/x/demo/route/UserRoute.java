package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.handler.FindUserHandler;
import com.asr.example.vert.x.demo.service.UserService;

public record UserRoute(UserService userService) {

  public void attach(io.vertx.mutiny.ext.web.Router router) {
    router
      .route("/api/user/:id")
      .setName("user")
      .handler(new FindUserHandler(userService));
  }
}
