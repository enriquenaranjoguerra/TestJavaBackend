#!/bin/bash

# 1. Limpia y Empaqueta el JAR
mvn clean package

# 2. Limpia el resultado de la construcción anterior
rm -rf portable_output

# 3. Ejecuta jpackage
jpackage --input target \
  --name "TestAppPortable" \
  --main-jar TestApp-1.0.0.jar \
  --type app-image \
  --icon test.ico \
  --java-options "-Dspring.datasource.url=jdbc:h2:file:~/opotromaticDB;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL -Dspring.jpa.hibernate.ddl-auto=update -Dlogging.file.name=logs/test-app.log" \
  --dest portable_output