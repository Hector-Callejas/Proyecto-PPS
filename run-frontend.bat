@echo off
echo ========================================
echo    INICIANDO GESTOR DOCUMENTAL FRONTEND
echo ========================================
echo.

cd gestor-documental-Front

echo Iniciando servidor HTTP local...
echo Frontend disponible en: http://localhost:8080
echo.

python -m http.server 8080

pause
