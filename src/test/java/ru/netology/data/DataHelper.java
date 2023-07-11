package ru.netology.data;

import com.github.javafaker.Faker;
import io.qameta.allure.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    public static CardInfo getApprovedCardData() {

        return new CardInfo("4444 4444 4444 4441", "APPROVED");
    }

    public static CardInfo getDeclinedCardData() {

        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    private static String generateRandomNumber() {
        return faker.business().creditCardNumber();
    }

    private static String generateRandomMonth(int plusMonth) {
        return LocalDate.now().plusMonths(plusMonth).format(DateTimeFormatter.ofPattern("MM"));
    }

    private static String generateRandomYear(int plusYear) {
        return LocalDate.now().plusYears(plusYear).format(DateTimeFormatter.ofPattern("yy"));
    }

    private static String generateRandomName() {
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    private static String generateRandomCvv() {
        return faker.numerify("###");
    }

    public static RandomInfo generateRandomInfo(int plusMonth, int plusYear) {
        return new RandomInfo(generateRandomNumber(), generateRandomMonth(plusMonth), generateRandomYear(plusYear), generateRandomName(), generateRandomCvv());
    }

    public static String generateRandomInt() {
        return faker.numerify("##");
    }


    @Value
    public static class CardInfo {
        String number;
        String status;
    }

    @Value
    public static class RandomInfo {
        private String number;
        private String month;
        private String year;
        private String name;
        private String cvv;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payment {
        private String id;
        private String amount;
        private String created;
        private String status;
        private String transaction_id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credit {
        private String id;
        private String bank_id;
        private String created;
        private String status;

    }
}
