package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.config.BaseConfiguration;
import com.asr.example.vert.x.demo.handler.hello.world.HelloWorldHandler;

public record HelloWorldRoute() {

  public static void attach(io.vertx.mutiny.ext.web.Router router, BaseConfiguration config) {
    router
      .route("/hello-world")
      .setName("hello-world")
      .handler(new HelloWorldHandler(config));

  }
}
