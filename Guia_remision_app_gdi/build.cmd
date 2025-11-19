@echo off
REM build.cmd - compila el proyecto asegurando UTF-8
if exist sources_cmd.txt del sources_cmd.txt
for /R %%i in (*.java) do @echo %%i >> sources_cmd.txt
javac -encoding UTF-8 -d bin -cp lib\mysql-connector-j-9.5.0.jar @sources_cmd.txt
if %ERRORLEVEL% neq 0 (
  echo COMPILACION FALLIDA
  exit /b %ERRORLEVEL%
)

echo COMPILACION OK
exit /b 0
