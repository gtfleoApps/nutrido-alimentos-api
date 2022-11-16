#!/bin/bash
docker-compose -f ./db/docker-compose-dev.yml up --build -d
./mvnw compile quarkus:dev -DskipTests

# Projeto pronto, rodar:
# rm -rf nutrido-alimentos-api_mysql.nutrido-alimento
# docker-compose up --build -d

# ./mvnw quarkus:dev -Dquarkus.profile=dev-with-data