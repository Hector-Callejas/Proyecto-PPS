@echo off
echo ========================================
echo    PRUEBA SIMPLE ENDPOINT DEBUG
echo ========================================
echo.

echo Probando endpoint de debug para usuario ID 1...
curl -s http://localhost:8081/api/usuarios/debug/1
echo.

echo.
echo Probando endpoint de debug para usuario ID 2...
curl -s http://localhost:8081/api/usuarios/debug/2
echo.

echo.
echo Probando endpoint de debug para usuario ID 3...
curl -s http://localhost:8081/api/usuarios/debug/3
echo.

echo.
echo ========================================
echo    PRUEBA COMPLETADA
echo ========================================
pause
