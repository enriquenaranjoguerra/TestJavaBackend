#!/bin/bash

# 1. Limpia y Empaqueta el JAR
mvn clean package

# 2. Limpia el resultado de la construcción anterior
rm -rf portable_output

# 3. Ejecuta jpackage
jpackage --input target --name "TestAppPortable" --main-jar TestApp-1.0.0.jar --type app-image --icon test.ico --dest portable_output