# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## **Iteration 4**: [0.0.4] - 2024-07-31

1. Added 2 sample filters on Request and response respectively:
   1. `IdempotencyHandler` filters the request based on the `X-Idempotency-Token` header.
      * It expects a unique header for each requests.
      * If header is not present then it invalidates the request.
      * If header is already used in a prior requests, it still invalidates the request.
   2. `PoweredByHandler` adds a header `X-Powered-By` to the response.
      * If configuration `poweredBy` is present, than value is set to be the same.
      * Otherwise, it defaults to `ASR`.

## **Iteration 3**: [0.0.3] - 2024-07-29

1. A basic Rest endpoint to interact with another downstream Rest API.
   * Hit the following URL in browser to see it in action.
     > [http://localhost:8080/api/employee](http://localhost:8080/api/employee)

## **Iteration 2**: [0.0.2] - 2024-07-29

1. A basic Rest endpoint to interact with User table in Postgres.
   * Hit the following URL in browser to see it in action.
      > [http://localhost:8080/api/user](http://localhost:8080/api/user)

     It will return an empty array for now, see code for more details on creation and rest of the CRUD operations on
     users.

## **Iteration 1**: [0.0.1] - 2024-07-05

1. A basic Rest endpoint which shows Hello message to provided name, hit the following URL in browser to see it in
   action.
   > [http://localhost:8080/hello-world?name=John](http://localhost:8080/hello?name=John)
2. A basic health check to show health check of the web-server.
3. Basic configuration setup.
