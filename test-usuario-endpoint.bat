@echo off
echo ========================================
echo    PROBANDO ENDPOINT DE USUARIOS
echo ========================================
echo.

echo Probando endpoint de test...
curl -s http://localhost:8081/api/usuarios/test
echo.

echo.
echo Probando obtener todos los usuarios...
curl -s -H "Authorization: Bearer YOUR_TOKEN_HERE" http://localhost:8081/api/usuarios
echo.

echo.
echo Probando obtener usuario por ID (ID 1)...
curl -s -H "Authorization: Bearer YOUR_TOKEN_HERE" http://localhost:8081/api/usuarios/1
echo.

echo.
echo ========================================
echo    PRUEBAS COMPLETADAS
echo ========================================
pause
