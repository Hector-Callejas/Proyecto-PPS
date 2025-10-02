# 🗄️ Base de Datos - Gestor Documental

Documentación completa del esquema de base de datos PostgreSQL para el sistema de gestión de documentos.

## 🏗️ Arquitectura de Base de Datos

### **SGBD**
- **PostgreSQL 12+**
- **Base de datos**: `gestor_documental`
- **Schema**: `gestdoc_ow`
- **Encoding**: UTF-8
- **Collation**: C

### **Características**
- **ACID Compliance**: Transacciones seguras
- **JSON Support**: Campos JSON nativos
- **Full Text Search**: Búsqueda en texto completo
- **Indexing**: Índices optimizados
- **Constraints**: Restricciones de integridad

## 📊 Modelo de Datos

### **Diagrama ER**
```
Usuario (1) ←→ (N) Usuario_Rol ←→ (N) Rol
Usuario (1) ←→ (N) Documento
Usuario (1) ←→ (N) DocumentoComentario
Documento (1) ←→ (N) DocumentoComentario
Documento (1) ←→ (N) DocumentoVersion
Documento (1) ←→ (N) DocumentoWorkflow
Rol (1) ←→ (N) Rol_Permiso ←→ (N) Permiso
Workflow (1) ←→ (N) WorkflowEtapa
DocumentoWorkflow (N) ←→ (1) Workflow
DocumentoWorkflow (N) ←→ (1) WorkflowEtapa
Usuario (1) ←→ (N) AuditLog
```

## 🗂️ Esquema de Tablas

### **👤 Tabla: usuario**
```sql
CREATE TABLE gestdoc_ow.usuario (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_usuario_username ON gestdoc_ow.usuario(username);
CREATE INDEX idx_usuario_email ON gestdoc_ow.usuario(email);
CREATE INDEX idx_usuario_activo ON gestdoc_ow.usuario(activo);
```

### **🔑 Tabla: rol**
```sql
CREATE TABLE gestdoc_ow.rol (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_rol_nombre ON gestdoc_ow.rol(nombre);
```

### **🔐 Tabla: permiso**
```sql
CREATE TABLE gestdoc_ow.permiso (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_permiso_nombre ON gestdoc_ow.permiso(nombre);
```

### **👥 Tabla: usuario_rol**
```sql
CREATE TABLE gestdoc_ow.usuario_rol (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES gestdoc_ow.usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES gestdoc_ow.rol(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_usuario_rol_usuario ON gestdoc_ow.usuario_rol(usuario_id);
CREATE INDEX idx_usuario_rol_rol ON gestdoc_ow.usuario_rol(rol_id);
```

### **🔗 Tabla: rol_permiso**
```sql
CREATE TABLE gestdoc_ow.rol_permiso (
    rol_id BIGINT NOT NULL,
    permiso_id BIGINT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (rol_id, permiso_id),
    FOREIGN KEY (rol_id) REFERENCES gestdoc_ow.rol(id) ON DELETE CASCADE,
    FOREIGN KEY (permiso_id) REFERENCES gestdoc_ow.permiso(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_rol_permiso_rol ON gestdoc_ow.rol_permiso(rol_id);
CREATE INDEX idx_rol_permiso_permiso ON gestdoc_ow.rol_permiso(permiso_id);
```

### **📄 Tabla: documento**
```sql
CREATE TABLE gestdoc_ow.documento (
    id BIGSERIAL PRIMARY KEY,
    nombre_original VARCHAR(255) NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    tipo_mime VARCHAR(100) NOT NULL,
    tamaño_archivo BIGINT NOT NULL,
    ruta_archivo TEXT,
    contenido_base64 TEXT,
    hash_archivo VARCHAR(64),
    publico BOOLEAN DEFAULT FALSE,
    usuario_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES gestdoc_ow.usuario(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_documento_usuario ON gestdoc_ow.documento(usuario_id);
CREATE INDEX idx_documento_publico ON gestdoc_ow.documento(publico);
CREATE INDEX idx_documento_fecha_creacion ON gestdoc_ow.documento(fecha_creacion);
CREATE INDEX idx_documento_tipo_mime ON gestdoc_ow.documento(tipo_mime);
CREATE INDEX idx_documento_nombre_original ON gestdoc_ow.documento(nombre_original);
CREATE INDEX idx_documento_hash ON gestdoc_ow.documento(hash_archivo);

-- Índice de texto completo
CREATE INDEX idx_documento_fulltext ON gestdoc_ow.documento 
USING gin(to_tsvector('spanish', nombre_original || ' ' || COALESCE(contenido_base64, '')));
```

### **💬 Tabla: documento_comentario**
```sql
CREATE TABLE gestdoc_ow.documento_comentario (
    id BIGSERIAL PRIMARY KEY,
    documento_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    comentario TEXT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (documento_id) REFERENCES gestdoc_ow.documento(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES gestdoc_ow.usuario(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_comentario_documento ON gestdoc_ow.documento_comentario(documento_id);
CREATE INDEX idx_comentario_usuario ON gestdoc_ow.documento_comentario(usuario_id);
CREATE INDEX idx_comentario_fecha ON gestdoc_ow.documento_comentario(fecha_creacion);
```

### **📋 Tabla: documento_version**
```sql
CREATE TABLE gestdoc_ow.documento_version (
    id BIGSERIAL PRIMARY KEY,
    documento_id BIGINT NOT NULL,
    numero_version INTEGER NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    tipo_mime VARCHAR(100) NOT NULL,
    tamaño_archivo BIGINT NOT NULL,
    ruta_archivo TEXT,
    contenido_base64 TEXT,
    hash_archivo VARCHAR(64),
    usuario_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (documento_id) REFERENCES gestdoc_ow.documento(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES gestdoc_ow.usuario(id) ON DELETE CASCADE,
    UNIQUE(documento_id, numero_version)
);

-- Índices
CREATE INDEX idx_version_documento ON gestdoc_ow.documento_version(documento_id);
CREATE INDEX idx_version_usuario ON gestdoc_ow.documento_version(usuario_id);
CREATE INDEX idx_version_numero ON gestdoc_ow.documento_version(documento_id, numero_version);
```

### **🔄 Tabla: workflow**
```sql
CREATE TABLE gestdoc_ow.workflow (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_workflow_nombre ON gestdoc_ow.workflow(nombre);
CREATE INDEX idx_workflow_activo ON gestdoc_ow.workflow(activo);
```

### **📝 Tabla: workflow_etapa**
```sql
CREATE TABLE gestdoc_ow.workflow_etapa (
    id BIGSERIAL PRIMARY KEY,
    workflow_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    orden INTEGER NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (workflow_id) REFERENCES gestdoc_ow.workflow(id) ON DELETE CASCADE,
    UNIQUE(workflow_id, orden)
);

-- Índices
CREATE INDEX idx_etapa_workflow ON gestdoc_ow.workflow_etapa(workflow_id);
CREATE INDEX idx_etapa_orden ON gestdoc_ow.workflow_etapa(workflow_id, orden);
```

### **🔗 Tabla: documento_workflow**
```sql
CREATE TABLE gestdoc_ow.documento_workflow (
    id BIGSERIAL PRIMARY KEY,
    documento_id BIGINT NOT NULL,
    workflow_id BIGINT NOT NULL,
    etapa_actual_id BIGINT,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_fin TIMESTAMP,
    usuario_asignado_id BIGINT,
    observaciones TEXT,
    FOREIGN KEY (documento_id) REFERENCES gestdoc_ow.documento(id) ON DELETE CASCADE,
    FOREIGN KEY (workflow_id) REFERENCES gestdoc_ow.workflow(id) ON DELETE CASCADE,
    FOREIGN KEY (etapa_actual_id) REFERENCES gestdoc_ow.workflow_etapa(id) ON DELETE SET NULL,
    FOREIGN KEY (usuario_asignado_id) REFERENCES gestdoc_ow.usuario(id) ON DELETE SET NULL
);

-- Índices
CREATE INDEX idx_doc_workflow_documento ON gestdoc_ow.documento_workflow(documento_id);
CREATE INDEX idx_doc_workflow_workflow ON gestdoc_ow.documento_workflow(workflow_id);
CREATE INDEX idx_doc_workflow_etapa ON gestdoc_ow.documento_workflow(etapa_actual_id);
CREATE INDEX idx_doc_workflow_usuario ON gestdoc_ow.documento_workflow(usuario_asignado_id);
CREATE INDEX idx_doc_workflow_estado ON gestdoc_ow.documento_workflow(estado);
```

### **📊 Tabla: audit_log**
```sql
CREATE TABLE gestdoc_ow.audit_log (
    id BIGSERIAL PRIMARY KEY,
    tabla_afectada VARCHAR(50) NOT NULL,
    registro_id BIGINT,
    accion VARCHAR(20) NOT NULL, -- INSERT, UPDATE, DELETE
    usuario_id BIGINT,
    datos_anteriores JSONB,
    datos_nuevos JSONB,
    ip_address INET,
    user_agent TEXT,
    fecha_accion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES gestdoc_ow.usuario(id) ON DELETE SET NULL
);

-- Índices
CREATE INDEX idx_audit_tabla ON gestdoc_ow.audit_log(tabla_afectada);
CREATE INDEX idx_audit_registro ON gestdoc_ow.audit_log(registro_id);
CREATE INDEX idx_audit_usuario ON gestdoc_ow.audit_log(usuario_id);
CREATE INDEX idx_audit_fecha ON gestdoc_ow.audit_log(fecha_accion);
CREATE INDEX idx_audit_accion ON gestdoc_ow.audit_log(accion);

-- Índice GIN para búsqueda en JSON
CREATE INDEX idx_audit_datos_anteriores ON gestdoc_ow.audit_log USING gin(datos_anteriores);
CREATE INDEX idx_audit_datos_nuevos ON gestdoc_ow.audit_log USING gin(datos_nuevos);
```

### **🏷️ Tabla: metadata**
```sql
CREATE TABLE gestdoc_ow.metadata (
    id BIGSERIAL PRIMARY KEY,
    documento_id BIGINT NOT NULL,
    clave VARCHAR(100) NOT NULL,
    valor TEXT,
    tipo_dato VARCHAR(20) DEFAULT 'TEXT', -- TEXT, NUMBER, DATE, BOOLEAN, JSON
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (documento_id) REFERENCES gestdoc_ow.documento(id) ON DELETE CASCADE,
    UNIQUE(documento_id, clave)
);

-- Índices
CREATE INDEX idx_metadata_documento ON gestdoc_ow.metadata(documento_id);
CREATE INDEX idx_metadata_clave ON gestdoc_ow.metadata(clave);
CREATE INDEX idx_metadata_valor ON gestdoc_ow.metadata(valor);
```

## 🚀 Script de Creación Completo

```sql
-- Crear base de datos
CREATE DATABASE gestor_documental
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Conectar a la base de datos
\c gestor_documental;

-- Crear schema
CREATE SCHEMA IF NOT EXISTS gestdoc_ow;
SET search_path TO gestdoc_ow;

-- [Aquí van todas las tablas del esquema anterior]

-- Crear usuario específico para la aplicación
CREATE USER gestdoc_ow WITH PASSWORD 'gestdoc_password';
GRANT ALL PRIVILEGES ON SCHEMA gestdoc_ow TO gestdoc_ow;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA gestdoc_ow TO gestdoc_ow;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA gestdoc_ow TO gestdoc_ow;

-- Configurar permisos por defecto
ALTER DEFAULT PRIVILEGES IN SCHEMA gestdoc_ow 
    GRANT ALL ON TABLES TO gestdoc_ow;
ALTER DEFAULT PRIVILEGES IN SCHEMA gestdoc_ow 
    GRANT ALL ON SEQUENCES TO gestdoc_ow;
```

## 📊 Datos Iniciales

### **Permisos por Defecto**
```sql
INSERT INTO gestdoc_ow.permiso (nombre, descripcion) VALUES
('UPLOAD_DOCUMENT', 'Subir documentos al sistema'),
('VIEW_DOCUMENT', 'Ver documentos existentes'),
('DOWNLOAD_DOCUMENT', 'Descargar documentos'),
('DELETE_DOCUMENT', 'Eliminar documentos'),
('MANAGE_USERS', 'Gestionar usuarios del sistema'),
('MANAGE_ROLES', 'Gestionar roles y permisos'),
('VIEW_AUDIT', 'Ver logs de auditoría'),
('MANAGE_WORKFLOW', 'Gestionar workflows de documentos');
```

### **Roles por Defecto**
```sql
INSERT INTO gestdoc_ow.rol (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema con todos los permisos'),
('USER', 'Usuario básico con permisos limitados'),
('User3', 'Usuario nivel 3 con permisos de documentos');
```

### **Asignación de Permisos a Roles**
```sql
-- ADMIN: Todos los permisos
INSERT INTO gestdoc_ow.rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM gestdoc_ow.rol r, gestdoc_ow.permiso p
WHERE r.nombre = 'ADMIN';

-- USER: Solo ver y descargar
INSERT INTO gestdoc_ow.rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM gestdoc_ow.rol r, gestdoc_ow.permiso p
WHERE r.nombre = 'USER' 
AND p.nombre IN ('VIEW_DOCUMENT', 'DOWNLOAD_DOCUMENT');

-- User3: Permisos de documentos
INSERT INTO gestdoc_ow.rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM gestdoc_ow.rol r, gestdoc_ow.permiso p
WHERE r.nombre = 'User3' 
AND p.nombre IN ('UPLOAD_DOCUMENT', 'VIEW_DOCUMENT', 'DOWNLOAD_DOCUMENT', 'DELETE_DOCUMENT');
```

### **Usuarios por Defecto**
```sql
-- Usuario admin
INSERT INTO gestdoc_ow.usuario (username, email, password, activo) VALUES
('admin', 'admin@gestor.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVqVJhKqQKvJhKqQKvJhKqQKvJ', true);

-- Usuario regular
INSERT INTO gestdoc_ow.usuario (username, email, password, activo) VALUES
('user', 'user@gestor.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVqVJhKqQKvJhKqQKvJhKqQKvJ', true);

-- Asignar roles
INSERT INTO gestdoc_ow.usuario_rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM gestdoc_ow.usuario u, gestdoc_ow.rol r
WHERE u.username = 'admin' AND r.nombre = 'ADMIN';

INSERT INTO gestdoc_ow.usuario_rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM gestdoc_ow.usuario u, gestdoc_ow.rol r
WHERE u.username = 'user' AND r.nombre = 'USER';
```

## 🔍 Consultas Útiles

### **Verificar Permisos de Usuario**
```sql
SELECT u.username, r.nombre as rol, p.nombre as permiso
FROM gestdoc_ow.usuario u
JOIN gestdoc_ow.usuario_rol ur ON u.id = ur.usuario_id
JOIN gestdoc_ow.rol r ON ur.rol_id = r.id
JOIN gestdoc_ow.rol_permiso rp ON r.id = rp.rol_id
JOIN gestdoc_ow.permiso p ON rp.permiso_id = p.id
WHERE u.username = 'admin'
ORDER BY u.username, r.nombre, p.nombre;
```

### **Documentos por Usuario**
```sql
SELECT u.username, COUNT(d.id) as total_documentos, 
       SUM(d.tamaño_archivo) as tamaño_total
FROM gestdoc_ow.usuario u
LEFT JOIN gestdoc_ow.documento d ON u.id = d.usuario_id
GROUP BY u.id, u.username
ORDER BY total_documentos DESC;
```

### **Auditoría de Acciones**
```sql
SELECT al.tabla_afectada, al.accion, u.username, 
       al.fecha_accion, al.ip_address
FROM gestdoc_ow.audit_log al
LEFT JOIN gestdoc_ow.usuario u ON al.usuario_id = u.id
ORDER BY al.fecha_accion DESC
LIMIT 100;
```

### **Búsqueda de Documentos**
```sql
-- Búsqueda por texto completo
SELECT d.id, d.nombre_original, u.username, d.fecha_creacion
FROM gestdoc_ow.documento d
JOIN gestdoc_ow.usuario u ON d.usuario_id = u.id
WHERE to_tsvector('spanish', d.nombre_original || ' ' || COALESCE(d.contenido_base64, ''))
      @@ plainto_tsquery('spanish', 'término de búsqueda')
ORDER BY d.fecha_creacion DESC;
```

## 🔧 Mantenimiento

### **Limpieza de Datos**
```sql
-- Eliminar documentos huérfanos (sin usuario)
DELETE FROM gestdoc_ow.documento 
WHERE usuario_id NOT IN (SELECT id FROM gestdoc_ow.usuario);

-- Limpiar logs de auditoría antiguos
DELETE FROM gestdoc_ow.audit_log 
WHERE fecha_accion < CURRENT_TIMESTAMP - INTERVAL '1 year';

-- Reindexar para optimizar búsquedas
REINDEX INDEX gestdoc_ow.idx_documento_fulltext;
```

### **Backup y Restore**
```bash
# Backup completo
pg_dump -h localhost -U postgres -d gestor_documental > backup.sql

# Backup solo schema
pg_dump -h localhost -U postgres -d gestor_documental --schema-only > schema.sql

# Backup solo datos
pg_dump -h localhost -U postgres -d gestor_documental --data-only > data.sql

# Restore
psql -h localhost -U postgres -d gestor_documental < backup.sql
```

### **Monitoreo de Performance**
```sql
-- Consultas lentas
SELECT query, mean_time, calls, total_time
FROM pg_stat_statements
ORDER BY mean_time DESC
LIMIT 10;

-- Tamaño de tablas
SELECT schemaname, tablename, 
       pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables
WHERE schemaname = 'gestdoc_ow'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

## 🔒 Seguridad

### **Configuración de Seguridad**
```sql
-- Revocar permisos públicos
REVOKE ALL ON SCHEMA gestdoc_ow FROM PUBLIC;
REVOKE ALL ON ALL TABLES IN SCHEMA gestdoc_ow FROM PUBLIC;

-- Crear roles específicos
CREATE ROLE gestdoc_readonly;
CREATE ROLE gestdoc_readwrite;

-- Asignar permisos
GRANT USAGE ON SCHEMA gestdoc_ow TO gestdoc_readonly;
GRANT SELECT ON ALL TABLES IN SCHEMA gestdoc_ow TO gestdoc_readonly;

GRANT USAGE ON SCHEMA gestdoc_ow TO gestdoc_readwrite;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA gestdoc_ow TO gestdoc_readwrite;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA gestdoc_ow TO gestdoc_readwrite;
```

### **Encriptación**
```sql
-- Crear extensión para encriptación
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Función para hashear passwords
CREATE OR REPLACE FUNCTION hash_password(password TEXT)
RETURNS TEXT AS $$
BEGIN
    RETURN crypt(password, gen_salt('bf'));
END;
$$ LANGUAGE plpgsql;

-- Función para verificar passwords
CREATE OR REPLACE FUNCTION verify_password(password TEXT, hash TEXT)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN hash = crypt(password, hash);
END;
$$ LANGUAGE plpgsql;
```

## 📈 Optimización

### **Índices Adicionales**
```sql
-- Índice parcial para documentos activos
CREATE INDEX idx_documento_activo_usuario 
ON gestdoc_ow.documento(usuario_id) 
WHERE publico = true;

-- Índice para fechas recientes
CREATE INDEX idx_documento_reciente 
ON gestdoc_ow.documento(fecha_creacion) 
WHERE fecha_creacion > CURRENT_DATE - INTERVAL '30 days';

-- Índice compuesto para búsquedas frecuentes
CREATE INDEX idx_documento_busqueda 
ON gestdoc_ow.documento(usuario_id, tipo_mime, fecha_creacion);
```

### **Configuración de PostgreSQL**
```sql
-- Optimizaciones para el sistema
ALTER SYSTEM SET shared_buffers = '256MB';
ALTER SYSTEM SET effective_cache_size = '1GB';
ALTER SYSTEM SET maintenance_work_mem = '64MB';
ALTER SYSTEM SET checkpoint_completion_target = 0.9;
ALTER SYSTEM SET wal_buffers = '16MB';
ALTER SYSTEM SET default_statistics_target = 100;

-- Recargar configuración
SELECT pg_reload_conf();
```

---

**Base de datos optimizada para el Gestor Documental** 🗄️
