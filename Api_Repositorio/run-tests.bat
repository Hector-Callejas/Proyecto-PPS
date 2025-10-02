@echo off
echo ========================================
echo   EJECUTANDO PRUEBAS DEL BACKEND
echo ========================================

echo.
echo Limpiando proyecto...
call mvn clean

echo.
echo Compilando proyecto...
call mvn compile

echo.
echo Ejecutando pruebas unitarias...
call mvn test -Dtest="*Test" -DfailIfNoTests=false

echo.
echo ========================================
echo   RESULTADOS DE LAS PRUEBAS
echo ========================================
echo.
echo Si todas las pruebas pasaron, el build fue exitoso.
echo Si hay errores, revisa los logs arriba.
echo.

pause
