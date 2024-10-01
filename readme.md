# E-Commerce Application

This project is an simulation of e-commerce built with Angular (frontend) and Spring Boot (backend). The frontend handles the user interface while the backend provides REST APIs and business logic.

Only the backend is secured with basic authentication. The username is `admin` and the password is `admin`.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Running the Frontend (Angular)](#running-the-frontend-angular)
- [Running the Backend (Spring Boot)](#running-the-backend-spring-boot)

## Prerequisites

To run the project, you need to have the following software installed:

- [Node.js 20](https://nodejs.org/) and npm (Node Package Manager)
- [Angular CLI](https://angular.io/cli)
- [Java 21](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or newer
- [Maven](https://maven.apache.org/download.cgi)

## Running the Frontend (Angular)

The frontend is built using Angular. To run it:

1. Navigate to the `frontend` directory:

    ```sh
    cd frontend
    ```

2. Install the dependencies:

    ```sh
    npm install
    ```

3. Start the development server:

    ```sh
    npm run start
    ```

4. Open your browser and navigate to `http://localhost:4200/`. The application should be up and running.


## Running the Backend (Spring Boot)

The backend is built using Spring Boot. To run it:

1. Navigate to the `backend` directory:

    ```sh
    cd backend
    ```

2. Build the application using Maven:

    ```sh
    mvn clean install
    ```

4. Run the application:

    ```sh
    mvn spring-boot:run
    ```

5. The backend should be running on `http://localhost:8080/`.

### Additional Spring Boot Commands

- To create a JAR file for deployment:

    ```sh
    mvn clean package
    ```