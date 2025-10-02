# 🚀 Guía de Instalación Rápida - Gestor Documental

Guía paso a paso para instalar y configurar el sistema de gestión de documentos en tu entorno local.

## ⚡ Instalación Rápida (5 minutos)

### **Paso 1: Verificar Requisitos**
```bash
# Verificar Java
java -version  # Debe ser 17+

# Verificar Maven
mvn -version  # Debe ser 3.6+

# Verificar PostgreSQL
psql --version  # Debe ser 12+

# Verificar Python
python --version  # Debe ser 3.7+
```

### **Paso 2: Configurar Base de Datos**
```sql
-- Conectar a PostgreSQL como superusuario
psql -U postgres

-- Crear base de datos
CREATE DATABASE gestor_documental;

-- Crear usuario
CREATE USER gestdoc_ow WITH PASSWORD 'gestdoc_password';

-- Conceder permisos
GRANT ALL PRIVILEGES ON DATABASE gestor_documental TO gestdoc_ow;

-- Salir
\q
```

### **Paso 3: Ejecutar Backend**
```bash
# Navegar al directorio del backend
cd Api_Repositorio

# Compilar y ejecutar
mvn clean spring-boot:run
```

### **Paso 4: Ejecutar Frontend**
```bash
# En una nueva terminal, navegar al frontend
cd gestor-documental-Front

# Iniciar servidor HTTP
python -m http.server 8080
```

### **Paso 5: Acceder al Sistema**
- **Frontend**: http://localhost:8080
- **Backend**: http://localhost:8081
- **Swagger**: http://localhost:8081/swagger-ui.html

## 🔑 Credenciales por Defecto

| Usuario | Contraseña | Rol | Permisos |
|---------|------------|-----|----------|
| `admin` | `admin123` | ADMIN | Todos los permisos |
| `user` | `admin123` | USER | Ver y descargar documentos |

## 📋 Scripts de Ejecución Automática

### **Windows (run-project.bat)**
```batch
@echo off
echo Iniciando Gestor Documental...

echo.
echo 1. Iniciando Backend...
start "Backend" cmd /k "cd Api_Repositorio && mvn spring-boot:run"

echo.
echo 2. Esperando 10 segundos para que el backend inicie...
timeout /t 10

echo.
echo 3. Iniciando Frontend...
start "Frontend" cmd /k "cd gestor-documental-Front && python -m http.server 8080"

echo.
echo 4. Abriendo navegador...
timeout /t 5
start http://localhost:8080

echo.
echo ✅ Proyecto iniciado correctamente!
echo Backend: http://localhost:8081
echo Frontend: http://localhost:8080
echo Swagger: http://localhost:8081/swagger-ui.html
pause
```

### **Linux/Mac (run-project.sh)**
```bash
#!/bin/bash
echo "Iniciando Gestor Documental..."

echo "1. Iniciando Backend..."
cd Api_Repositorio
mvn spring-boot:run &
BACKEND_PID=$!

echo "2. Esperando 10 segundos para que el backend inicie..."
sleep 10

echo "3. Iniciando Frontend..."
cd ../gestor-documental-Front
python -m http.server 8080 &
FRONTEND_PID=$!

echo "4. Abriendo navegador..."
sleep 5
xdg-open http://localhost:8080 2>/dev/null || open http://localhost:8080 2>/dev/null

echo "✅ Proyecto iniciado correctamente!"
echo "Backend: http://localhost:8081"
echo "Frontend: http://localhost:8080"
echo "Swagger: http://localhost:8081/swagger-ui.html"
echo "Presiona Ctrl+C para detener los servicios"

# Función para limpiar procesos al salir
cleanup() {
    echo "Deteniendo servicios..."
    kill $BACKEND_PID 2>/dev/null
    kill $FRONTEND_PID 2>/dev/null
    exit
}

trap cleanup SIGINT SIGTERM

# Esperar indefinidamente
wait
```

## 🐳 Instalación con Docker

### **docker-compose.yml**
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:12
    environment:
      POSTGRES_DB: gestor_documental
      POSTGRES_USER: gestdoc_ow
      POSTGRES_PASSWORD: gestdoc_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  backend:
    build: ./Api_Repositorio
    ports:
      - "8081:8081"
    environment:
      - DB_HOST=postgres
      - DB_PORT=5432
      - DB_NAME=gestor_documental
      - DB_USER=gestdoc_ow
      - DB_PASSWORD=gestdoc_password
    depends_on:
      - postgres

  frontend:
    image: nginx:alpine
    ports:
      - "8080:80"
    volumes:
      - ./gestor-documental-Front:/usr/share/nginx/html
    depends_on:
      - backend

volumes:
  postgres_data:
```

### **Dockerfile para Backend**
```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app

COPY target/Api_Repositorio-1.0.0.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **Comandos Docker**
```bash
# Construir y ejecutar
docker-compose up --build

# Ejecutar en background
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener
docker-compose down

# Limpiar volúmenes
docker-compose down -v
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
export JWT_SECRET=tu_jwt_secret_super_seguro
export SERVER_PORT=8081

# Frontend
export API_BASE_URL=http://localhost:8081
```

### **Configuración de Base de Datos**
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:gestor_documental}
    username: ${DB_USER:gestdoc_ow}
    password: ${DB_PASSWORD:gestdoc_password}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true

server:
  port: ${SERVER_PORT:8081}

file:
  upload-dir: ${FILE_UPLOAD_DIR:./uploads}

jwt:
  secret: ${JWT_SECRET:mi-jwt-secret-super-seguro}
  expiration: ${JWT_EXPIRATION:18000000}
```

## 🧪 Testing de Instalación

### **Script de Verificación**
```bash
#!/bin/bash
echo "🧪 Verificando instalación..."

# Verificar backend
echo "1. Verificando backend..."
if curl -s http://localhost:8081/api/documentos/health > /dev/null; then
    echo "✅ Backend funcionando correctamente"
else
    echo "❌ Backend no responde"
    exit 1
fi

# Verificar frontend
echo "2. Verificando frontend..."
if curl -s http://localhost:8080 > /dev/null; then
    echo "✅ Frontend funcionando correctamente"
else
    echo "❌ Frontend no responde"
    exit 1
fi

# Verificar autenticación
echo "3. Verificando autenticación..."
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"admin123"}' | \
    grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ ! -z "$TOKEN" ]; then
    echo "✅ Autenticación funcionando correctamente"
else
    echo "❌ Error en autenticación"
    exit 1
fi

echo "🎉 ¡Instalación verificada correctamente!"
```

### **Pruebas Manuales**
```bash
# 1. Probar health check
curl http://localhost:8081/api/documentos/health

# 2. Probar login
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 3. Probar listado de usuarios (requiere token)
curl -H "Authorization: Bearer TU_TOKEN_AQUI" \
  http://localhost:8081/api/usuarios

# 4. Verificar Swagger
# Abrir http://localhost:8081/swagger-ui.html en navegador
```

## 🐛 Troubleshooting

### **Problemas Comunes**

#### **Error: Puerto 8081 en uso**
```bash
# Verificar proceso usando el puerto
netstat -tulpn | grep :8081
# o en Windows
netstat -ano | findstr :8081

# Cambiar puerto en application.yml
server:
  port: 8082
```

#### **Error: Base de datos no existe**
```sql
-- Crear base de datos
CREATE DATABASE gestor_documental;

-- Verificar conexión
psql -h localhost -U gestdoc_ow -d gestor_documental
```

#### **Error: Maven no encontrado**
```bash
# Instalar Maven en Ubuntu/Debian
sudo apt update
sudo apt install maven

# Instalar Maven en CentOS/RHEL
sudo yum install maven

# Verificar instalación
mvn -version
```

#### **Error: Java no encontrado**
```bash
# Instalar OpenJDK 17
sudo apt install openjdk-17-jdk

# Configurar JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$PATH:$JAVA_HOME/bin

# Verificar instalación
java -version
```

#### **Error: PostgreSQL no responde**
```bash
# Iniciar PostgreSQL
sudo systemctl start postgresql

# Verificar estado
sudo systemctl status postgresql

# Verificar conexión
psql -h localhost -U postgres
```

### **Logs de Debug**
```bash
# Ver logs del backend
tail -f Api_Repositorio/logs/application.log

# Ver logs de PostgreSQL
sudo tail -f /var/log/postgresql/postgresql-12-main.log

# Ver logs del frontend (en consola del navegador)
# F12 → Console
```

## 📊 Monitoreo

### **Health Checks**
```bash
# Backend health
curl http://localhost:8081/api/documentos/health

# Database health
psql -h localhost -U gestdoc_ow -d gestor_documental -c "SELECT 1;"

# Frontend health
curl http://localhost:8080
```

### **Métricas de Sistema**
```bash
# CPU y memoria
top

# Espacio en disco
df -h

# Conexiones de red
netstat -tulpn | grep :8081
netstat -tulpn | grep :8080
```

## 🔄 Actualización

### **Backup Antes de Actualizar**
```bash
# Backup de base de datos
pg_dump -h localhost -U gestdoc_ow -d gestor_documental > backup_$(date +%Y%m%d).sql

# Backup de archivos subidos
tar -czf uploads_backup_$(date +%Y%m%d).tar.gz Api_Repositorio/uploads/
```

### **Proceso de Actualización**
```bash
# 1. Detener servicios
docker-compose down  # Si usa Docker
# o
pkill -f "spring-boot:run"
pkill -f "http.server"

# 2. Actualizar código
git pull origin main

# 3. Recompilar backend
cd Api_Repositorio
mvn clean package

# 4. Reiniciar servicios
# Seguir pasos de instalación
```

## 📞 Soporte

### **Recursos de Ayuda**
- **Documentación**: README.md
- **API Docs**: http://localhost:8081/swagger-ui.html
- **Logs**: Ver archivos de log en el directorio del proyecto
- **Issues**: Crear issue en el repositorio del proyecto

### **Información del Sistema**
```bash
# Información completa del sistema
echo "=== SISTEMA ==="
uname -a
echo "=== JAVA ==="
java -version
echo "=== MAVEN ==="
mvn -version
echo "=== POSTGRESQL ==="
psql --version
echo "=== PYTHON ==="
python --version
echo "=== DOCKER ==="
docker --version
docker-compose --version
```

---

**¡Instalación completada! 🎉**

El sistema está listo para usar. Accede a http://localhost:8080 para comenzar.
