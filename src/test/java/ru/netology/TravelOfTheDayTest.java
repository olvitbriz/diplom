package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pages.CreditPage;
import ru.netology.pages.PaymentPage;

import java.time.Duration;

import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.openqa.selenium.devtools.v110.page.Page.close;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class TravelOfTheDayTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterAll
    static void teardown() {
        cleanDatabase();
    }


    @Test
    public void positivePaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var approvedInfo = DataHelper.getApprovedCardData();
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(approvedInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.verifyNotificationOkVisibility();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());


    }

    @Test
    public void negativePaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var declinedInfo = DataHelper.getDeclinedCardData();
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(declinedInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.verifyErrorNotificationVisibility();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());

    }

    @Test
    public void randomPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(randomInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.verifyErrorNotificationVisibility();
        assertNull(SQLHelper.getPaymentStatus());

    }

    @Test
    public void invalidCardNumberPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue("0000");
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.wrongFormat();
    }

    @Test
    public void invalidMonthPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(-1, 0);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(randomInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.wrongValidity();
    }

    @Test
    public void invalidYearPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, -1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(randomInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.endOfValidity();
    }

    @Test
    public void invalidNamePaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(randomInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(DataHelper.generateRandomInt());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.wrongFormat();
    }

    @Test
    public void invalidCvvPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(randomInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(DataHelper.generateRandomInt());
        paymentPage.sendFieldClick();
        paymentPage.wrongFormat();
    }

    @Test
    public void boardMonthAndYearPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(0, 0);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(randomInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.endOfValidity();
    }

    @Test
    public void emptyCardNumberPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.wrongFormat();
    }

    @Test
    public void emptyMonthPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(randomInfo.getNumber());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.wrongFormat();
    }

    @Test
    public void emptyYearPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(randomInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.wrongFormat();
    }

    @Test
    public void emptyNamePaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(randomInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.cvvFieldValue(randomInfo.getCvv());
        paymentPage.sendFieldClick();
        paymentPage.necessarilyField();
    }

    @Test
    public void emptyCvvPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        paymentPage.paymentFieldClick();
        paymentPage.cardNumberFieldValue(randomInfo.getNumber());
        paymentPage.monthFieldValue(randomInfo.getMonth());
        paymentPage.yearFieldValue(randomInfo.getYear());
        paymentPage.nameFieldValue(randomInfo.getName());
        paymentPage.sendFieldClick();
        paymentPage.wrongFormat();
    }

    @Test
    public void positiveCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var approvedInfo = DataHelper.getApprovedCardData();
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(approvedInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.verifyNotificationOkVisibility();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());

    }

    @Test
    public void negativeCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var declinedInfo = DataHelper.getDeclinedCardData();
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(declinedInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.verifyErrorNotificationVisibility();
        assertEquals("DECLINED", SQLHelper.getCreditStatus());

    }

    @Test
    public void randomCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(randomInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.verifyErrorNotificationVisibility();
        assertNull(SQLHelper.getCreditStatus());

    }

    @Test
    public void invalidCardNumberCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue("0000");
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.wrongFormat();
    }

    @Test
    public void invalidMonthCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(-1, 0);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(randomInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.wrongValidity();
    }

    @Test
    public void invalidYearCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, -1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(randomInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.endOfValidity();
    }


    @Test
    public void invalidNameCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(randomInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(DataHelper.generateRandomInt());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.wrongFormat();
    }

    @Test
    public void invalidCvvCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(randomInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(DataHelper.generateRandomInt());
        creditPage.sendFieldClick();
        creditPage.wrongFormat();
    }


    @Test
    public void boardMonthAndYearCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(0, 0);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(randomInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.endOfValidity();
    }

    @Test
    public void emptyCardNumberCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.wrongFormat();
    }

    @Test
    public void emptyMonthCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(randomInfo.getNumber());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.wrongFormat();
    }

    @Test
    public void emptyYearCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(randomInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.wrongFormat();
    }

    @Test
    public void emptyNameCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(randomInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.cvvFieldValue(randomInfo.getCvv());
        creditPage.sendFieldClick();
        creditPage.necessarilyField();
    }

    @Test
    public void emptyCvvCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var randomInfo = DataHelper.generateRandomInfo(1, 1);
        creditPage.creditFieldClick();
        creditPage.cardNumberFieldValue(randomInfo.getNumber());
        creditPage.monthFieldValue(randomInfo.getMonth());
        creditPage.yearFieldValue(randomInfo.getYear());
        creditPage.nameFieldValue(randomInfo.getName());
        creditPage.sendFieldClick();
        creditPage.wrongFormat();
    }

}
