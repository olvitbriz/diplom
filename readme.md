# Процедура выполнения автотестов
## Запуск контейнеров

Запуск контейнеров выполнять при помощи команды docker-compose up --build;

## Запуск сервиса

1. Для запуска сервиса с указанием пути к базе данных mySQL выполнить команду  java  "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar  
2. Для запуска сервиса с указанием пути к базе данных postgreSQL выполнить команду java   "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar

## Запуск тестов 
1. Запуск тестов с указанием пути к базе данных mySQL выполнить  в терминале с помощью команды  ./gradlew clean test "-Db.url=jdbc:mysql://localhost:3306/app"
2. Запуск тестов с указанием пути к базе данных postgreSQL выполнить  в терминале с помощью команды  ./gradlew clean test "-Db.url=jdbc:postgresql://localhost:5432/app"

## Документация
1. План дипломной работы 
2. Отчет о дипломной работе 
3. Резюме 