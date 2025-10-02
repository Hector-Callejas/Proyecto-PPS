@echo off
echo ========================================
echo    DEBUG ENDPOINTS DE USUARIOS
echo ========================================
echo.

echo 1. Probando endpoint de test...
curl -s http://localhost:8081/api/usuarios/test
echo.

echo.
echo 2. Probando endpoint simple para usuario ID 1...
curl -s http://localhost:8081/api/usuarios/1/simple
echo.

echo.
echo 3. Probando endpoint simple para usuario ID 2...
curl -s http://localhost:8081/api/usuarios/2/simple
echo.

echo.
echo 4. Probando endpoint simple para usuario ID 3...
curl -s http://localhost:8081/api/usuarios/3/simple
echo.

echo.
echo 5. Probando endpoint completo para usuario ID 1...
curl -s http://localhost:8081/api/usuarios/1
echo.

echo.
echo ========================================
echo    PRUEBAS COMPLETADAS
echo ========================================
echo.
echo Revisa los logs del backend para ver los mensajes de debug
pause
