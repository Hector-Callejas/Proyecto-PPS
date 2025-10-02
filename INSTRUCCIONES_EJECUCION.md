# 🚀 Gestor Documental - Instrucciones de Ejecución

## 📋 Prerrequisitos

1. **Java 17+** instalado
2. **PostgreSQL** instalado y corriendo
3. **Python 3** instalado (para servidor frontend)
4. **Maven** instalado (opcional, se puede usar el wrapper)

## 🗄️ Configuración de Base de Datos

### Opción 1: Usar el script SQL
```bash
# Conectar a PostgreSQL
psql -U postgres

# Ejecutar el script
\i database.sql
```

### Opción 2: Crear manualmente
```sql
-- Conectar a PostgreSQL
psql -U postgres

-- Crear base de datos
CREATE DATABASE gestor_documental;

-- Conectar a la base de datos
\c gestor_documental;

-- Crear esquema
CREATE SCHEMA gestdoc_ow;
```

## 🚀 Ejecución Rápida

### Método 1: Scripts Automáticos (Recomendado)
```bash
# Ejecutar todo el proyecto
start-project.bat

# O ejecutar por separado:
run-backend.bat    # Solo backend
run-frontend.bat   # Solo frontend
```

### Método 2: Manual

#### Backend (Terminal 1):
```bash
cd Api_Repositorio
mvn spring-boot:run
```

#### Frontend (Terminal 2):
```bash
cd gestor-documental-Front
python -m http.server 8080
```

## 🌐 URLs de Acceso

- **Frontend**: http://localhost:8080
- **Backend API**: http://localhost:8081
- **Swagger UI**: http://localhost:8081/swagger-ui.html

## 👤 Usuarios de Prueba

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin   | admin123   | ADMIN |
| user    | admin123   | USER |

## 🔧 Verificación

```bash
# Verificar que todo funciona
test-endpoints.bat
```

## 📁 Estructura del Proyecto

```
Proyecto-PPS-main/
├── Api_Repositorio/          # Backend Spring Boot
│   ├── src/main/java/        # Código Java
│   ├── src/main/resources/   # Configuración
│   └── pom.xml              # Dependencias Maven
├── gestor-documental-Front/  # Frontend HTML/JS
│   ├── adminlte/            # Páginas HTML
│   ├── js/                  # JavaScript
│   └── index.html           # Página principal
├── database.sql             # Script de base de datos
└── *.bat                    # Scripts de ejecución
```

## 🐛 Solución de Problemas

### Error: "no existe la base de datos"
- Verificar que PostgreSQL esté corriendo
- Crear la base de datos `gestor_documental`
- Crear el esquema `gestdoc_ow`

### Error: "Error al cargar usuarios"
- Verificar que el backend esté corriendo en puerto 8081
- Verificar que la base de datos esté configurada
- Revisar la consola del navegador (F12)

### Error CORS
- Usar servidor HTTP local (no abrir archivos directamente)
- Ejecutar `python -m http.server 8080` en la carpeta frontend

## 📝 Notas Importantes

- El backend crea automáticamente los usuarios la primera vez que arranca
- Los archivos se suben a la carpeta `uploads/`
- El token JWT expira en 5 horas
- La aplicación usa el esquema `gestdoc_ow` en PostgreSQL
