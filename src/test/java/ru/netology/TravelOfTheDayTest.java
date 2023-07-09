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
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(approvedInfo.getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        PaymentPage.sendField.click();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
        PaymentPage.verifyNotificationOkVisibility();

    }

    @Test
    public void negativePaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        var declinedInfo = DataHelper.getDeclinedCardData();
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(declinedInfo.getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        PaymentPage.sendField.click();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
        PaymentPage.verifyErrorNotificationVisibility();
    }

    @Test
    public void randomPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        cleanDatabase();
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        PaymentPage.sendField.click();
        assertNull(SQLHelper.getPaymentStatus());
        PaymentPage.verifyErrorNotificationVisibility();
    }

    @Test
    public void invalidCardNumberPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue("0000");
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void invalidMonthPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(DataHelper.generateRandomInfo(-1, 0).getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(-1, 0).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(-1, 0).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(-1, 0).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(-1, 0).getCvv());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверно указан срок действия карты")).shouldBe(Condition.visible);
    }

    @Test
    public void invalidYearPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, -1).getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, -1).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, -1).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(1, -1).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(1, -1).getCvv());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Истёк срок действия карты")).shouldBe(Condition.visible);
    }

    @Test
    public void invalidNamePaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInt());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void invalidCvvPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInt());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void boardMonthAndYearPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(DataHelper.generateRandomInfo(0, 0).getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(0, 0).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(0, 0).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(0, 0).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(0, 0).getCvv());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Истёк срок действия карты")).shouldBe(Condition.visible);
    }

    @Test
    public void emptyCardNumberPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void emptyMonthPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void emptyYearPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void emptyNamePaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        PaymentPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения")).shouldBe(Condition.visible);
    }

    @Test
    public void emptyCvvPaymentCase() {

        var paymentPage = open("http://localhost:8080", PaymentPage.class);
        PaymentPage.paymentField.click();
        PaymentPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        PaymentPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        PaymentPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        PaymentPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        PaymentPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void positiveCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        var approvedInfo = DataHelper.getApprovedCardData();
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(approvedInfo.getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        CreditPage.sendField.click();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
        CreditPage.verifyNotificationOkVisibility();
    }

    @Test
    public void negativeCreditCase() {

        var CreditPage = open("http://localhost:8080", CreditPage.class);
        var declinedInfo = DataHelper.getDeclinedCardData();
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(declinedInfo.getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        CreditPage.sendField.click();
        assertEquals("DECLINED", SQLHelper.getCreditStatus());
        CreditPage.verifyErrorNotificationVisibility();
    }

    @Test
    public void randomCreditCase() {

        var paymentPage = open("http://localhost:8080", CreditPage.class);
        cleanDatabase();
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        CreditPage.sendField.click();
        assertNull(SQLHelper.getCreditStatus());
        CreditPage.verifyErrorNotificationVisibility();
    }

    @Test
    public void invalidCardNumberCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue("0000");
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void invalidMonthCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(DataHelper.generateRandomInfo(-1, 0).getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(-1, 0).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(-1, 0).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(-1, 0).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(-1, 0).getCvv());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверно указан срок действия карты")).shouldBe(Condition.visible);
    }
    @Test
    public void invalidYearCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, -1).getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, -1).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, -1).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(1, -1).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(1, -1).getCvv());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Истёк срок действия карты")).shouldBe(Condition.visible);
    }


    @Test
    public void invalidNameCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInt());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void invalidCvvCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInt());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }


    @Test
    public void boardMonthAndYearCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(DataHelper.generateRandomInfo(0, 0).getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(0, 0).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(0, 0).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(0, 0).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(0, 0).getCvv());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Истёк срок действия карты")).shouldBe(Condition.visible);
    }

    @Test
    public void emptyCardNumberCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void emptyMonthCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void emptyYearCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

    @Test
    public void emptyNameCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        CreditPage.cvvField.setValue(DataHelper.generateRandomInfo(1, 1).getCvv());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения")).shouldBe(Condition.visible);
    }

    @Test
    public void emptyCvvCreditCase() {

        var creditPage = open("http://localhost:8080", CreditPage.class);
        CreditPage.creditField.click();
        CreditPage.cardNumberField.setValue(DataHelper.generateRandomInfo(1, 1).getNumber());
        CreditPage.monthField.setValue(DataHelper.generateRandomInfo(1, 1).getMonth());
        CreditPage.yearField.setValue(DataHelper.generateRandomInfo(1, 1).getYear());
        CreditPage.nameField.setValue(DataHelper.generateRandomInfo(1, 1).getName());
        CreditPage.sendField.click();
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
    }

}
