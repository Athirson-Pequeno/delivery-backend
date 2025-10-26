# Delivery

[![Java](https://img.shields.io/badge/Java-21-brightgreen)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-blue)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.3.0-orange)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/license-MIT-green)](#license)

## Descrição
Delivery é uma aplicação backend em **Java 21** usando **Spring Boot 3.5.4**, criada para gerenciar pedidos, lojas e integrações de pagamento (Mercado Pago). O projeto inclui autenticação JWT, segurança Spring Security, cache distribuído com Hazelcast, rate limiting com Bucket4j e documentação de API via Swagger.

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.4**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
  - Spring Actuator
  - Spring HATEOAS
  - Spring Cache
  - Spring Mail
- **Hibernate / JPA**
- **MySQL 8**
- **JWT (JSON Web Token)**
- **Mercado Pago SDK**
- **Hazelcast (Cache distribuído)**
- **Bucket4j (Rate Limiting)**
- **Swagger / Springdoc OpenAPI**

---

## Pré-requisitos

- Java 21
- Maven 4+
- MySQL 8+
- Variáveis de ambiente para JWT e API Token:

```bash
export JWT_SECRET=<sua_chave_secreta>
export API_TOKEN=<seu_api_token>
```

---

## Configuração

Arquivo `application.properties`:

```properties
spring.application.name=Delivery

spring.datasource.url=jdbc:mysql://localhost:3306/delivery
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

spring.web.resources.static-locations=classpath:/static/,file:./uploads/

api.token=${API_TOKEN}
jwt.access.token.expiration=3600000
jwt.refresh.token.expiration=604800000
jwt.secret=${JWT_SECRET}

springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.packages-to-scan=com.tizo.delivery.controller
```

---

## Como Rodar Localmente

1. Clone o repositório:

```bash
git clone https://github.com/Athirson-Pequeno/delivery-backend.git
cd Delivery
```

2. Configure as variáveis de ambiente (`JWT_SECRET` e `API_TOKEN`)  

3. Execute com Maven:

```bash
mvn clean install
mvn spring-boot:run
```

4. Acesse a API via Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## Estrutura do Projeto

```
Delivery/
│
├─ src/main/java/com/tizo/delivery/
│  ├─ controller/        # Controllers REST
│  ├─ service/           # Serviços de negócio
│  ├─ repository/        # Repositórios JPA
│  ├─ model/             # Entidades e DTOs
│  ├─ security/          # Configurações de segurança JWT
│  └─ config/            # Configurações do Spring
│
├─ src/main/resources/
│  ├─ application.properties
│  └─ static/            # Arquivos estáticos
```

---

## Endpoints Principais

### Autenticação
- `POST /auth/login` – Login e geração de JWT
- `POST /auth/refresh` – Refresh token

### Pedidos
- `GET /orders` – Listar pedidos
- `POST /orders` – Criar pedido
- `GET /orders/{id}` – Detalhes do pedido
- `PUT /orders/{id}` – Atualizar pedido
- `DELETE /orders/{id}` – Deletar pedido

### Lojas
- `GET /stores` – Listar lojas
- `POST /stores` – Criar loja
- `GET /stores/{id}` – Detalhes da loja

### Pagamentos
- `POST /payments/mercadopago` – Integração com Mercado Pago

> Para todos os detalhes, use Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Exemplos de Request/Response

**Login**

```json
POST /auth/login
{
  "username": "usuario",
  "password": "senha123"
}

Response:
{
  "accessToken": "<jwt_access_token>",
  "refreshToken": "<jwt_refresh_token>"
}
```

**Criar Pedido**

```json
POST /orders
{
  "storeId": "123",
  "items": [
    {"productId": "1", "quantity": 2},
    {"productId": "3", "quantity": 1}
  ]
}

Response:
{
  "id": "456",
  "status": "PENDING",
  "total": 150.0
}
```
