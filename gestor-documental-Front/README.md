# 🎨 Frontend - Gestor Documental Web

Interfaz web moderna desarrollada con HTML5, CSS3, JavaScript vanilla y Bootstrap 5 para la gestión de documentos.

## 🏗️ Arquitectura

### **Stack Tecnológico**
- **HTML5**: Estructura semántica
- **CSS3**: Estilos y diseño responsive
- **JavaScript**: Vanilla JS con clases modulares
- **Bootstrap 5**: Framework CSS y componentes
- **AdminLTE 3.2**: Template administrativo
- **jQuery 3.6**: Manipulación del DOM
- **Fetch API**: Comunicación con backend

### **Patrón de Arquitectura**
```
HTML Pages → JavaScript Managers → API Calls → Backend
    ↓              ↓                ↓           ↓
  UI Layer    Business Logic    HTTP Layer   REST API
```

## 📁 Estructura del Proyecto

```
gestor-documental-Front/
├── adminlte/                    # Páginas HTML
│   ├── login.html              # Página de inicio de sesión
│   ├── dashboard.html          # Panel principal
│   ├── historial_documentos.html  # Gestión de documentos
│   ├── subida_documentos.html  # Subida de documentos
│   ├── comentarios_documentos.html  # Comentarios
│   ├── gestion_usuarios.html   # Gestión de usuarios
│   ├── gestion_roles.html      # Gestión de roles
│   ├── css/                    # Estilos AdminLTE
│   ├── js/                     # Scripts AdminLTE
│   └── plugins/                # Plugins (Bootstrap, jQuery)
├── js/                         # Scripts personalizados
│   ├── auth.js                # Gestión de autenticación
│   ├── document-manager.js     # Gestión de documentos
│   ├── user-manager.js         # Gestión de usuarios
│   ├── role-manager.js         # Gestión de roles
│   ├── comment-manager.js      # Gestión de comentarios
│   ├── authorization-manager.js # Gestión de autorización
│   └── ui-manager.js           # Utilidades de UI
├── assets/                     # Recursos estáticos
│   └── logo.png               # Logo de la aplicación
└── index.html                  # Página principal
```

## 🎯 Funcionalidades

### **🔐 Autenticación**
- **Login seguro** con JWT
- **Gestión de tokens** automática
- **Logout** con limpieza de sesión
- **Redirección automática** según autenticación

### **📄 Gestión de Documentos**
- **Listado de documentos** con filtros
- **Subida de documentos** con drag & drop
- **Visualización** en modal
- **Descarga** de documentos
- **Eliminación** con confirmación
- **Comentarios** en documentos

### **👥 Gestión de Usuarios**
- **CRUD completo** de usuarios
- **Asignación de roles**
- **Validación de formularios**
- **Búsqueda y filtros**

### **🔑 Gestión de Roles**
- **Creación y edición** de roles
- **Asignación de permisos**
- **Validación de permisos**
- **Gestión granular**

### **💬 Sistema de Comentarios**
- **Comentarios por documento**
- **Historial de comentarios**
- **Identificación de autor**
- **Timestamps automáticos**

## 🧩 Módulos JavaScript

### **AuthManager**
```javascript
class AuthManager {
    static login(username, password)           // Iniciar sesión
    static logout()                           // Cerrar sesión
    static isAuthenticated()                  // Verificar autenticación
    static getToken()                         // Obtener token
    static authenticatedFetch(url, options)   // Fetch autenticado
}
```

### **DocumentManager**
```javascript
class DocumentManager {
    static uploadDocument(file, usuarioId)    // Subir documento
    static getDocuments()                     // Listar documentos
    static getDocument(id)                    // Obtener documento
    static downloadDocument(id)               // Descargar documento
    static deleteDocument(id)                 // Eliminar documento
    static getDocumentContent(id)             // Obtener contenido Base64
}
```

### **UserManager**
```javascript
class UserManager {
    static getUsers()                         // Listar usuarios
    static getUser(id)                        // Obtener usuario
    static createUser(userData)               // Crear usuario
    static updateUser(id, userData)           // Actualizar usuario
    static deleteUser(id)                     // Eliminar usuario
    static getRoles()                         // Obtener roles disponibles
}
```

### **RoleManager**
```javascript
class RoleManager {
    static getRoles()                         // Listar roles
    static getRole(id)                        // Obtener rol
    static createRole(roleData)               // Crear rol
    static updateRole(id, roleData)           // Actualizar rol
}
```

### **CommentManager**
```javascript
class CommentManager {
    static getComments(documentId)            // Listar comentarios
    static addComment(commentData)            // Agregar comentario
}
```

### **AuthorizationManager**
```javascript
class AuthorizationManager {
    static initialize()                       // Inicializar permisos
    static hasPermission(permission)          // Verificar permiso
    static canManageUsers()                   // Verificar gestión usuarios
    static canManageRoles()                   // Verificar gestión roles
    static canViewDocuments()                 // Verificar ver documentos
    static canDownloadDocuments()             // Verificar descarga
}
```

### **UIManager**
```javascript
class UIManager {
    static showSuccess(message, container)    // Mostrar éxito
    static showError(message, container)      // Mostrar error
    static showLoading(message, container)    // Mostrar carga
    static hideLoading(container)             // Ocultar carga
}
```

## 🎨 Diseño y UX

### **Framework CSS**
- **Bootstrap 5.1.3**: Grid system, componentes, utilidades
- **AdminLTE 3.2**: Template administrativo profesional
- **Responsive Design**: Adaptable a móviles y tablets

### **Componentes Principales**
- **Sidebar**: Navegación principal
- **Header**: Barra superior con logout
- **Content**: Área principal de contenido
- **Modals**: Ventanas emergentes para formularios
- **Tables**: Tablas de datos con paginación
- **Forms**: Formularios con validación

### **Paleta de Colores**
```css
:root {
  --primary-color: #007bff;      /* Azul primario */
  --success-color: #28a745;      /* Verde éxito */
  --danger-color: #dc3545;       /* Rojo error */
  --warning-color: #ffc107;      /* Amarillo advertencia */
  --info-color: #17a2b8;         /* Azul información */
  --light-color: #f8f9fa;        /* Gris claro */
  --dark-color: #343a40;         /* Gris oscuro */
}
```

## 🚀 Configuración y Ejecución

### **Requisitos**
- **Navegador moderno**: Chrome 90+, Firefox 88+, Safari 14+
- **Servidor HTTP**: Python 3.7+ o servidor web
- **Backend**: API REST ejecutándose en puerto 8081

### **Ejecución Local**
```bash
# Navegar al directorio del frontend
cd gestor-documental-Front

# Iniciar servidor HTTP con Python
python -m http.server 8080

# O con Python 2
python -m SimpleHTTPServer 8080

# Acceder a la aplicación
# http://localhost:8080
```

### **Configuración de API**
```javascript
// En cada manager
static API_BASE_URL = 'http://localhost:8081';
```

## 🔧 Configuración Avanzada

### **Variables de Configuración**
```javascript
// Configuración global
const CONFIG = {
    API_BASE_URL: 'http://localhost:8081',
    TOKEN_KEY: 'gestor_documental_token',
    USER_KEY: 'gestor_documental_user',
    TIMEOUT: 30000,  // 30 segundos
    MAX_FILE_SIZE: 10 * 1024 * 1024,  // 10MB
    ALLOWED_FILE_TYPES: [
        'application/pdf',
        'image/jpeg',
        'image/png',
        'text/plain',
        'application/msword',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
    ]
};
```

### **Manejo de Errores**
```javascript
// Interceptor global de errores
class ErrorHandler {
    static handle(error) {
        console.error('Error:', error);
        
        if (error.status === 401) {
            AuthManager.logout();
            window.location.href = '../index.html';
        } else if (error.status === 403) {
            UIManager.showError('No tienes permisos para realizar esta acción');
        } else if (error.status >= 500) {
            UIManager.showError('Error del servidor. Intenta más tarde.');
        }
    }
}
```

## 📱 Responsive Design

### **Breakpoints**
```css
/* Extra small devices (portrait phones, less than 576px) */
@media (max-width: 575.98px) { }

/* Small devices (landscape phones, 576px and up) */
@media (min-width: 576px) { }

/* Medium devices (tablets, 768px and up) */
@media (min-width: 768px) { }

/* Large devices (desktops, 992px and up) */
@media (min-width: 992px) { }

/* Extra large devices (large desktops, 1200px and up) */
@media (min-width: 1200px) { }
```

### **Adaptaciones Móviles**
- **Sidebar colapsable** en dispositivos pequeños
- **Tablas responsive** con scroll horizontal
- **Modales fullscreen** en móviles
- **Botones táctiles** optimizados

## 🧪 Testing

### **Testing Manual**
```javascript
// Función de prueba para validar funcionalidades
function runTests() {
    console.log('🧪 Iniciando tests...');
    
    // Test de autenticación
    console.log('✅ AuthManager disponible:', typeof AuthManager);
    
    // Test de gestión de documentos
    console.log('✅ DocumentManager disponible:', typeof DocumentManager);
    
    // Test de UI
    console.log('✅ UIManager disponible:', typeof UIManager);
    
    console.log('🎉 Tests completados');
}

// Ejecutar tests en consola
runTests();
```

### **Validación de Formularios**
```javascript
// Validación de formulario de usuario
function validateUserForm(formData) {
    const errors = [];
    
    if (!formData.username || formData.username.length < 3) {
        errors.push('Username debe tener al menos 3 caracteres');
    }
    
    if (!formData.email || !isValidEmail(formData.email)) {
        errors.push('Email inválido');
    }
    
    if (!formData.password || formData.password.length < 6) {
        errors.push('Password debe tener al menos 6 caracteres');
    }
    
    return errors;
}
```

## 🔒 Seguridad

### **Protección XSS**
```javascript
// Sanitización de entrada
function sanitizeInput(input) {
    const div = document.createElement('div');
    div.textContent = input;
    return div.innerHTML;
}
```

### **Validación de Archivos**
```javascript
// Validación de archivos subidos
function validateFile(file) {
    const maxSize = 10 * 1024 * 1024; // 10MB
    const allowedTypes = [
        'application/pdf',
        'image/jpeg',
        'image/png',
        'text/plain'
    ];
    
    if (file.size > maxSize) {
        throw new Error('Archivo demasiado grande');
    }
    
    if (!allowedTypes.includes(file.type)) {
        throw new Error('Tipo de archivo no permitido');
    }
    
    return true;
}
```

## 📊 Performance

### **Optimizaciones**
- **Lazy loading** de imágenes
- **Debounce** en búsquedas
- **Caché** de datos frecuentes
- **Compresión** de assets

### **Métricas**
```javascript
// Medición de performance
class PerformanceMonitor {
    static measureTime(label, fn) {
        const start = performance.now();
        const result = fn();
        const end = performance.now();
        console.log(`${label}: ${end - start}ms`);
        return result;
    }
}
```

## 🐛 Troubleshooting

### **Problemas Comunes**

#### **Error de CORS**
```javascript
// Verificar configuración CORS en backend
fetch('http://localhost:8081/api/test')
    .then(response => console.log('CORS OK'))
    .catch(error => console.error('CORS Error:', error));
```

#### **Error de autenticación**
```javascript
// Verificar token
const token = localStorage.getItem('gestor_documental_token');
console.log('Token:', token ? 'Presente' : 'Ausente');

// Verificar expiración
const payload = JSON.parse(atob(token.split('.')[1]));
console.log('Expira:', new Date(payload.exp * 1000));
```

#### **Error de carga de recursos**
```javascript
// Verificar carga de scripts
console.log('jQuery:', typeof $);
console.log('Bootstrap:', typeof bootstrap);
console.log('AuthManager:', typeof AuthManager);
```

## 📈 Monitoreo

### **Analytics**
```javascript
// Tracking de eventos
function trackEvent(category, action, label) {
    console.log(`📊 Event: ${category} - ${action} - ${label}`);
    // Integrar con Google Analytics o similar
}
```

### **Error Logging**
```javascript
// Log de errores
window.addEventListener('error', function(e) {
    console.error('Global Error:', e.error);
    // Enviar a servicio de logging
});
```

## 🚀 Despliegue

### **Build para Producción**
```bash
# Minificar JavaScript
npx uglifyjs js/*.js -o js/app.min.js

# Optimizar imágenes
npx imagemin assets/*.png --out-dir=assets/optimized

# Comprimir archivos
tar -czf frontend.tar.gz .
```

### **Servidor Web**
```nginx
# Configuración Nginx
server {
    listen 80;
    server_name gestor-documental.com;
    root /var/www/gestor-documental-front;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8081;
    }
}
```

## 📝 Changelog

### **v1.0.0** (Enero 2025)
- ✅ Interfaz completa con AdminLTE
- ✅ Sistema de autenticación JWT
- ✅ Gestión completa de documentos
- ✅ CRUD de usuarios y roles
- ✅ Sistema de comentarios
- ✅ Autorización basada en permisos
- ✅ Diseño responsive
- ✅ Validación de formularios

## 👥 Contribución

1. Fork el proyecto
2. Crear una rama para tu feature
3. Implementar cambios con tests
4. Crear Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT.

---

**Desarrollado con ❤️ usando tecnologías web modernas**
