package com.asr.example.vert.x.demo.util;

import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.http.HttpServerResponse;

import java.util.Map;

public record ResponseUtil() {

  public static void addError(String message, HttpServerResponse response) {
    addError(message, response, null);
  }

  public static void addError(String message, HttpServerResponse response, Map<String, Object> otherData) {
    Map<String, Object> body = new java.util.HashMap<>(Map.of("message", message));
    if (otherData != null && !otherData.isEmpty())
      body.putAll(otherData);
    response
      .putHeader("Content-Type", "application/json")
      .endAndForget(JsonObject.mapFrom(body).encode());
  }

}
