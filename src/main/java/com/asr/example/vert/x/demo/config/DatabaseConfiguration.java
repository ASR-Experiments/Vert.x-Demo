package com.asr.example.vert.x.demo.config;

import com.asr.example.vert.x.demo.domain.UserEntity;
import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class DatabaseConfiguration {

  private final Mutiny.SessionFactory sessionFactory;

  private DatabaseConfiguration(Mutiny.SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public static DatabaseConfiguration create(BaseConfiguration appConfig) {
    return new DatabaseConfiguration(getSessionFactory(appConfig));
  }

  static Configuration getConfiguration(BaseConfiguration appConfig) {
    Configuration dbConfig = new Configuration();
    Properties props = appConfig.getDatabaseAsProperties();
    if (props != null) {
      dbConfig.addProperties(props);
    }
    return dbConfig;
  }

  static Configuration addEntities(Configuration dbConfig) {
    dbConfig.addAnnotatedClass(UserEntity.class);
    return dbConfig;
  }

  static ServiceRegistry getServiceRegistry(Configuration dbConfig) {
    return new ReactiveServiceRegistryBuilder()
      .applySettings(dbConfig.getProperties())
      .build();
  }

  static Mutiny.SessionFactory getSessionFactory(Configuration dbConfig, ServiceRegistry serviceRegistry) {
    return dbConfig
      .buildSessionFactory(serviceRegistry)
      .unwrap(Mutiny.SessionFactory.class);
  }

  public static Mutiny.SessionFactory getSessionFactory(BaseConfiguration appConfig) {
    Configuration dbConfig = getConfiguration(appConfig);
    dbConfig = addEntities(dbConfig);
    ServiceRegistry serviceRegistry = getServiceRegistry(dbConfig);
    return getSessionFactory(dbConfig, serviceRegistry);
  }

  public Mutiny.SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}
