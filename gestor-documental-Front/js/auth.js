/**
 * Sistema de Autenticación para Gestor Documental
 * Maneja login, logout y tokens JWT
 */

class AuthManager {
    static API_BASE_URL = 'http://localhost:8081';
    
    /**
     * Realiza login del usuario
     * @param {string} username - Nombre de usuario
     * @param {string} password - Contraseña
     * @returns {Promise} - Promesa con el resultado del login
     */
    static async login(username, password) {
        try {
            const response = await fetch(`${this.API_BASE_URL}/api/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error en el login');
            }

            const data = await response.json();
            
            // Guardar token y datos del usuario
            localStorage.setItem('auth_token', data.token);
            localStorage.setItem('user_data', JSON.stringify({
                username: username,
                loginTime: new Date().toISOString()
            }));

            return { success: true, token: data.token };
        } catch (error) {
            console.error('Error en login:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Obtiene los headers de autenticación
     * @returns {Object} - Headers con token JWT
     */
    static getAuthHeaders() {
        const token = localStorage.getItem('auth_token');
        return {
            'Authorization': `Bearer ${token}`
            // NO incluir Content-Type aquí para FormData
        };
    }

    /**
     * Obtiene los headers de autenticación con Content-Type para JSON
     * @returns {Object} - Headers con token JWT y Content-Type
     */
    static getAuthHeadersWithContentType() {
        const token = localStorage.getItem('auth_token');
        return {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
    }

    /**
     * Verifica si el usuario está autenticado
     * @returns {boolean} - True si está autenticado
     */
    static isAuthenticated() {
        const token = localStorage.getItem('auth_token');
        const userData = localStorage.getItem('user_data');
        
        if (!token || !userData) {
            return false;
        }

        // Verificar si el token no ha expirado (opcional)
        try {
            const user = JSON.parse(userData);
            const loginTime = new Date(user.loginTime);
            const now = new Date();
            const hoursDiff = (now - loginTime) / (1000 * 60 * 60);
            
            // Token válido por 24 horas
            if (hoursDiff > 24) {
                this.logout();
                return false;
            }
        } catch (error) {
            this.logout();
            return false;
        }

        return true;
    }

    /**
     * Obtiene el nombre de usuario actual
     * @returns {string|null} - Nombre de usuario o null
     */
    static getCurrentUser() {
        const userData = localStorage.getItem('user_data');
        if (userData) {
            try {
                const user = JSON.parse(userData);
                return user.username;
            } catch (error) {
                return null;
            }
        }
        return null;
    }

    /**
     * Cierra la sesión del usuario
     */
    static logout() {
        localStorage.removeItem('auth_token');
        localStorage.removeItem('user_data');
        window.location.href = '../index.html';
    }

    /**
     * Redirige a login si no está autenticado
     */
    static requireAuth() {
        if (!this.isAuthenticated()) {
            window.location.href = '../index.html';
            return false;
        }
        return true;
    }

    /**
     * Realiza una petición autenticada
     * @param {string} url - URL de la petición
     * @param {Object} options - Opciones de fetch
     * @returns {Promise} - Promesa con la respuesta
     */
    static async authenticatedFetch(url, options = {}) {
        if (!this.isAuthenticated()) {
            throw new Error('Usuario no autenticado');
        }

        const defaultOptions = {
            headers: this.getAuthHeaders()
        };

        const mergedOptions = {
            ...defaultOptions,
            ...options,
            headers: {
                ...defaultOptions.headers,
                ...options.headers
            }
        };

        const response = await fetch(url, mergedOptions);
        
        if (response.status === 401) {
            this.logout();
            throw new Error('Sesión expirada');
        }

        return response;
    }
}

// Función global para logout (para usar en HTML)
function logout() {
    AuthManager.logout();
}

// Verificar autenticación al cargar páginas protegidas
document.addEventListener('DOMContentLoaded', function() {
    // Solo verificar en páginas que no sean login
    if (!window.location.pathname.includes('index.html')) {
        AuthManager.requireAuth();
    }
});
