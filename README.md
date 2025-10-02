# 📄 Gestor Documental

Sistema completo de gestión de documentos con autenticación JWT, autorización basada en roles y permisos, y almacenamiento flexible (Base64 y físico).

## 🚀 Características Principales

### 🔐 **Sistema de Autenticación y Autorización**
- **Autenticación JWT** con tokens seguros
- **Autorización basada en roles y permisos** granular
- **Gestión de usuarios** con asignación de roles
- **Gestión de roles** con permisos específicos

### 📁 **Gestión de Documentos**
- **Subida de documentos** con validación de tipos
- **Almacenamiento dual**: Base64 y archivos físicos
- **Visualización de documentos** en modal
- **Descarga de documentos** con autenticación
- **Eliminación de documentos** con confirmación
- **Historial completo** de documentos subidos

### 💬 **Sistema de Comentarios**
- **Comentarios en documentos** por usuarios autenticados
- **Historial de comentarios** con timestamps
- **Identificación de autor** de comentarios

### 🎨 **Interfaz de Usuario**
- **Diseño responsive** con Bootstrap 5 y AdminLTE
- **Interfaz moderna** y fácil de usar
- **Feedback visual** con mensajes de éxito/error
- **Navegación intuitiva** con sidebar

## 🏗️ Arquitectura del Sistema

### **Backend (Spring Boot)**
- **Framework**: Spring Boot 3.2.0
- **Base de datos**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Seguridad**: Spring Security + JWT
- **API**: RESTful con Swagger/OpenAPI
- **Puerto**: 8081

### **Frontend (HTML/JavaScript)**
- **Framework CSS**: Bootstrap 5 + AdminLTE 3.2
- **JavaScript**: Vanilla JS con clases modulares
- **Servidor**: Python HTTP Server
- **Puerto**: 8080

### **Base de Datos**
- **SGBD**: PostgreSQL
- **Base de datos**: `gestor_documental`
- **Schema**: `gestdoc_ow`
- **Inicialización automática** de datos por defecto

## 📋 Requisitos del Sistema

### **Software Requerido**
- **Java**: JDK 17 o superior
- **Maven**: 3.6 o superior
- **PostgreSQL**: 12 o superior
- **Python**: 3.7 o superior (para servidor frontend)

### **Dependencias Backend**
- Spring Boot 3.2.0
- Spring Security 6.1.1
- Spring Data JPA
- PostgreSQL Driver
- JWT (jsonwebtoken)
- Lombok
- Swagger/OpenAPI 3

## 🚀 Instalación y Configuración

### **1. Configuración de Base de Datos**

```sql
-- Crear base de datos
CREATE DATABASE gestor_documental;

-- Crear usuario (opcional)
CREATE USER gestdoc_user WITH PASSWORD 'gestdoc_password';

-- Conceder permisos
GRANT ALL PRIVILEGES ON DATABASE gestor_documental TO gestdoc_user;
```

### **2. Configuración del Backend**

```bash
# Navegar al directorio del backend
cd Api_Repositorio

# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn spring-boot:run
```

### **3. Configuración del Frontend**

```bash
# Navegar al directorio del frontend
cd gestor-documental-Front

# Iniciar servidor HTTP
python -m http.server 8080
```

### **4. Acceso al Sistema**

- **Frontend**: http://localhost:8080
- **Backend API**: http://localhost:8081
- **Swagger UI**: http://localhost:8081/swagger-ui.html

## 👥 Usuarios por Defecto

| Usuario | Contraseña | Rol | Permisos |
|---------|------------|-----|----------|
| `admin` | `admin123` | ADMIN | Todos los permisos |
| `user` | `admin123` | USER | Ver y descargar documentos |

## 🔑 Sistema de Permisos

### **Permisos Disponibles**
- `UPLOAD_DOCUMENT` - Subir documentos
- `VIEW_DOCUMENT` - Ver documentos
- `DOWNLOAD_DOCUMENT` - Descargar documentos
- `DELETE_DOCUMENT` - Eliminar documentos
- `MANAGE_USERS` - Gestionar usuarios
- `MANAGE_ROLES` - Gestionar roles
- `VIEW_AUDIT` - Ver auditoría
- `MANAGE_WORKFLOW` - Gestionar workflow

### **Roles por Defecto**
- **ADMIN**: Todos los permisos
- **USER**: Solo ver y descargar documentos

## 📁 Estructura del Proyecto

```
Proyecto-PPS-main/
├── Api_Repositorio/                 # Backend Spring Boot
│   ├── src/main/java/
│   │   └── hn/cus/api_repositorio/
│   │       ├── controller/          # Controladores REST
│   │       ├── service/            # Lógica de negocio
│   │       ├── repository/         # Acceso a datos
│   │       ├── entity/            # Entidades JPA
│   │       ├── dto/               # Objetos de transferencia
│   │       ├── security/          # Configuración JWT
│   │       └── config/            # Configuraciones
│   └── src/main/resources/
│       └── application.yml        # Configuración
├── gestor-documental-Front/        # Frontend
│   ├── adminlte/                  # Páginas HTML
│   ├── js/                        # Scripts JavaScript
│   └── assets/                    # Recursos estáticos
└── README.md                      # Este archivo
```

## 🔧 Configuración Avanzada

### **Variables de Entorno**
```bash
# Backend
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=gestor_documental
export DB_USER=gestdoc_ow
export DB_PASSWORD=gestdoc_password
export JWT_SECRET=tu_jwt_secret_aqui
```

### **Configuración de Archivos**
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gestor_documental
    username: gestdoc_ow
    password: gestdoc_password
  jpa:
    hibernate:
      ddl-auto: update
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

file:
  upload-dir: ./uploads
```

## 🧪 Testing

### **Endpoints de Prueba**
- **Health Check**: `GET /api/documentos/health`
- **Test Backend**: `GET /api/documentos/test`
- **Test Usuario**: `GET /api/usuarios/test`

### **Scripts de Prueba**
```bash
# Probar conectividad
curl http://localhost:8081/api/documentos/health

# Probar autenticación
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## 📚 API Documentation

La documentación completa de la API está disponible en Swagger UI:
- **URL**: http://localhost:8081/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8081/v3/api-docs

## 🛠️ Desarrollo

### **Agregar Nuevos Permisos**
1. Agregar permiso en `DataInitializer.java`
2. Actualizar `AuthorizationService.java`
3. Agregar método en `AuthorizationManager.js`
4. Implementar verificación en el frontend

### **Agregar Nuevos Roles**
1. Crear rol en `DataInitializer.java`
2. Asignar permisos específicos
3. Actualizar interfaz de gestión de roles

## 🐛 Troubleshooting

### **Problemas Comunes**

#### **Error de conexión a base de datos**
```bash
# Verificar que PostgreSQL esté ejecutándose
sudo systemctl status postgresql

# Verificar conexión
psql -h localhost -U gestdoc_ow -d gestor_documental
```

#### **Error de puerto en uso**
```bash
# Cambiar puerto en application.yml
server:
  port: 8082
```

#### **Problemas de caché en frontend**
```bash
# Limpiar caché del navegador
Ctrl + F5 (Windows/Linux)
Cmd + Shift + R (Mac)
```

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Contribuidores

- **Desarrollador Principal**: [Tu Nombre]
- **Versión**: 1.0.0
- **Fecha**: Enero 2025

## 📞 Soporte

Para soporte técnico o preguntas sobre el proyecto:
- **Email**: [tu-email@ejemplo.com]
- **Issues**: [GitHub Issues URL]

---

**¡Gracias por usar el Gestor Documental!** 🎉
