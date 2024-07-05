# Vert.x Demo

This is a simple demo of a **Vert.x** application. It is a simple RESTful service that allows you to create, read,
update and delete users.

## Running the application

### Via **Intellij**

1. Import run configuration from `./idea/runConfigurations/Run_Application.xml`
2. Click on the Run or Debug button to Run the application.

### Via **Maven**

1. Run the following command in the terminal:
    ```shell
    mvn clean package
    ```
2. Run the following command in the terminal:
    ```shell
    mvn exec:java
    ```

## What's covered ?

1. **HealthCheck**: A simple health check to check the health of the application.
2. **HelloWorldEndpoint**: A simple endpoint that returns a message `Hello! <name>`.

## APIs

1. **HealthCheck**: A simple health check to check the health of the application.
   - **URL**: `/health`
   - **Method**: `GET`
   - **Response**:
     `{ "status": "UP", "checks": [ { "id": "template-check", "status": "UP", "data": { "template": "Hello, %s!" } } ], "outcome": "UP" }`
   - **Description**: It returns the health of the application.

2. **HelloWorldEndpoint**: A simple endpoint that returns a message `Hello! <name>`.
   - **URL**: `/hello`
   - **Method**: `GET`
   - **Response**:
     `{ "id": 1, "content": "Hello, Stranger!" }`
   - **Query Params**:
     - `name`: The name of the user.
       - **Description**: It returns the message `Hello! <name>`.
       - **Example**: `/hello?name=John`
       - **Response**: `{ "id": 0, "content": "Hello, John!" }`

## Sonar

### Badges

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=bugs)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)

### Stats

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=coverage)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=ASR-Experiments_Vert.x-Demo&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=ASR-Experiments_Vert.x-Demo)
