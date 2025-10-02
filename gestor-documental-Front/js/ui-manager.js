/**
 * Sistema de Manejo de UI y Notificaciones
 * Maneja mensajes, loading states y interacciones de usuario
 */

class UIManager {
    /**
     * Muestra un mensaje de éxito
     * @param {string} message - Mensaje a mostrar
     * @param {string} containerId - ID del contenedor (opcional)
     */
    static showSuccess(message, containerId = 'messageContainer') {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = `
                <div class="success">
                    ✅ ${message}
                </div>
            `;
            container.style.display = 'block';
            this.autoHide(container, 5000);
        } else {
            this.showToast(message, 'success');
        }
    }

    /**
     * Muestra un mensaje de error
     * @param {string} message - Mensaje a mostrar
     * @param {string} containerId - ID del contenedor (opcional)
     */
    static showError(message, containerId = 'messageContainer') {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = `
                <div class="error">
                    ❌ ${message}
                </div>
            `;
            container.style.display = 'block';
            this.autoHide(container, 8000);
        } else {
            this.showToast(message, 'error');
        }
    }

    /**
     * Muestra un mensaje de información
     * @param {string} message - Mensaje a mostrar
     * @param {string} containerId - ID del contenedor (opcional)
     */
    static showInfo(message, containerId = 'messageContainer') {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = `
                <div class="info">
                    ℹ️ ${message}
                </div>
            `;
            this.autoHide(container, 5000);
        } else {
            this.showToast(message, 'info');
        }
    }

    /**
     * Muestra un mensaje de carga
     * @param {string} message - Mensaje a mostrar
     * @param {string} containerId - ID del contenedor
     */
    static showLoading(message = 'Cargando...', containerId = 'messageContainer') {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = `
                <div class="loading">
                    <div class="spinner"></div>
                    ${message}
                </div>
            `;
            container.style.display = 'block';
        } else {
            console.warn('Contenedor de mensajes no encontrado:', containerId);
        }
    }

    /**
     * Limpia los mensajes
     * @param {string} containerId - ID del contenedor
     */
    static clearMessages(containerId = 'messageContainer') {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = '';
            container.style.display = 'none';
        }
    }

    /**
     * Auto-oculta un mensaje después de un tiempo
     * @param {HTMLElement} container - Contenedor del mensaje
     * @param {number} delay - Tiempo en milisegundos
     */
    static autoHide(container, delay) {
        setTimeout(() => {
            if (container) {
                container.innerHTML = '';
                container.style.display = 'none';
            }
        }, delay);
    }

    /**
     * Muestra un toast notification
     * @param {string} message - Mensaje a mostrar
     * @param {string} type - Tipo de mensaje (success, error, info)
     */
    static showToast(message, type = 'info') {
        // Crear el toast si no existe
        let toastContainer = document.getElementById('toastContainer');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.id = 'toastContainer';
            toastContainer.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 9999;
            `;
            document.body.appendChild(toastContainer);
        }

        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.style.cssText = `
            background: ${this.getToastColor(type)};
            color: white;
            padding: 12px 20px;
            border-radius: 4px;
            margin-bottom: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.2);
            animation: slideIn 0.3s ease-out;
        `;
        toast.textContent = message;

        toastContainer.appendChild(toast);

        // Auto-remover después de 5 segundos
        setTimeout(() => {
            toast.style.animation = 'slideOut 0.3s ease-in';
            setTimeout(() => {
                if (toast.parentNode) {
                    toast.parentNode.removeChild(toast);
                }
            }, 300);
        }, 5000);
    }

    /**
     * Obtiene el color para el toast según el tipo
     * @param {string} type - Tipo de mensaje
     * @returns {string} - Color en formato CSS
     */
    static getToastColor(type) {
        const colors = {
            success: '#28a745',
            error: '#dc3545',
            info: '#17a2b8',
            warning: '#ffc107'
        };
        return colors[type] || colors.info;
    }

    /**
     * Habilita/deshabilita un botón
     * @param {string} buttonId - ID del botón
     * @param {boolean} enabled - Si está habilitado
     * @param {string} text - Texto del botón (opcional)
     */
    static toggleButton(buttonId, enabled, text = null) {
        const button = document.getElementById(buttonId);
        if (button) {
            button.disabled = !enabled;
            if (text) {
                button.textContent = text;
            }
        }
    }

    /**
     * Muestra/oculta un elemento
     * @param {string} elementId - ID del elemento
     * @param {boolean} show - Si mostrar el elemento
     */
    static toggleElement(elementId, show) {
        const element = document.getElementById(elementId);
        if (element) {
            element.style.display = show ? 'block' : 'none';
        }
    }

    /**
     * Actualiza el contenido de un elemento
     * @param {string} elementId - ID del elemento
     * @param {string} content - Contenido HTML
     */
    static updateContent(elementId, content) {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = content;
        }
    }

    /**
     * Muestra un modal de confirmación
     * @param {string} message - Mensaje de confirmación
     * @param {Function} onConfirm - Callback para confirmar
     * @param {Function} onCancel - Callback para cancelar (opcional)
     */
    static showConfirm(message, onConfirm, onCancel = null) {
        const confirmed = confirm(message);
        if (confirmed && onConfirm) {
            onConfirm();
        } else if (!confirmed && onCancel) {
            onCancel();
        }
    }

    /**
     * Inicializa los estilos CSS para los mensajes
     */
    static initStyles() {
        if (document.getElementById('ui-manager-styles')) {
            return;
        }

        const style = document.createElement('style');
        style.id = 'ui-manager-styles';
        style.textContent = `
            .success {
                color: #3c763d;
                background-color: #dff0d8;
                border: 1px solid #d6e9c6;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 1rem;
            }
            
            .error {
                color: #a94442;
                background-color: #f2dede;
                border: 1px solid #ebccd1;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 1rem;
            }
            
            .info {
                color: #31708f;
                background-color: #d9edf7;
                border: 1px solid #bce8f1;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 1rem;
            }
            
            .loading {
                color: #8a6d3b;
                background-color: #fcf8e3;
                border: 1px solid #faebcc;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 1rem;
                display: flex;
                align-items: center;
                gap: 10px;
            }
            
            .spinner {
                width: 20px;
                height: 20px;
                border: 2px solid #f3f3f3;
                border-top: 2px solid #3498db;
                border-radius: 50%;
                animation: spin 1s linear infinite;
            }
            
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            
            @keyframes slideIn {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
            
            @keyframes slideOut {
                from { transform: translateX(0); opacity: 1; }
                to { transform: translateX(100%); opacity: 0; }
            }
        `;
        document.head.appendChild(style);
    }
}

// Inicializar estilos al cargar
document.addEventListener('DOMContentLoaded', function() {
    UIManager.initStyles();
});
