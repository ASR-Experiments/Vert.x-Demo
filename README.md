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
3. It also covers the Database interactions with a postgres database, check [Pre-requisites](###Database-setup).
   - To access the endpoint, hit the following URL in the **cURL** or other clients:
      ```shell
         curl --location 'localhost:8080/api/user' \
              --header 'X-Idempotency-Token: b415d958-b617-404b-80e9-009202a82395' \

      ```
     > It will return a response similar to following.
     ```json
       [{"id":1,"name":"username","email":"username@test.com","password":"password","role":"ADMIN"}]
     ```
   - To create a user, hit the following URL in **cURL** or other clients:
     ```shell
        curl --location 'localhost:8080/api/user' \
              --header 'X-Idempotency-Token: b415d958-b617-404b-80e9-009202a82395' \
              --header 'Content-Type: application/json' \
              --data-raw '{
              "name": "user",
              "email": "user@test.com",
              "password": "password",
              "role": "USER"
              }'
      ```
     > It will return the created user in the response.
       ```json
         {"id":2,"name":"user","email":"user@test.com","password":"password","role":"USER"}
       ```
    - Calling the first api again, will respond with the newly created user.
    - Rest of the CRUD operations can be performed in a similar way by changing the method and payload.
      * For more information, check the code in `UserRoute` class.
      * OR, check `/docs` folder.

4. Additionally, it also covers downstream calls using `EmployeeRoute`. To test try the following api:
   ```shell
    curl --location 'http://localhost:8080/api/employee' \
         --header 'X-Idempotency-Token: b415d958-b617-404b-80e9-009202a82395'
    ```
   - It should return response similar to following:
     ```json
     [
      {
        "id": 1,
        "name": "Leanne Graham",
        "username": "Bret",
        "email": "Sincere@april.biz",
        "address": {
            "street": "Kulas Light",
            "suite": "Apt. 556",
            "city": "Gwenborough",
            "zipcode": "92998-3874",
            "geo": {
                "lat": "-37.3159",
                "lng": "81.1496"
            }
        },
        "phone": "1-770-736-8031 x56442",
        "website": "hildegard.org",
        "company": {
            "name": "Romaguera-Crona",
            "catchPhrase": "Multi-layered client-server neural-net",
            "bs": "harness real-time e-markets"
        }
      } ...
     ```

## Pre-Requisite

### Database setup

1. **Pre-requisite**: Postgres should be installed and running on the local machine.
   1. Create a database.
   2. Create a sequence for Id Generation.
       ```sql
       CREATE SEQUENCE IF NOT EXISTS user_schema.id_sequence
       INCREMENT 1
       START 1
       MINVALUE 1
       MAXVALUE 9223372036854775807
       CACHE 1;
       ```
   3. Finally, Create the following table:
       ```sql
       CREATE TABLE IF NOT EXISTS tbl_user
       (
       name character varying(128) NOT NULL,
       email character varying(128),
       password text NOT NULL,
       id bigint NOT NULL DEFAULT nextval('user_schema.id_sequence'::regclass),
       CONSTRAINT tbl_user_pkey PRIMARY KEY (id),
       CONSTRAINT "user" UNIQUE NULLS NOT DISTINCT (name)
       );
       ```
   4. Update `config.yml` by replacing the placeholders for database details,
      like `<DB_USER>`, `<DB_PASSWORD>`, `<DB_NAME>`,
      `<DB_HOST>`, `<DB_PORT>`.
   5. Add another column for Authorization (added in Iteration 5 (0.0.5))
      ```sql
      ALTER TABLE IF EXISTS user_schema.tbl_user
         ADD COLUMN role character varying(16) DEFAULT USER;
      ```
   6. Add a sample user for testing.
      ```sql
        INSERT INTO tbl_user (name, email, password, role)
           VALUES ('username', 'username@test.com', 'password', 'ADMIN');
      ```

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
