package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.config.BaseConfiguration;
import com.asr.example.vert.x.demo.handler.hello.world.HelloWorldHandler;
import io.vertx.ext.web.Router;

public class HelloWorldRoute {

  public static void attach(final Router web, final BaseConfiguration config) {
    web
      .route("/hello-world")
      .setName("hello-world")
      .handler(new HelloWorldHandler(config));
  }
}
