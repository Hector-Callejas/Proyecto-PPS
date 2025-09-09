# 🚀 Instrucciones de Implementación - Sistema Gestor Documental

## ✅ **LO QUE YA ESTÁ IMPLEMENTADO**

### **Frontend (gestor-documental-Front)**
- ✅ Sistema de autenticación completo con JWT
- ✅ Login funcional con validación
- ✅ Dashboard con navegación
- ✅ Subida de documentos con validación de archivos
- ✅ Historial de documentos con filtros
- ✅ Visualización de documentos en modal
- ✅ Manejo de errores y notificaciones
- ✅ Interfaz responsive y moderna

### **Backend (Api_Repositorio)**
- ✅ API REST completa con Spring Boot
- ✅ Autenticación JWT implementada
- ✅ Endpoints para documentos (CRUD)
- ✅ Endpoints para autenticación
- ✅ Configuración de seguridad
- ✅ Base de datos PostgreSQL configurada
- ✅ Swagger/OpenAPI documentación
- ✅ Usuarios de prueba creados

## 🔧 **PASOS PARA EJECUTAR EL SISTEMA**

### **1. Configurar Base de Datos PostgreSQL**
```sql
-- Crear base de datos
CREATE DATABASE gestor_documental;

-- Crear esquema
CREATE SCHEMA gestdoc_ow;
```

### **2. Ejecutar Backend**
```bash
cd Api_Repositorio
mvn clean install
mvn spring-boot:run
```

El backend estará disponible en: `http://localhost:8081`

### **3. Ejecutar Frontend**
```bash
cd gestor-documental-Front
# Abrir index.html en un navegador web
# O usar un servidor local como Live Server
```

### **4. Credenciales de Prueba**
- **Usuario Admin**: `admin` / `admin123`
- **Usuario Regular**: `user` / `admin123`

## 📋 **FUNCIONALIDADES DISPONIBLES**

### **Autenticación**
- Login con JWT
- Logout automático
- Protección de rutas
- Validación de sesión

### **Gestión de Documentos**
- Subir archivos (PDF, imágenes, documentos)
- Validación de tipos de archivo
- Límite de tamaño (10MB)
- Vista previa de PDFs
- Historial con filtros
- Eliminación de documentos
- Visualización en modal

### **Interfaz de Usuario**
- Dashboard principal
- Navegación entre secciones
- Mensajes de éxito/error
- Loading states
- Confirmaciones de acciones

## 🔗 **ENDPOINTS DE LA API**

### **Autenticación**
- `POST /api/auth/login` - Login de usuario

### **Documentos**
- `GET /api/documentos` - Listar documentos
- `GET /api/documentos/{id}` - Obtener documento por ID
- `POST /api/documentos/subir` - Subir documento (FormData)
- `POST /api/documentos/uploadBase64` - Subir documento (Base64)
- `DELETE /api/documentos/{id}` - Eliminar documento

### **Documentación**
- `GET /swagger-ui.html` - Interfaz Swagger
- `GET /v3/api-docs` - Documentación JSON

## 🐛 **SOLUCIÓN DE PROBLEMAS**

### **Error de Conexión**
- Verificar que PostgreSQL esté ejecutándose
- Verificar credenciales en `application.yml`
- Verificar que el puerto 8081 esté libre

### **Error de Autenticación**
- Verificar que los usuarios de prueba estén creados
- Verificar que el token JWT sea válido
- Limpiar localStorage del navegador

### **Error de CORS**
- Verificar configuración CORS en `SecurityConfig.java`
- Verificar que el frontend use el puerto correcto (8081)

## 🚀 **PRÓXIMOS PASOS SUGERIDOS**

### **Mejoras Inmediatas**
1. **Implementar descarga de documentos**
2. **Agregar sistema de roles y permisos**
3. **Implementar búsqueda avanzada**
4. **Agregar sistema de comentarios**

### **Mejoras Futuras**
1. **Sistema de workflows**
2. **Notificaciones en tiempo real**
3. **Integración con servicios cloud**
4. **Aplicación móvil**

## 📞 **SOPORTE**

Si encuentras algún problema:
1. Verificar logs del backend en la consola
2. Verificar consola del navegador (F12)
3. Verificar que todos los archivos estén en su lugar
4. Verificar que las dependencias estén instaladas

---

**¡El sistema está listo para usar!** 🎉
