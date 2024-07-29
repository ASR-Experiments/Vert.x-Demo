# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).


## **Iteration 2**: [0.0.2] - 2024-07-29

1. A basic Rest endpoint to interact with User table in Postgres.
   1. Hit the following URL in browser to see it in action.
   >    [http://localhost:8080/api/user](http://localhost:8080/api/user)

   It will return an empty array for now, see code for more details on creation and rest of the CRUD operations on
   users.

## **Iteration 1**: [0.0.1] - 2024-07-05

1. A basic Rest endpoint which shows Hello message to provided name, hit the following URL in browser to see it in
   action.
   > [http://localhost:8080/hello-world?name=John](http://localhost:8080/hello?name=John)
2. A basic health check to show health check of the web-server.
3. Basic configuration setup.
