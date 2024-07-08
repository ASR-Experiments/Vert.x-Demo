package com.asr.example.vert.x.demo.util;

import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.http.HttpServerResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

  public static Map<String, Object> formError(Throwable throwable) {
    return Optional.ofNullable(throwable)
      .map(error -> Map.of(
        "error", getErrorString(error),
        "localisedError", Optional.of(error).map(Throwable::getLocalizedMessage).orElse(""),
        "cause", Optional.of(error).map(Throwable::getCause).map(ResponseUtil::getErrorString).orElse(""),
        "first", Arrays.stream(error.getStackTrace())
          .limit(5)
          .map(StackTraceElement::toString)
          .toList()))
      .orElse(new HashMap<>());
  }

  private static String getErrorString(Throwable error) {
    return Optional.of(error).map(err -> err.getClass() + " :: " + err.getMessage()).orElse("");
  }

  public static Long parseLong(String str) {
    try {
      if (str == null || str.trim().isEmpty()) {
        throw new IllegalArgumentException("Parameter is empty");
      }
      return Long.parseLong(str.trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Parameter (" + str + ") is in invalid Number format", e);
    }
  }
}
