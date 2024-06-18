# Pet Clinic Client

This project is a simple Java application using Spring Boot, designed as a REST client for
the [Pet Clinic](https://github.com/CarinaPorumb/pet-clinic) backend services.
It provides functionalities for listing, retrieving, creating, updating, and deleting
pet records. This application communicates securely using OAuth2 and JWT tokens.

**Note: This project requires the [Pet Clinic](https://github.com/CarinaPorumb/pet-clinic) service to be set up and
running. The Pet Clinic service will provide further instructions on additional dependencies required.**

---

## Features

- RESTful APIs for managing pet records
- Secured endpoints using OAuth2 and JWT tokens
- Integration with Spring Cloud Gateway for secure request routing

---

## Technologies Used

- Java 21
- Spring Boot 3.3.0
- Spring MVC for RESTful controllers
- Spring Security
- OAuth2 Authorization Server with JWT
- RestTemplate for HTTP client requests

---

## Getting Started

### Prerequisites

Make sure you have the following installed on your system:

- Java 21
- Maven
- Postman (or preferred HTTP client)

---

### Installation

- #### 1. Clone and Set Up the Pet Clinic Project

This project relies on the Pet Clinic services for authentication and data management.

```bash 
git clone https://github.com/CarinaPorumb/pet-clinic
```
<br>
Build the project using Maven:

```bash
mvn clean install
```

<br>

You can run the application using your IDE or from the command line:
  ```bash
   mvn spring-boot:run
   ```
Once the application is running, it will be available at [http://localhost:8081](http://localhost:8081).

<br>

- #### 2. Clone and Set Up the Pet Clinic Client Project

After setting up the Pet Clinic Project, clone and set up the client project:

```bash
git clone https://github.com/CarinaPorumb/pet-clinic-client
```
<br>

Build the project using Maven:

```bash
mvn clean install
```

<br>
You can run the application using your IDE or from the command line:

```bash
mvn spring-boot:run
```

Once the application is running, it will be available at [http://localhost:8082](http://localhost:8082).

<br>

### Testing with Postman

To test the Pet Clinic Client API endpoints, I used Postman to request an access token and make authenticated requests. Here are the steps:


1. **Configure Postman for OAuth2 Authentication**

- Open Postman and create a new request.
- Set the Request URL to your desired endpoint, e.g., `http://localhost:8082/api/pets`.
- Go to the **Authorization** tab and select **OAuth 2.0** as the type.

<br>

2. **Configure a New Token**

- **Token Name**: Choose a name for your token, e.g., `newToken`.
- **Grant Type**: Select `Client Credentials`.
- **Access Token URL**: `http://localhost:9000/oauth2/token`
- **Client ID**: `messaging-client`
- **Client Secret**: `secret`
- **Scope**: `message.read message.write`
- **Client Authentication**: Select `Send as Basic Auth header`

<br>

3. **Request Token**

- Click on **Get New Access Token**.
- Postman will request the token from the OAuth2 server.
- If successful, the token will be displayed. Click on **Use Token** to set it for your request.

<br>

4. **Send the Request**

- Ensure the token is added to the request headers.
- Send the request to the API endpoint.

---