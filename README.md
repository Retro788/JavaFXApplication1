# DentalClinicManagement

Sistema de gestión para clínicas dentales con soporte para múltiples roles de usuario.

## Requisitos

- Java 8 o superior
- MySQL 5.7 o superior

## Dependencias

El proyecto requiere las siguientes bibliotecas:

- JavaFX (incluido en Java 8)
- MySQL JDBC Driver 5.1.5
- HikariCP 4.0.3 (pool de conexiones)
- jBCrypt 0.4 (para hashing de contraseñas)
- SLF4J 1.7.30 (para logging)

## Configuración

1. Crear la base de datos:
   ```sql
   CREATE DATABASE dentalcare;
   USE dentalcare;
   ```

2. Ejecutar el script SQL para crear la tabla de usuarios:
   ```sql
   CREATE TABLE users (
     id INT AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(50) UNIQUE NOT NULL,
     password_hash VARCHAR(60) NOT NULL,
     role ENUM('Practicante','Paciente','Docente','Operator','Dentist') NOT NULL
   );
   ```

3. Configurar las propiedades de conexión en `src/javafxapplication1/util/config.properties`

## Estructura del Proyecto

```
src/javafxapplication1/
├─ util/         ← ConnectionUtil, ConfigLoader, HashingUtil
├─ model/        ← POJOs de entidades (Patient, Practitioner, Publication…)
├─ dao/          ← Interfaces y DAOs (PatientDAO, PublicationDAO…)
├─ service/      ← Lógica de negocio (UserService, PublicationService…)
├─ controller/   ← Controladores JavaFX (LoginController, AddPatientController…)
└─ view/         ← FXMLs y CSS
```

## Roles de Usuario

- **Practicante**: Puede gestionar pacientes, citas y tratamientos
- **Paciente**: Puede ver y solicitar citas
- **Docente**: Acceso completo al sistema
- **Operator**: Puede gestionar pacientes, citas y facturas
- **Dentist**: Puede gestionar pacientes, citas, tratamientos y facturas