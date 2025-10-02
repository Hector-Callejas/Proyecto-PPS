@echo off
echo ========================================
echo    VERIFICANDO ENDPOINTS DEL BACKEND
echo ========================================
echo.

echo Probando conexion al backend...
curl -s http://localhost:8081/api/documentos/health
if %ERRORLEVEL% neq 0 (
    echo ERROR: Backend no responde en puerto 8081
    echo Asegurate de que el backend este corriendo
    pause
    exit /b 1
)

echo.
echo Probando endpoint de roles...
curl -s http://localhost:8081/api/roles
if %ERRORLEVEL% neq 0 (
    echo ERROR: Endpoint de roles no responde
) else (
    echo OK: Endpoint de roles funcionando
)

echo.
echo Probando endpoint de usuarios...
curl -s http://localhost:8081/api/usuarios
if %ERRORLEVEL% neq 0 (
    echo ERROR: Endpoint de usuarios no responde
) else (
    echo OK: Endpoint de usuarios funcionando
)

echo.
echo ========================================
echo    VERIFICACION COMPLETADA
echo ========================================
pause
