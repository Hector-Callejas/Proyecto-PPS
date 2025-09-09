/**
 * Gestor de Roles
 * Maneja la funcionalidad de gestión de roles
 */

class RoleManager {
    static API_BASE_URL = 'http://localhost:8081';

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
     * Obtiene un rol por ID
     * @param {number} roleId - ID del rol
     * @returns {Promise} - Promesa con el rol
     */
    static async getRole(roleId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/roles/${roleId}`
            );

            if (!response.ok) {
                throw new Error('Error al obtener rol');
            }

            const role = await response.json();
            return { success: true, role: role };

        } catch (error) {
            console.error('Error al obtener rol:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Crea un nuevo rol
     * @param {Object} roleData - Datos del rol
     * @returns {Promise} - Promesa con el resultado
     */
    static async createRole(roleData) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/roles`,
                {
                    method: 'POST',
                    headers: AuthManager.getAuthHeadersWithContentType(),
                    body: JSON.stringify(roleData)
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al crear rol');
            }

            const result = await response.json();
            return { success: true, role: result };

        } catch (error) {
            console.error('Error al crear rol:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Actualiza un rol
     * @param {number} roleId - ID del rol
     * @param {Object} roleData - Datos del rol
     * @returns {Promise} - Promesa con el resultado
     */
    static async updateRole(roleId, roleData) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/roles/${roleId}`,
                {
                    method: 'PUT',
                    headers: AuthManager.getAuthHeadersWithContentType(),
                    body: JSON.stringify(roleData)
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al actualizar rol');
            }

            const result = await response.json();
            return { success: true, role: result };

        } catch (error) {
            console.error('Error al actualizar rol:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Elimina un rol
     * @param {number} roleId - ID del rol
     * @returns {Promise} - Promesa con el resultado
     */
    static async deleteRole(roleId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/roles/${roleId}`,
                {
                    method: 'DELETE'
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al eliminar rol');
            }

            return { success: true };

        } catch (error) {
            console.error('Error al eliminar rol:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Obtiene los permisos disponibles
     * @returns {Promise} - Promesa con los permisos
     */
    static async getPermissions() {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/roles/permissions`
            );

            if (!response.ok) {
                throw new Error('Error al obtener permisos');
            }

            const permissions = await response.json();
            return { success: true, permissions: permissions };

        } catch (error) {
            console.error('Error al obtener permisos:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Asigna permisos a un rol
     * @param {number} roleId - ID del rol
     * @param {Array} permissions - Array de permisos
     * @returns {Promise} - Promesa con el resultado
     */
    static async assignPermissions(roleId, permissions) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/roles/${roleId}/permissions`,
                {
                    method: 'POST',
                    headers: AuthManager.getAuthHeadersWithContentType(),
                    body: JSON.stringify({ permissions: permissions })
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al asignar permisos');
            }

            const result = await response.json();
            return { success: true, role: result };

        } catch (error) {
            console.error('Error al asignar permisos:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Obtiene los usuarios de un rol
     * @param {number} roleId - ID del rol
     * @returns {Promise} - Promesa con los usuarios
     */
    static async getRoleUsers(roleId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/roles/${roleId}/users`
            );

            if (!response.ok) {
                throw new Error('Error al obtener usuarios del rol');
            }

            const users = await response.json();
            return { success: true, users: users };

        } catch (error) {
            console.error('Error al obtener usuarios del rol:', error);
            return { success: false, error: error.message };
        }
    }
}

