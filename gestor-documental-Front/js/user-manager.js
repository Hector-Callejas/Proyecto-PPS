/**
 * Gestor de Usuarios
 * Maneja la funcionalidad de gestión de usuarios
 */

class UserManager {
    static API_BASE_URL = 'http://localhost:8081';

    /**
     * Obtiene todos los usuarios
     * @returns {Promise} - Promesa con los usuarios
     */
    static async getUsers() {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/usuarios`
            );

            if (!response.ok) {
                throw new Error('Error al obtener usuarios');
            }

            const users = await response.json();
            return { success: true, users: users };

        } catch (error) {
            console.error('Error al obtener usuarios:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Obtiene un usuario por ID
     * @param {number} userId - ID del usuario
     * @returns {Promise} - Promesa con el usuario
     */
    static async getUser(userId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/usuarios/${userId}`
            );

            if (!response.ok) {
                throw new Error('Error al obtener usuario');
            }

            const user = await response.json();
            return { success: true, user: user };

        } catch (error) {
            console.error('Error al obtener usuario:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Crea un nuevo usuario
     * @param {Object} userData - Datos del usuario
     * @returns {Promise} - Promesa con el resultado
     */
    static async createUser(userData) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/usuarios`,
                {
                    method: 'POST',
                    headers: AuthManager.getAuthHeadersWithContentType(),
                    body: JSON.stringify(userData)
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al crear usuario');
            }

            const result = await response.json();
            return { success: true, user: result };

        } catch (error) {
            console.error('Error al crear usuario:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Actualiza un usuario
     * @param {number} userId - ID del usuario
     * @param {Object} userData - Datos del usuario
     * @returns {Promise} - Promesa con el resultado
     */
    static async updateUser(userId, userData) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/usuarios/${userId}`,
                {
                    method: 'PUT',
                    headers: AuthManager.getAuthHeadersWithContentType(),
                    body: JSON.stringify(userData)
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al actualizar usuario');
            }

            const result = await response.json();
            return { success: true, user: result };

        } catch (error) {
            console.error('Error al actualizar usuario:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Elimina un usuario
     * @param {number} userId - ID del usuario
     * @returns {Promise} - Promesa con el resultado
     */
    static async deleteUser(userId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/usuarios/${userId}`,
                {
                    method: 'DELETE'
                }
            );

            if (!response.ok) {
                throw new Error('Error al eliminar usuario');
            }

            return { success: true };

        } catch (error) {
            console.error('Error al eliminar usuario:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Obtiene todos los roles
     * @returns {Promise} - Promesa con los roles
     */
    static async getRoles() {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/roles`
            );

            if (!response.ok) {
                throw new Error('Error al obtener roles');
            }

            const roles = await response.json();
            return { success: true, roles: roles };

        } catch (error) {
            console.error('Error al obtener roles:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Cambia el estado activo/inactivo de un usuario
     * @param {number} userId - ID del usuario
     * @param {boolean} activo - Estado del usuario
     * @returns {Promise} - Promesa con el resultado
     */
    static async toggleUserStatus(userId, activo) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/usuarios/${userId}/status`,
                {
                    method: 'PATCH',
                    headers: AuthManager.getAuthHeadersWithContentType(),
                    body: JSON.stringify({ activo: activo })
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al cambiar estado del usuario');
            }

            const result = await response.json();
            return { success: true, user: result };

        } catch (error) {
            console.error('Error al cambiar estado del usuario:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Asigna roles a un usuario
     * @param {number} userId - ID del usuario
     * @param {Array} roles - Array de roles
     * @returns {Promise} - Promesa con el resultado
     */
    static async assignRoles(userId, roles) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/usuarios/${userId}/roles`,
                {
                    method: 'POST',
                    headers: AuthManager.getAuthHeadersWithContentType(),
                    body: JSON.stringify({ roles: roles })
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al asignar roles');
            }

            const result = await response.json();
            return { success: true, user: result };

        } catch (error) {
            console.error('Error al asignar roles:', error);
            return { success: false, error: error.message };
        }
    }
}

