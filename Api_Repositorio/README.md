# 🔧 Backend - Gestor Documental API

API REST desarrollada con Spring Boot para la gestión de documentos, usuarios, roles y permisos.

## 🏗️ Arquitectura

### **Stack Tecnológico**
- **Framework**: Spring Boot 3.2.0
- **Base de Datos**: PostgreSQL 12+
- **ORM**: JPA/Hibernate
- **Seguridad**: Spring Security 6.1.1 + JWT
- **Documentación**: Swagger/OpenAPI 3
- **Build Tool**: Maven 3.6+

### **Patrón de Arquitectura**
```
Controller → Service → Repository → Entity
    ↓           ↓          ↓         ↓
   REST      Business    Data    Database
  Layer       Logic     Access    Layer
```

## 📁 Estructura del Proyecto

```
src/main/java/hn/cus/api_repositorio/
├── controller/           # Controladores REST
│   ├── AuthController.java
│   ├── DocumentoController.java
│   ├── UsuarioController.java
│   ├── RolController.java
│   ├── ComentarioController.java
│   └── AuthorizationController.java
├── service/             # Lógica de negocio
│   ├── impl/
│   │   ├── AuthServiceImpl.java
│   │   ├── DocumentoServiceImpl.java
│   │   ├── UsuarioServiceImpl.java
│   │   ├── RolServiceImpl.java
│   │   └── DocumentoComentarioServiceImpl.java
│   ├── AuthService.java
│   ├── DocumentoService.java
│   ├── UsuarioService.java
│   ├── RolService.java
│   ├── DocumentoComentarioService.java
│   └── AuthorizationService.java
├── repository/          # Acceso a datos
│   ├── UsuarioRepository.java
│   ├── DocumentoRepository.java
│   ├── RolRepository.java
│   ├── PermisoRepository.java
│   └── DocumentoComentarioRepository.java
├── entity/             # Entidades JPA
│   ├── Usuario.java
│   ├── Documento.java
│   ├── Rol.java
│   ├── Permiso.java
│   └── DocumentoComentario.java
├── dto/               # Objetos de transferencia
│   ├── UsuarioDTO.java
│   ├── DocumentoDTO.java
│   ├── RolDTO.java
│   ├── RolResponseDTO.java
│   ├── ComentarioRequestDTO.java
│   └── DocumentoComentarioDTO.java
├── security/          # Configuración de seguridad
│   ├── JwtUtil.java
│   └── JwtAuthenticationFilter.java
├── config/            # Configuraciones
│   ├── SecurityConfig.java
│   ├── DataInitializer.java
│   └── FileStorageConfig.java
└── exception/         # Manejo de excepciones
    └── GlobalExceptionHandler.java
```

## 🔐 Sistema de Autenticación y Autorización

### **Flujo de Autenticación JWT**
```
1. Login → 2. Validar Credenciales → 3. Generar JWT → 4. Retornar Token
```

### **Flujo de Autorización**
```
1. Request → 2. Extraer JWT → 3. Validar Token → 4. Verificar Permisos → 5. Permitir/Denegar
```

### **Configuración de Seguridad**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Configuración de endpoints públicos
    // Configuración de filtros JWT
    // Configuración de CORS
}
```

## 📊 Modelo de Datos

### **Entidades Principales**

#### **Usuario**
```java
@Entity
public class Usuario {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean activo;
    private Set<Rol> roles;
}
```

#### **Rol**
```java
@Entity
public class Rol {
    private Long id;
    private String nombre;
    private String descripcion;
    private Set<Permiso> permisos;
}
```

#### **Permiso**
```java
@Entity
public class Permiso {
    private Long id;
    private String nombre;
    private String descripcion;
}
```

#### **Documento**
```java
@Entity
public class Documento {
    private Long id;
    private String nombreOriginal;
    private String tipoMime;
    private String rutaArchivo;
    private String contenidoBase64;
    private Boolean publico;
    private LocalDateTime fechaCreacion;
}
```

## 🚀 Endpoints de la API

### **Autenticación**
```
POST /api/auth/login          # Iniciar sesión
GET  /api/auth/permissions    # Obtener permisos del usuario
GET  /api/auth/check-permission/{permission}  # Verificar permiso específico
```

### **Usuarios**
```
GET    /api/usuarios          # Listar usuarios
GET    /api/usuarios/{id}     # Obtener usuario
POST   /api/usuarios          # Crear usuario
PUT    /api/usuarios/{id}     # Actualizar usuario
DELETE /api/usuarios/{id}     # Eliminar usuario
```

### **Roles**
```
GET    /api/roles             # Listar roles
GET    /api/roles/{id}        # Obtener rol
POST   /api/roles             # Crear rol
PUT    /api/roles/{id}        # Actualizar rol
```

### **Documentos**
```
GET    /api/documentos                    # Listar documentos
GET    /api/documentos/{id}               # Obtener documento
GET    /api/documentos/{id}/download      # Descargar documento
GET    /api/documentos/{id}/content       # Obtener contenido Base64
POST   /api/documentos/subir              # Subir documento
DELETE /api/documentos/{id}               # Eliminar documento
```

### **Comentarios**
```
GET    /api/comentarios/documento/{id}    # Listar comentarios
POST   /api/comentarios                   # Agregar comentario
```

## ⚙️ Configuración

### **application.yml**
```yaml
spring:
  application:
    name: gestor-documental-api
  
  datasource:
    url: jdbc:postgresql://localhost:5432/gestor_documental
    username: gestdoc_ow
    password: gestdoc_password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

server:
  port: 8081

file:
  upload-dir: ./uploads

jwt:
  secret: mi-jwt-secret-super-seguro
  expiration: 18000000  # 5 horas en milisegundos
```

## 🏃‍♂️ Ejecución

### **Requisitos**
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### **Comandos**
```bash
# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run

# Ejecutar con perfil específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Crear JAR ejecutable
mvn clean package
java -jar target/Api_Repositorio-1.0.0.jar
```

### **Variables de Entorno**
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=gestor_documental
export DB_USER=gestdoc_ow
export DB_PASSWORD=gestdoc_password
export JWT_SECRET=tu_jwt_secret_aqui
```

## 🧪 Testing

### **Endpoints de Prueba**
```bash
# Health Check
curl http://localhost:8081/api/documentos/health

# Test Backend
curl http://localhost:8081/api/documentos/test

# Login
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### **Scripts de Prueba**
```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests de integración
mvn verify
```

## 📚 Documentación de la API

### **Swagger UI**
- **URL**: http://localhost:8081/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8081/v3/api-docs

### **Ejemplo de Request/Response**

#### **Login**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "roles": ["ADMIN"]
}
```

#### **Subir Documento**
```http
POST /api/documentos/subir
Content-Type: multipart/form-data
Authorization: Bearer <token>

file: [archivo]
usuarioId: 1
```

## 🔒 Seguridad

### **JWT Configuration**
```java
@Configuration
public class JwtConfig {
    private String secret = "mi-jwt-secret-super-seguro";
    private int expiration = 18000000; // 5 horas
}
```

### **CORS Configuration**
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    return source;
}
```

## 📈 Monitoreo y Logs

### **Logging Configuration**
```yaml
logging:
  level:
    hn.cus.api_repositorio: DEBUG
    org.springframework.security: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### **Health Checks**
```java
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    // Verificación de salud de la base de datos
}
```

## 🚀 Despliegue

### **Docker**
```dockerfile
FROM openjdk:17-jre-slim
COPY target/Api_Repositorio-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### **Docker Compose**
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8081:8081"
    environment:
      - DB_HOST=postgres
      - DB_NAME=gestor_documental
    depends_on:
      - postgres
  
  postgres:
    image: postgres:12
    environment:
      - POSTGRES_DB=gestor_documental
      - POSTGRES_USER=gestdoc_ow
      - POSTGRES_PASSWORD=gestdoc_password
    ports:
      - "5432:5432"
```

## 🐛 Troubleshooting

### **Problemas Comunes**

#### **Error de conexión a base de datos**
```bash
# Verificar que PostgreSQL esté ejecutándose
sudo systemctl status postgresql

# Verificar configuración de red
telnet localhost 5432
```

#### **Error de memoria insuficiente**
```bash
# Aumentar memoria para Maven
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=256m"

# Aumentar memoria para la aplicación
java -Xmx1024m -jar target/Api_Repositorio-1.0.0.jar
```

#### **Error de puerto en uso**
```bash
# Verificar procesos usando el puerto
netstat -tulpn | grep :8081

# Cambiar puerto en application.yml
server:
  port: 8082
```

## 📝 Changelog

### **v1.0.0** (Enero 2025)
- ✅ Sistema de autenticación JWT
- ✅ Autorización basada en roles y permisos
- ✅ CRUD completo de usuarios y roles
- ✅ Gestión de documentos con almacenamiento dual
- ✅ Sistema de comentarios
- ✅ API REST documentada con Swagger
- ✅ Interfaz web completa

## 👥 Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver `LICENSE` para más detalles.

---

**Desarrollado con ❤️ usando Spring Boot**
