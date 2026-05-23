# Phase 1 Bootstrap Platform Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the minimum runnable ClinicFlow backend platform: Gradle multi-module skeleton, Spring Boot app boot, Dockerized PostgreSQL with Flyway, and OpenAPI plus shared validation error handling.

**Architecture:** Use a modular monolith with one deployable Spring Boot application in `app/` and domain-oriented library modules in `modules/`. Keep the app runnable locally, keep PostgreSQL in Docker only, and establish API-first and migration-first foundations before any business feature work.

**Tech Stack:** Java 21, Gradle 8.14.2 with Kotlin DSL, Spring Boot 3.5.0, Spring Web, Spring Actuator, Spring Validation, Spring Data JDBC, Flyway, springdoc-openapi, PostgreSQL 16, Docker Compose

---

## Planned File Structure

**Create**
- `settings.gradle.kts`
- `build.gradle.kts`
- `gradle.properties`
- `gradle/libs.versions.toml`
- `app/build.gradle.kts`
- `app/src/main/java/com/clinicflow/app/ClinicFlowApplication.java`
- `app/src/main/java/com/clinicflow/app/api/SystemEchoController.java`
- `app/src/main/java/com/clinicflow/app/api/SystemEchoRequest.java`
- `app/src/main/java/com/clinicflow/app/api/ApiErrorHandler.java`
- `app/src/main/resources/application.yml`
- `app/src/main/resources/application-local.yml`
- `app/src/main/resources/db/migration/V1__bootstrap.sql`
- `app/src/test/java/com/clinicflow/app/ClinicFlowApplicationTests.java`
- `app/src/test/java/com/clinicflow/app/api/SystemEchoControllerTest.java`
- `modules/auth/build.gradle.kts`
- `modules/auth/src/main/java/com/clinicflow/auth/package-info.java`
- `modules/tenant/build.gradle.kts`
- `modules/tenant/src/main/java/com/clinicflow/tenant/package-info.java`
- `modules/provider/build.gradle.kts`
- `modules/provider/src/main/java/com/clinicflow/provider/package-info.java`
- `modules/patient/build.gradle.kts`
- `modules/patient/src/main/java/com/clinicflow/patient/package-info.java`
- `modules/appointment/build.gradle.kts`
- `modules/appointment/src/main/java/com/clinicflow/appointment/package-info.java`
- `modules/notification/build.gradle.kts`
- `modules/notification/src/main/java/com/clinicflow/notification/package-info.java`
- `compose.yaml`

**Modify**
- none

## Task 1: Slice 1.1 Gradle Multi-Module Skeleton

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle.properties`
- Create: `gradle/libs.versions.toml`
- Create: `app/build.gradle.kts`
- Create: `modules/*/build.gradle.kts`
- Create: `modules/*/src/main/java/com/clinicflow/*/package-info.java`

- [ ] **Step 1: Generate the Gradle wrapper**

Run:

```powershell
gradle wrapper --gradle-version 8.14.2
```

Expected: `gradlew`, `gradlew.bat`, and `gradle/wrapper/*` are created.

- [ ] **Step 2: Create `settings.gradle.kts`**

```kotlin
rootProject.name = "clinicflow"

include("app")
include("modules:auth")
include("modules:tenant")
include("modules:provider")
include("modules:patient")
include("modules:appointment")
include("modules:notification")
```

- [ ] **Step 3: Create the root `build.gradle.kts`**

```kotlin
plugins {
    id("org.springframework.boot") version "3.5.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    java
}

allprojects {
    group = "com.clinicflow"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
```

- [ ] **Step 4: Create `gradle.properties`**

```properties
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configuration-cache=true
```

- [ ] **Step 5: Create `gradle/libs.versions.toml`**

```toml
[versions]
spring-boot = "3.5.0"
springdoc = "2.8.9"
postgresql = "42.7.5"
flyway = "11.8.0"

[libraries]
spring-boot-starter-web = { module = "org.springframework.boot:spring-boot-starter-web" }
spring-boot-starter-actuator = { module = "org.springframework.boot:spring-boot-starter-actuator" }
spring-boot-starter-validation = { module = "org.springframework.boot:spring-boot-starter-validation" }
spring-boot-starter-test = { module = "org.springframework.boot:spring-boot-starter-test" }
spring-boot-starter-data-jdbc = { module = "org.springframework.boot:spring-boot-starter-data-jdbc" }
flyway-core = { module = "org.flywaydb:flyway-core", version.ref = "flyway" }
postgresql = { module = "org.postgresql:postgresql", version.ref = "postgresql" }
springdoc-openapi = { module = "org.springdoc:springdoc-openapi-starter-webmvc-ui", version.ref = "springdoc" }
```

- [ ] **Step 6: Create `app/build.gradle.kts`**

```kotlin
plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":modules:auth"))
    implementation(project(":modules:tenant"))
    implementation(project(":modules:provider"))
    implementation(project(":modules:patient"))
    implementation(project(":modules:appointment"))
    implementation(project(":modules:notification"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.data.jdbc)
    implementation(libs.flyway.core)
    runtimeOnly(libs.postgresql)

    testImplementation(libs.spring.boot.starter.test)
}
```

- [ ] **Step 7: Create one module `build.gradle.kts` and copy the same pattern to all six modules**

Use this content in each module build file:

```kotlin
plugins {
    id("java-library")
}

dependencies {
    testImplementation(libs.spring.boot.starter.test)
}
```

- [ ] **Step 8: Create `package-info.java` placeholders for each module**

Use the matching package in each file, for example `modules/auth/src/main/java/com/clinicflow/auth/package-info.java`:

```java
@org.springframework.lang.NonNullApi
package com.clinicflow.auth;
```

Use the same pattern for:
- `com.clinicflow.tenant`
- `com.clinicflow.provider`
- `com.clinicflow.patient`
- `com.clinicflow.appointment`
- `com.clinicflow.notification`

- [ ] **Step 9: Verify the module graph**

Run:

```powershell
.\gradlew.bat projects
```

Expected: `app` and all six `modules:*` projects are listed.

- [ ] **Step 10: Commit**

```bash
git add .
git commit -m "build: add gradle multi-module skeleton"
```

## Task 2: Slice 1.2 Spring Boot App Boot and Health

**Files:**
- Create: `app/src/main/java/com/clinicflow/app/ClinicFlowApplication.java`
- Create: `app/src/main/resources/application.yml`
- Create: `app/src/test/java/com/clinicflow/app/ClinicFlowApplicationTests.java`

- [ ] **Step 1: Write the failing boot test**

```java
package com.clinicflow.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClinicFlowApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

- [ ] **Step 2: Run the boot test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:test --tests "com.clinicflow.app.ClinicFlowApplicationTests"
```

Expected: FAIL because `ClinicFlowApplication` does not exist yet.

- [ ] **Step 3: Create the application entry point**

```java
package com.clinicflow.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.clinicflow")
public class ClinicFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicFlowApplication.class, args);
    }
}
```

- [ ] **Step 4: Create the base `application.yml`**

```yaml
spring:
  application:
    name: clinicflow

management:
  endpoints:
    web:
      exposure:
        include: health,info

server:
  port: 8080
```

- [ ] **Step 5: Run the boot test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:test --tests "com.clinicflow.app.ClinicFlowApplicationTests"
```

Expected: PASS

- [ ] **Step 6: Verify the app exposes health locally**

Run in terminal 1:

```powershell
.\gradlew.bat :app:bootRun
```

Run in terminal 2:

```powershell
Invoke-WebRequest http://localhost:8080/actuator/health | Select-Object -ExpandProperty Content
```

Expected:

```json
{"status":"UP"}
```

- [ ] **Step 7: Commit**

```bash
git add .
git commit -m "feat: boot clinicflow application"
```

## Task 3: Slice 1.3 Dockerized PostgreSQL and Flyway Baseline

**Files:**
- Create: `compose.yaml`
- Create: `app/src/main/resources/application-local.yml`
- Create: `app/src/main/resources/db/migration/V1__bootstrap.sql`
- Modify: `app/src/main/resources/application.yml`

- [ ] **Step 1: Create `compose.yaml` for PostgreSQL only**

```yaml
services:
  postgres:
    image: postgres:16
    container_name: clinicflow-postgres
    environment:
      POSTGRES_DB: clinicflow
      POSTGRES_USER: clinicflow
      POSTGRES_PASSWORD: clinicflow
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U clinicflow -d clinicflow"]
      interval: 10s
      timeout: 5s
      retries: 5
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

- [ ] **Step 2: Extend `application.yml` with Flyway and profile defaults**

Append:

```yaml
spring:
  profiles:
    default: local
  flyway:
    enabled: true
    locations: classpath:db/migration
```

- [ ] **Step 3: Create `application-local.yml`**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/clinicflow
    username: clinicflow
    password: clinicflow
  flyway:
    baseline-on-migrate: true
```

- [ ] **Step 4: Create the first migration**

```sql
create schema if not exists clinicflow;

create table if not exists clinicflow.schema_version_probe (
    id bigint primary key,
    description varchar(100) not null,
    created_at timestamp not null default current_timestamp
);
```

- [ ] **Step 5: Start PostgreSQL**

Run:

```powershell
docker compose up -d postgres
```

Expected: the `clinicflow-postgres` container is healthy.

- [ ] **Step 6: Boot the app against PostgreSQL and verify migration**

Run:

```powershell
.\gradlew.bat :app:bootRun
```

Expected log output includes:

```text
Successfully applied 1 migration
```

- [ ] **Step 7: Verify the probe table exists**

Run:

```powershell
docker exec -it clinicflow-postgres psql -U clinicflow -d clinicflow -c "\dt clinicflow.*"
```

Expected output includes `schema_version_probe`.

- [ ] **Step 8: Commit**

```bash
git add .
git commit -m "feat: add postgres and flyway baseline"
```

## Task 4: Slice 1.4 OpenAPI, Validation, and Shared Error Shape

**Files:**
- Modify: `app/build.gradle.kts`
- Create: `app/src/main/java/com/clinicflow/app/api/SystemEchoRequest.java`
- Create: `app/src/main/java/com/clinicflow/app/api/SystemEchoController.java`
- Create: `app/src/main/java/com/clinicflow/app/api/ApiErrorHandler.java`
- Create: `app/src/test/java/com/clinicflow/app/api/SystemEchoControllerTest.java`

- [ ] **Step 1: Add `springdoc-openapi` to `app/build.gradle.kts`**

Append inside `dependencies`:

```kotlin
implementation(libs.springdoc.openapi)
```

- [ ] **Step 2: Write the failing controller test**

```java
package com.clinicflow.app.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SystemEchoController.class)
class SystemEchoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void rejectsBlankMessageWithStandardErrorShape() throws Exception {
        mvc.perform(post("/api/v1/system/echo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"message":""}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.errors[0].field").value("message"));
    }
}
```

- [ ] **Step 3: Run the controller test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:test --tests "com.clinicflow.app.api.SystemEchoControllerTest"
```

Expected: FAIL because the controller, request DTO, and exception handler do not exist yet.

- [ ] **Step 4: Create `SystemEchoRequest.java`**

```java
package com.clinicflow.app.api;

import jakarta.validation.constraints.NotBlank;

public record SystemEchoRequest(
    @NotBlank(message = "message must not be blank")
    String message
) {
}
```

- [ ] **Step 5: Create `SystemEchoController.java`**

```java
package com.clinicflow.app.api;

import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system")
class SystemEchoController {

    @PostMapping("/echo")
    Map<String, String> echo(@Valid @RequestBody SystemEchoRequest request) {
        return Map.of("message", request.message());
    }
}
```

- [ ] **Step 6: Create `ApiErrorHandler.java`**

```java
package com.clinicflow.app.api;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> Map.of(
                "field", error.getField(),
                "message", error.getDefaultMessage() == null ? "invalid value" : error.getDefaultMessage()
            ))
            .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "code", "VALIDATION_ERROR",
            "message", "Request validation failed",
            "errors", errors
        ));
    }
}
```

- [ ] **Step 7: Run the controller test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:test --tests "com.clinicflow.app.api.SystemEchoControllerTest"
```

Expected: PASS

- [ ] **Step 8: Verify OpenAPI is exposed**

Run the app:

```powershell
.\gradlew.bat :app:bootRun
```

Then check:

```powershell
Invoke-WebRequest http://localhost:8080/v3/api-docs | Select-Object -ExpandProperty StatusCode
```

Expected:

```text
200
```

- [ ] **Step 9: Verify the standard error shape manually**

Run:

```powershell
Invoke-WebRequest http://localhost:8080/api/v1/system/echo `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"message":""}'
```

Expected response body includes:

```json
{"code":"VALIDATION_ERROR","message":"Request validation failed","errors":[{"field":"message","message":"message must not be blank"}]}
```

- [ ] **Step 10: Commit**

```bash
git add .
git commit -m "feat: add api baseline and validation errors"
```

## Task 5: Final Phase 1 Verification

**Files:**
- Modify: none

- [ ] **Step 1: Run the full test suite**

```powershell
.\gradlew.bat test
```

Expected: PASS

- [ ] **Step 2: Verify the project structure**

```powershell
.\gradlew.bat projects
```

Expected: `app` and all planned modules are listed.

- [ ] **Step 3: Verify local runtime against Dockerized PostgreSQL**

```powershell
docker compose up -d postgres
.\gradlew.bat :app:bootRun
```

Expected:
- app boots locally
- Flyway applies `V1__bootstrap.sql`
- `/actuator/health` returns `UP`
- `/v3/api-docs` returns `200`

- [ ] **Step 4: Commit any final cleanup**

```bash
git add .
git commit -m "build: complete phase 1 bootstrap verification"
```

