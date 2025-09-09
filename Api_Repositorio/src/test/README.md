# Pruebas de Base de Datos - Api_Repositorio

Este directorio contiene las pruebas unitarias y de integración para la conexión a la base de datos del proyecto.

## 📁 Estructura de Archivos

```
src/test/
├── java/hn/cus/api_repositorio/
│   ├── DatabaseConnectionTest.java      # Pruebas básicas de conexión (H2)
│   ├── DatabaseIntegrationTest.java     # Pruebas de integración (PostgreSQL)
│   ├── JpaEntityTest.java              # Pruebas de entidades JPA
│   └── RepositoryTest.java             # Pruebas de repositorios
├── resources/
│   ├── application-test.yml             # Configuración para pruebas unitarias
│   └── application-integration-test.yml # Configuración para pruebas de integración
└── README.md                            # Este archivo
```

## 🚀 Ejecutar Pruebas

### Pruebas Unitarias (H2 en memoria)
```bash
# Ejecutar todas las pruebas unitarias
mvn test -Dspring.profiles.active=test

# Ejecutar solo pruebas de conexión
mvn test -Dtest=DatabaseConnectionTest

# Ejecutar solo pruebas de entidades JPA
mvn test -Dtest=JpaEntityTest

# Ejecutar solo pruebas de repositorios
mvn test -Dtest=RepositoryTest
```

### Pruebas de Integración (PostgreSQL real)
```bash
# Ejecutar pruebas de integración con PostgreSQL
mvn test -Dtest.postgres=true -Dspring.profiles.active=default

# Ejecutar solo pruebas de integración
mvn test -Dtest=DatabaseIntegrationTest -Dtest.postgres=true
```

## 🔧 Configuración

### application-test.yml (H2)
- Base de datos en memoria para pruebas rápidas
- Esquema se crea y destruye automáticamente
- No requiere base de datos externa

### application-integration-test.yml (PostgreSQL)
- Conecta a PostgreSQL real usando TestContainers
- Requiere Docker para ejecutar contenedor PostgreSQL
- Prueba la configuración real de producción

## 📊 Tipos de Pruebas

### 1. DatabaseConnectionTest
- ✅ Verificación de DataSource
- ✅ Conexiones múltiples
- ✅ Validación de metadatos
- ✅ Soporte de transacciones
- ✅ Estructura de esquema
- ✅ Claves primarias y foráneas
- ✅ Índices y restricciones

### 2. DatabaseIntegrationTest
- ✅ Conexión a PostgreSQL real
- ✅ Validación de esquema `gestdoc_ow`
- ✅ Estructura de tablas
- ✅ Restricciones y claves
- ✅ Inserción de datos
- ✅ Rendimiento de consultas
- ✅ Pool de conexiones

### 3. JpaEntityTest
- ✅ Mapeo de entidades a tablas
- ✅ Relaciones entre entidades
- ✅ Persistencia de datos
- ✅ Generación de IDs
- ✅ Validaciones JPA

### 4. RepositoryTest
- ✅ Operaciones CRUD básicas
- ✅ Búsquedas por criterios
- ✅ Relaciones entre entidades
- ✅ Transacciones
- ✅ Eliminación y actualización

## 🐳 Requisitos para Pruebas de Integración

### Docker
```bash
# Verificar que Docker esté ejecutándose
docker --version
docker ps
```

### PostgreSQL (opcional)
Si prefieres usar tu base de datos PostgreSQL local:
```bash
# Modificar application-integration-test.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gestor_documental
    username: postgres
    password: tu_password
```

## 🔍 Interpretar Resultados

### Pruebas Exitosas
```
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Errores Comunes
1. **Conexión rechazada**: Verificar que PostgreSQL esté ejecutándose
2. **Esquema no encontrado**: Verificar que `gestdoc_ow` exista
3. **Credenciales incorrectas**: Verificar usuario/password en configuración
4. **Docker no disponible**: Para pruebas de integración con TestContainers

## 📈 Métricas de Rendimiento

Las pruebas incluyen validaciones de rendimiento:
- Consultas deben completarse en < 1 segundo
- Conexiones múltiples simultáneas
- Pool de conexiones funcional

## 🛠️ Solución de Problemas

### Error: "No qualifying bean of type 'DataSource'"
```bash
# Verificar que el perfil de testing esté activo
mvn test -Dspring.profiles.active=test
```

### Error: "Connection refused"
```bash
# Verificar que PostgreSQL esté ejecutándose
pg_isready -h localhost -p 5432
```

### Error: "Schema 'gestdoc_ow' does not exist"
```sql
-- Crear esquema en PostgreSQL
CREATE SCHEMA IF NOT EXISTS gestdoc_ow;
```

## 📝 Notas de Desarrollo

- Las pruebas unitarias usan H2 para velocidad
- Las pruebas de integración usan PostgreSQL real
- Todas las pruebas son transaccionales y se revierten
- Los datos de prueba se limpian automáticamente
- Las pruebas verifican tanto funcionalidad como rendimiento
