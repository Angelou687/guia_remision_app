@echo off
REM run.cmd - ejecuta la aplicación con JVM forzando UTF-8
java -Dfile.encoding=UTF-8 -cp bin;lib\mysql-connector-j-9.5.0.jar MainApp
if %ERRORLEVEL% neq 0 (
  echo EJECUCION FALLIDA
  exit /b %ERRORLEVEL%
)
exit /b 0
