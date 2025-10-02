/**
 * Sistema de Gestión de Documentos
 * Maneja subida, listado y operaciones con documentos
 */

class DocumentManager {
    static API_BASE_URL = 'http://localhost:8081';

    /**
     * Prueba la conectividad con el backend
     * @returns {Promise} - Promesa con el resultado
     */
    static async testConnection() {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/documentos/health`,
                {
                    method: 'GET',
                    headers: {
                        'Authorization': AuthManager.getAuthHeaders().Authorization
                    }
                }
            );

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }

            const result = await response.text();
            return { success: true, message: result };

        } catch (error) {
            console.error('Error al probar conexión:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Sube un documento al servidor
     * @param {File} file - Archivo a subir
     * @param {Function} onProgress - Callback para progreso
     * @returns {Promise} - Promesa con el resultado
     */
    static async uploadDocument(file, onProgress = null) {
        try {
            // Validar archivo
            this.validateFile(file);

            const formData = new FormData();
            formData.append('file', file);
            formData.append('usuarioId', this.getCurrentUserId());

            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/documentos/subir`,
                {
                    method: 'POST',
                    headers: {
                        // No establecer Content-Type, dejar que el navegador lo haga para FormData
                        'Authorization': AuthManager.getAuthHeaders().Authorization
                    },
                    body: formData
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al subir documento');
            }

            const result = await response.json();
            return { success: true, document: result };

        } catch (error) {
            console.error('Error al subir documento:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Sube un documento en formato Base64
     * @param {string} fileName - Nombre del archivo
     * @param {string} fileType - Tipo MIME del archivo
     * @param {string} contentBase64 - Contenido en Base64
     * @returns {Promise} - Promesa con el resultado
     */
    static async uploadDocumentBase64(fileName, fileType, contentBase64) {
        try {
            const payload = {
                fileName: fileName,
                fileType: fileType,
                contentBase64: contentBase64,
                usuarioId: this.getCurrentUserId()
            };

            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/documentos/uploadBase64`,
                {
                    method: 'POST',
                    body: JSON.stringify(payload)
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al subir documento');
            }

            const result = await response.json();
            return { success: true, document: result };

        } catch (error) {
            console.error('Error al subir documento Base64:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Obtiene la lista de documentos
     * @returns {Promise} - Promesa con la lista de documentos
     */
    static async getDocuments() {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/documentos`
            );

            if (!response.ok) {
                throw new Error('Error al obtener documentos');
            }

            const documents = await response.json();
            return { success: true, documents: documents };

        } catch (error) {
            console.error('Error al obtener documentos:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Obtiene un documento específico por ID
     * @param {number} documentId - ID del documento
     * @returns {Promise} - Promesa con el documento
     */
    static async getDocument(documentId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/documentos/${documentId}`
            );

            if (!response.ok) {
                throw new Error('Error al obtener documento');
            }

            const document = await response.json();
            return { success: true, document: document };

        } catch (error) {
            console.error('Error al obtener documento:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Obtiene el contenido Base64 de un documento
     * @param {number} documentId - ID del documento
     * @returns {Promise} - Promesa con el contenido Base64
     */
    static async getDocumentContent(documentId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/documentos/${documentId}/content`
            );

            if (!response.ok) {
                throw new Error('Error al obtener contenido del documento');
            }

            const data = await response.json();
            return { success: true, content: data.content };

        } catch (error) {
            console.error('Error al obtener contenido del documento:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Descarga un documento
     * @param {number} documentId - ID del documento
     * @returns {Promise} - Promesa con el resultado
     */
    static async downloadDocument(documentId) {
        try {
            // Primero obtener la información del documento
            const docResult = await this.getDocument(documentId);
            if (!docResult.success) {
                throw new Error(docResult.error || 'No se pudo obtener información del documento');
            }

            const docData = docResult.document;
            
            // Verificar si es un archivo Base64 o físico
            if (docData.rutaArchivo && docData.rutaArchivo.startsWith('base64://')) {
                // Archivo guardado como Base64
                const contentResult = await this.getDocumentContent(documentId);
                if (!contentResult.success) {
                    throw new Error(contentResult.error || 'No se pudo obtener el contenido del documento');
                }

                // Crear blob y descargar
                const byteCharacters = atob(contentResult.content);
                const byteNumbers = new Array(byteCharacters.length);
                for (let i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                const byteArray = new Uint8Array(byteNumbers);
                const blob = new Blob([byteArray], { type: docData.tipoMime || 'application/octet-stream' });
                
                // Crear enlace de descarga
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                link.download = docData.nombreOriginal || 'documento';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                window.URL.revokeObjectURL(url);

                return { success: true, filename: docData.nombreOriginal };

            } else {
                // Archivo guardado físicamente - descargar usando fetch con autenticación
                const response = await AuthManager.authenticatedFetch(
                    `${this.API_BASE_URL}/api/documentos/${documentId}/download`
                );

                if (!response.ok) {
                    throw new Error(`Error ${response.status}: ${response.statusText}`);
                }

                // Obtener el blob del archivo
                const blob = await response.blob();
                
                // Crear enlace de descarga
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                link.download = docData.nombreOriginal || 'documento';
                link.style.display = 'none';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                window.URL.revokeObjectURL(url);

                return { success: true, filename: docData.nombreOriginal };
            }

        } catch (error) {
            console.error('Error al descargar documento:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Elimina un documento
     * @param {number} documentId - ID del documento
     * @returns {Promise} - Promesa con el resultado
     */
    static async deleteDocument(documentId) {
        try {
            const response = await AuthManager.authenticatedFetch(
                `${this.API_BASE_URL}/api/documentos/${documentId}`,
                {
                    method: 'DELETE'
                }
            );

            if (!response.ok) {
                throw new Error('Error al eliminar documento');
            }

            return { success: true };

        } catch (error) {
            console.error('Error al eliminar documento:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Valida un archivo antes de subirlo
     * @param {File} file - Archivo a validar
     */
    static validateFile(file) {
        const allowedTypes = [
            'application/pdf',
            'image/jpeg',
            'image/png',
            'image/gif',
            'text/plain',
            'application/msword',
            'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
        ];

        const maxSize = 10 * 1024 * 1024; // 10MB

        if (!allowedTypes.includes(file.type)) {
            throw new Error('Tipo de archivo no permitido. Tipos permitidos: PDF, imágenes, documentos de texto');
        }

        if (file.size > maxSize) {
            throw new Error('El archivo es demasiado grande. Tamaño máximo: 10MB');
        }
    }

    /**
     * Obtiene el ID del usuario actual
     * @returns {number} - ID del usuario
     */
    static getCurrentUserId() {
        // Mapeo simple de usuarios (en el futuro se puede obtener del backend)
        const username = AuthManager.getCurrentUser();
        if (username === 'admin') {
            return 1; // ID del usuario admin
        } else if (username === 'user') {
            return 2; // ID del usuario user
        }
        
        // Por defecto retornamos 1 (admin)
        return 1;
    }

    /**
     * Formatea una fecha para mostrar
     * @param {string} dateString - Fecha en formato ISO
     * @returns {string} - Fecha formateada
     */
    static formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('es-ES', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    /**
     * Obtiene el icono para un tipo de archivo
     * @param {string} mimeType - Tipo MIME del archivo
     * @returns {string} - Emoji del icono
     */
    static getFileIcon(mimeType) {
        const icons = {
            'application/pdf': '📄',
            'image/jpeg': '🖼️',
            'image/png': '🖼️',
            'image/gif': '🖼️',
            'text/plain': '📝',
            'application/msword': '📄',
            'application/vnd.openxmlformats-officedocument.wordprocessingml.document': '📄'
        };
        return icons[mimeType] || '📄';
    }
}

// Timestamp para forzar recarga del caché
console.log('DocumentManager cargado:', new Date().toISOString());
