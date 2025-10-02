/**
 * Gestor de Autorización
 * Maneja la verificación de permisos en el frontend
 */

class AuthorizationManager {
    static API_BASE_URL = 'http://localhost:8081';
    static userPermissions = null;
    static userInfo = null;

    /**
     * Carga los permisos del usuario actual
     * @returns {Promise} - Promesa con los permisos del usuario
     */
    static async loadUserPermissions() {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/auth/permissions`
            );

            if (!response.ok) {
                throw new Error('Error al obtener permisos');
            }

            const data = await response.json();
            this.userPermissions = data.permissions;
            this.userInfo = data;

            console.log('Permisos cargados:', this.userPermissions);
            console.log('Información del usuario:', this.userInfo);

            return { success: true, data: data };

        } catch (error) {
            console.error('Error al cargar permisos:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Verifica si el usuario tiene un permiso específico
     * @param {string} permission - Nombre del permiso
     * @returns {boolean} - True si tiene el permiso
     */
    static hasPermission(permission) {
        if (!this.userPermissions) {
            console.warn('Permisos no cargados aún');
            return false;
        }
        return this.userPermissions.includes(permission);
    }

    /**
     * Verifica si el usuario puede gestionar roles
     * @returns {boolean} - True si puede gestionar roles
     */
    static canManageRoles() {
        return this.hasPermission('MANAGE_ROLES');
    }

    /**
     * Verifica si el usuario puede gestionar usuarios
     * @returns {boolean} - True si puede gestionar usuarios
     */
    static canManageUsers() {
        return this.hasPermission('MANAGE_USERS');
    }

    /**
     * Verifica si el usuario puede subir documentos
     * @returns {boolean} - True si puede subir documentos
     */
    static canUploadDocuments() {
        return this.hasPermission('UPLOAD_DOCUMENT');
    }

    /**
     * Verifica si el usuario puede ver documentos
     * @returns {boolean} - True si puede ver documentos
     */
    static canViewDocuments() {
        return this.hasPermission('VIEW_DOCUMENT');
    }

    /**
     * Verifica si el usuario puede descargar documentos
     * @returns {boolean} - True si puede descargar documentos
     */
    static canDownloadDocuments() {
        return this.hasPermission('DOWNLOAD_DOCUMENT');
    }

    /**
     * Verifica si el usuario puede eliminar documentos
     * @returns {boolean} - True si puede eliminar documentos
     */
    static canDeleteDocuments() {
        return this.hasPermission('DELETE_DOCUMENT');
    }

    /**
     * Oculta elementos de la interfaz según los permisos del usuario
     */
    static applyPermissionsToUI() {
        console.log('Aplicando permisos a la interfaz...');

        // Ocultar gestión de roles si no tiene permisos
        if (!this.canManageRoles()) {
            this.hideElement('gestion_roles_link');
            this.hideElement('roles_menu_item');
            console.log('Usuario sin permisos para gestionar roles - ocultando elementos');
        }

        // Ocultar gestión de usuarios si no tiene permisos
        if (!this.canManageUsers()) {
            this.hideElement('gestion_usuarios_link');
            this.hideElement('users_menu_item');
            console.log('Usuario sin permisos para gestionar usuarios - ocultando elementos');
        }

        // Ocultar botón de subir documentos si no tiene permisos
        if (!this.canUploadDocuments()) {
            this.hideElement('upload_document_btn');
            this.hideElement('subir_menu_item');
            console.log('Usuario sin permisos para subir documentos - ocultando elementos');
        }
    }

    /**
     * Oculta un elemento por ID
     * @param {string} elementId - ID del elemento a ocultar
     */
    static hideElement(elementId) {
        const element = document.getElementById(elementId);
        if (element) {
            element.style.display = 'none';
            console.log(`Elemento ${elementId} ocultado por falta de permisos`);
        } else {
            // Buscar por clase o selector alternativo
            const elementByClass = document.querySelector(`[data-permission="${elementId}"]`);
            if (elementByClass) {
                elementByClass.style.display = 'none';
                console.log(`Elemento con data-permission="${elementId}" ocultado por falta de permisos`);
            }
        }
    }

    /**
     * Muestra un mensaje de acceso denegado
     * @param {string} requiredPermission - Permiso requerido
     */
    static showAccessDenied(requiredPermission) {
        UIManager.showError(
            `Acceso denegado. Se requiere el permiso: ${requiredPermission}`,
            'messageContainer'
        );
    }

    /**
     * Verifica permisos antes de realizar una acción
     * @param {string} permission - Permiso requerido
     * @param {function} action - Acción a realizar si tiene permisos
     * @param {string} errorMessage - Mensaje de error personalizado
     */
    static requirePermission(permission, action, errorMessage = null) {
        if (this.hasPermission(permission)) {
            action();
        } else {
            const message = errorMessage || `No tienes permisos para realizar esta acción. Se requiere: ${permission}`;
            UIManager.showError(message, 'messageContainer');
            console.warn(`Acceso denegado - Permiso requerido: ${permission}`);
        }
    }

    /**
     * Inicializa el sistema de autorización
     * @returns {Promise} - Promesa que se resuelve cuando se cargan los permisos
     */
    static async initialize() {
        try {
            console.log('Inicializando sistema de autorización...');
            const result = await this.loadUserPermissions();
            
            if (result.success) {
                this.applyPermissionsToUI();
                console.log('Sistema de autorización inicializado correctamente');
                return true;
            } else {
                console.error('Error inicializando autorización:', result.error);
                return false;
            }
        } catch (error) {
            console.error('Error en inicialización de autorización:', error);
            return false;
        }
    }
}
