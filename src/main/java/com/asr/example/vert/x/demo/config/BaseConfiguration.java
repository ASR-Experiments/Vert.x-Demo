package com.asr.example.vert.x.demo.config;

import java.util.Map;
import java.util.Properties;

public class BaseConfiguration {
  String template;
  String defaultName;
  Map<String, Object> database;

  public Properties getDatabaseAsProperties() {
    Properties properties = new Properties();
    flattenJsonObject(database, "", properties);
    return properties;
  }

  public Map<String, Object> getDatabase() {
    return database;
  }

  public void setDatabase(Map<String, Object> database) {
    this.database = database;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public String getDefaultName() {
    return defaultName;
  }

  public void setDefaultName(String defaultName) {
    this.defaultName = defaultName;
  }

  private static void flattenJsonObject(
    Map<String, Object> map,
    String parentKey,
    Properties properties
  ) {
    if (map == null || map.isEmpty()) {
      return;
    }
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      String key = parentKey.isEmpty() ? entry.getKey() : parentKey + "." + entry.getKey();
      Object value = entry.getValue();
      if (value instanceof Map) {
        // Recursive call for nested objects
        flattenJsonObject((Map<String, Object>) value, key, properties);
      } else {
        properties.put(key, value.toString());
      }
    }
  }
}
