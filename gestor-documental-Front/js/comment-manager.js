/**
 * Gestor de Comentarios para Documentos
 * Maneja la funcionalidad de comentarios de documentos
 */

class CommentManager {
    static API_BASE_URL = 'http://localhost:8081';

    /**
     * Obtiene todos los comentarios de un documento
     * @param {number} documentId - ID del documento
     * @returns {Promise} - Promesa con los comentarios
     */
    static async getComments(documentId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/comentarios/documento/${documentId}`
            );

            if (!response.ok) {
                throw new Error('Error al obtener comentarios');
            }

            const comments = await response.json();
            return { success: true, comments: comments };

        } catch (error) {
            console.error('Error al obtener comentarios:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Agrega un comentario a un documento
     * @param {number} documentId - ID del documento
     * @param {string} comentario - Texto del comentario
     * @returns {Promise} - Promesa con el resultado
     */
    static async addComment(documentId, comentario) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/comentarios`,
                {
                    method: 'POST',
                    headers: AuthManager.getAuthHeadersWithContentType(),
                    body: JSON.stringify({
                        documentoId: documentId,
                        comentario: comentario
                    })
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al agregar comentario');
            }

            const result = await response.json();
            return { success: true, comment: result };

        } catch (error) {
            console.error('Error al agregar comentario:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Obtiene el conteo de comentarios de un documento
     * @param {number} documentId - ID del documento
     * @returns {Promise} - Promesa con el conteo
     */
    static async getCommentCount(documentId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/comentarios/documento/${documentId}/count`
            );

            if (!response.ok) {
                throw new Error('Error al obtener conteo de comentarios');
            }

            const data = await response.json();
            return { success: true, count: data.count };

        } catch (error) {
            console.error('Error al obtener conteo de comentarios:', error);
            return { success: false, count: 0 };
        }
    }

    /**
     * Elimina un comentario
     * @param {number} commentId - ID del comentario
     * @returns {Promise} - Promesa con el resultado
     */
    static async deleteComment(commentId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/comentarios/${commentId}`,
                {
                    method: 'DELETE'
                }
            );

            if (!response.ok) {
                throw new Error('Error al eliminar comentario');
            }

            return { success: true };

        } catch (error) {
            console.error('Error al eliminar comentario:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Actualiza un comentario
     * @param {number} commentId - ID del comentario
     * @param {string} comentario - Nuevo texto del comentario
     * @returns {Promise} - Promesa con el resultado
     */
    static async updateComment(commentId, comentario) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/comentarios/${commentId}`,
                {
                    method: 'PUT',
                    headers: AuthManager.getAuthHeadersWithContentType(),
                    body: JSON.stringify({
                        comentario: comentario
                    })
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al actualizar comentario');
            }

            const result = await response.json();
            return { success: true, comment: result };

        } catch (error) {
            console.error('Error al actualizar comentario:', error);
            return { success: false, error: error.message };
        }
    }
}
