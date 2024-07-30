package com.asr.example.vert.x.demo.route;

import com.asr.example.vert.x.demo.handler.employee.GetEmployeeHandler;
import io.vertx.mutiny.ext.web.client.WebClient;

public record EmployeeRoute() {

  public static final String BASE_PATH = "/employee"; //NOSONAR

  public static void attach(io.vertx.mutiny.ext.web.Router router, WebClient webClient) {
    router
      .get(BASE_PATH)
      .handler(new GetEmployeeHandler(webClient));
  }
}
