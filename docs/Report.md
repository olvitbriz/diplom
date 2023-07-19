# Отчёт о дипломной работе

### Цель дипломной работы 
В рамках дипломной работы была выполнена автоматизация тестирования веб-сервиса, который предлагает купить тур по определенной цене двумя способами-за наличные и в кредит.

### Используемые инструменты и настройка тестового окружения
Был создан проект IDEA на базе Gradle, который реализуется с помощью инструментов JUnit5, Selenide. Также при написании автотестов были использованы инструмент кодогенерации Lombok и  Faker для генерации тестовых данных.
При выполнении дипломной работы ипользуется Docker, т.к. заявлена поддержка СУБД MySQL и PostgreSQL.
Для подключения к базам данных был использован менеджер баз данных DBeaver, а генерация отчетов реализована при помощи Allure.

Настройка контейнеров выпонена в файле docker-compose.yml, в папку gate-simulator помещён файл Dockerfile для запуска симмулятора. В файле build.gradle прописаны необходимые зависимости. Также в корень проекта помещен файл application.properties, содержащий учетные данные и url для подключения к СУБД.

### Планирование и автоматизация

В процессе подготовки было запланировано 28 тестовых сценариев, из них 2 позитивных теста ( Покупка по карте " 4444 4444 4444 4441" за наличные и в кредит одобрена банком), 2 негативных теста ( Покупка по карте " 4444 4444 4444 4442" за наличные и в кредит отклонена банком), 2 теста покупки  по рандомной карте ( за наличные и в кредит), а также тестирование невалидных значений полей и пустых полей, и тесты на граничные значения (текущий месяц и год).

Автотестирование реализовано полностью.
Из 28 тестов прошли успешно 18, а упали 10 ( 
в процентном отношении 65% успешных, 35% упали).


В процессе тестирования было выявлено 10 дефектов, которые были оформлены в виде баг-репортов:


1. Покупка за наличные по карте " 4444 4444 4444 4442"

https://github.com/olvitbriz/diplom/blob/d6bf87a16d9b964d6c7c96edeb5c0e6205bdb849/docs/screenshots/negativePaymentCase.png

2. Покупка за наличные  по рандомной карте 

https://github.com/olvitbriz/diplom/blob/d6bf87a16d9b964d6c7c96edeb5c0e6205bdb849/docs/screenshots/randomPaymentCase.png
3. Не валидное значение поля "Владелец" при покупке по карте

https://github.com/olvitbriz/diplom/blob/d6bf87a16d9b964d6c7c96edeb5c0e6205bdb849/docs/screenshots/invalidNamePaymentCase.png

4. Пустое поле "CVV" при покупке по карте

https://github.com/olvitbriz/diplom/blob/d6bf87a16d9b964d6c7c96edeb5c0e6205bdb849/docs/screenshots/emptyCvvPaymentCase.png

5. Граничное значение полей "Месяц" и "Год" при покупке по карте (требуется уточнить техзадание)

https://github.com/olvitbriz/diplom/blob/d6bf87a16d9b964d6c7c96edeb5c0e6205bdb849/docs/screenshots/boardMonthAndYearPaymentCase.png

6. Покупка в кредит по карте " 4444 4444 4444 4442" 

https://github.com/olvitbriz/diplom/blob/d6bf87a16d9b964d6c7c96edeb5c0e6205bdb849/docs/screenshots/negativeCreditCase.png

7. Покупка в кредит по рандомной карте 

https://github.com/olvitbriz/diplom/blob/d6bf87a16d9b964d6c7c96edeb5c0e6205bdb849/docs/screenshots/randomCreditCase.png

8. Не валидное значение поля "Владелец" при покупке в кредит

https://github.com/olvitbriz/diplom/blob/d6bf87a16d9b964d6c7c96edeb5c0e6205bdb849/docs/screenshots/invalidCreditPaymentCase.png

9. Пустое поле "CVV" при покупке в кредит

https://github.com/olvitbriz/diplom/blob/d6bf87a16d9b964d6c7c96edeb5c0e6205bdb849/docs/screenshots/emptyCvvCreditCase.png

10. Граничное значение полей "Месяц" и "Год" при покупке в кредит (требуется уточнить техзадание)

https://github.com/olvitbriz/diplom/blob/d6bf87a16d9b964d6c7c96edeb5c0e6205bdb849/docs/screenshots/boardMonthAndYearCreditCase.png