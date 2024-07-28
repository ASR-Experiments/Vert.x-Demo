package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.handler.user.CreateUserHandler;
import com.asr.example.vert.x.demo.handler.user.DeleteUserHandler;
import com.asr.example.vert.x.demo.handler.user.FindAllUserHandler;
import com.asr.example.vert.x.demo.handler.user.FindUserHandler;
import com.asr.example.vert.x.demo.handler.user.UpdateUserHandler;
import com.asr.example.vert.x.demo.service.UserService;
import io.vertx.mutiny.ext.web.handler.BodyHandler;

public record UserRoute(UserService userService) {


  public static final String BASE_PATH = "/user"; //NOSONAR

  public void attach(io.vertx.mutiny.ext.web.Router router) {

    router
      .get(BASE_PATH)
      .setName("find-all-users")
      .handler(new FindAllUserHandler(userService));

    router
      .get(BASE_PATH + "/:id")
      .setName("find-user")
      .handler(new FindUserHandler(userService));

    router
      .delete(BASE_PATH + "/:id")
      .setName("delete-user")
      .handler(new DeleteUserHandler(userService));

    BodyHandler bodyHandler = BodyHandler.create();

    router
      .post(BASE_PATH)
      .setName("create-user")
      .handler(bodyHandler)
      .handler(new CreateUserHandler(userService));

    router
      .put(BASE_PATH + "/:id")
      .setName("update-user")
      .handler(bodyHandler)
      .handler(new UpdateUserHandler(userService));
  }
}
