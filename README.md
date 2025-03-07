[Líneas desde 3: Documentación en español] | [Lines from 95: English documentation]

# API de Cálculo Dinámico con Spring Boot

Este proyecto es una API REST en Spring Boot que proporciona funcionalidades para cálculos con porcentaje dinámico, almacenamiento en memoria del porcentaje y mantenimiento de un historial de llamadas. La API está diseñada para ser desplegada en un contenedor Docker y utiliza PostgreSQL para almacenar el historial de llamadas.

## Características

1. **Cálculo con Porcentaje Dinámico**:
   - Un endpoint que recibe dos números, los suma y aplica un porcentaje obtenido dinámicamente de un servicio externo (o valor en memoria si el servicio falla).

2. **Almacenamiento en memoria**:
   - El porcentaje obtenido del servicio externo se almacena en memoria durante 30 minutos. Si el servicio externo falla, se utiliza el último valor almacenado. Si no hay valor en memoria, se devuelve un error.

3. **Historial de Llamadas**:
   - Un endpoint para recuperar el historial de llamadas a la API, incluyendo la fecha, endpoint, parámetros y respuestas/errores. Este historial se almacena en una base de datos PostgreSQL.

## Tecnologías Utilizadas

- Java 21
- Spring Boot
- PostgreSQL
- Docker
- Swagger para documentación de la API

## Instrucciones de Configuración

### Requisitos Previos

- Docker y Docker Compose instalados en su máquina. (Ver install_docker_and_docker_compose.sh para linux)
- Java 21 instalado.
- Maven instalado.

### Construcción del Archivo JAR

Antes de desplegar con Docker, puede construir el archivo JAR usando Maven:

```
mvn clean package
```

### Ejecutando la Aplicación

1. **Clonar el repositorio**:

   ```
   git clone <repository-url>
   cd springbootten
   ```

2. **Ejecutar la aplicación con Docker Compose**:

   ```
   docker-compose up --build
   ```

   Este comando iniciará tanto las aplicaciones Spring Boot como la base de datos PostgreSQL.

### Configuración de la Base de Datos

La base de datos PostgreSQL está configurada en el archivo `docker-compose.yml`. El historial de llamadas se almacenará en la tabla `call_history_db`.

### Accediendo a la API

- La API estará disponible en `http://localhost:8080`.
- La documentación de Swagger se puede acceder en `http://localhost:8080/swagger-ui.html`.

### Pruebas

Las pruebas unitarias están incluidas en el proyecto. Puede ejecutar las pruebas usando Maven:

```
mvn test
```

### Detener el Servicio Externo

Use el comando `docker-compose stop [mock-percentage-service]` para detener el servicio "externo" y realizar pruebas con los valores en memoria.

## Endpoints de la API

- **Calcular**: `POST /api/calculate`
  - Cuerpo de la Solicitud: `{ "num1": <número>, "num2": <número> }`
  - Respuesta: `{ "result": <valor_calculado> }`

- **Historial de Llamadas**: `GET /api/history`
  - Respuesta: Lista de entradas del historial de llamadas.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulte el archivo LICENSE para más detalles.

---

# Spring Boot Dynamic Calculation API

This project is a Spring Boot REST API that provides functionalities for dynamic percentage calculation, caching of the percentage, and maintaining a call history. The API is designed to be deployed in a Docker container and uses PostgreSQL for storing call history.

## Features

1. **Dynamic Percentage Calculation**:
   - An endpoint that receives two numbers, sums them, and applies a dynamically fetched percentage from an external service (or cached in memory value if the service fails).

2. **Caching**:
   - The percentage fetched from the external service is cached in memory for 30 minutes. If the external service fails, the last cached in memory value is used. If no value is cached in memory, an error is returned.

3. **Call History**:
   - An endpoint to retrieve the history of API calls, including the date, endpoint, parameters, and responses/errors. This history is stored in a PostgreSQL database.

## Technologies Used

- Java 21
- Spring Boot
- PostgreSQL
- Docker
- Swagger for API documentation

## Setup Instructions

### Prerequisites

- Docker and Docker Compose installed on your machine. (View install_docker_and_docker_compose.sh for linux)
- Java 21 installed.
- Maven installed.

### Building the JAR File

Before deploying with Docker, you can build the JAR file using Maven:

```
mvn clean package
```

### Running the Application

1. **Clone the repository**:

   ```
   git clone <repository-url>
   cd springbootten
   ```

2. **Run the application with Docker Compose**:

   ```
   docker-compose up --build
   ```

   This command will start both the Spring Boot applications and the PostgreSQL database.

### Accessing the API

- The API will be available at `http://localhost:8080`.
- Swagger documentation can be accessed at `http://localhost:8080/swagger-ui.html`.

### Database Configuration

The PostgreSQL database is configured in the `docker-compose.yml` file. The call history will be stored in the `call_history_db` table.

### Testing

Unit tests are included in the project. You can run the tests using Maven:

```
mvn test
```

### Stopping the External Service

Use the command `docker-compose stop [mock-percentage-service]` to stop the "external" service and perform tests with in memory cached values .

## API Endpoints

- **Calculate**: `POST /api/calculate`
  - Request Body: `{ "num1": <number>, "num2": <number> }`
  - Response: `{ "result": <calculated_value> }`

- **Call History**: `GET /api/history`
  - Response: List of call history entries.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
