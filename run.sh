#!/bin/bash

# Trova il percorso della directory in cui si trova lo script
script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Nome del file jar
jar_file="progettoIngeSoft.jar"

# Percorso completo del file jar
jar_path="$script_dir/$jar_file"

# Verifica se il file jar esiste
if [ -f "$jar_path" ]; then
    echo "Esecuzione di $jar_file in corso..."
    java -jar "$jar_path"
else
    echo "Errore: $jar_file non trovato nella directory $(pwd)"
    exit 1
fi
