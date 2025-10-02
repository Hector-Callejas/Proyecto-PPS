@echo off
echo ========================================
echo    INICIANDO GESTOR DOCUMENTAL BACKEND
echo ========================================
echo.

cd Api_Repositorio

echo Compilando proyecto...
call mvn clean compile -q

if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo en la compilacion
    pause
    exit /b 1
)

echo.
echo Iniciando aplicacion Spring Boot...
echo Backend disponible en: http://localhost:8081
echo Swagger UI disponible en: http://localhost:8081/swagger-ui.html
echo.

call mvn spring-boot:run

pause
