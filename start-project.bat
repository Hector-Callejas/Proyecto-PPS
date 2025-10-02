@echo off
echo ========================================
echo    GESTOR DOCUMENTAL - INICIO COMPLETO
echo ========================================
echo.

echo PASO 1: Verificando base de datos...
echo Asegurate de que PostgreSQL este corriendo y la BD 'gestor_documental' exista
echo.

echo PASO 2: Iniciando Backend (Puerto 8081)...
start "Backend" cmd /k "cd Api_Repositorio && mvn spring-boot:run"

echo Esperando 10 segundos para que el backend inicie...
timeout /t 10 /nobreak > nul

echo PASO 3: Iniciando Frontend (Puerto 8080)...
start "Frontend" cmd /k "cd gestor-documental-Front && python -m http.server 8080"

echo.
echo ========================================
echo    APLICACION INICIADA
echo ========================================
echo Backend:  http://localhost:8081
echo Frontend: http://localhost:8080
echo Swagger:  http://localhost:8081/swagger-ui.html
echo.
echo Usuarios de prueba:
echo - admin / admin123
echo - user / admin123
echo.
echo Presiona cualquier tecla para salir...
pause > nul
