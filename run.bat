@echo off

rem Trova il percorso della directory in cui si trova lo script
set "script_dir=%~dp0"
set "script_dir=%script_dir:~0,-1%"

rem Nome del file jar
set "jar_file=progettoIngeSoft.jar"

rem Percorso completo del file jar
set "jar_path=%script_dir%\%jar_file%"

rem Verifica se il file jar esiste
if exist "%jar_path%" (
    echo Esecuzione di %jar_file% in corso...
    java -jar "%jar_path%"
) else (
    echo Errore: %jar_file% non trovato nella directory %CD%
    exit /b 1
)
