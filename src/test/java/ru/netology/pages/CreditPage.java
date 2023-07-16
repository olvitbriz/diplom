package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CreditPage {

    private SelenideElement creditField = $$("button").find(Condition.exactText("Купить"));
    private SelenideElement cardNumberField = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("[placeholder='08']");
    private SelenideElement yearField = $("[placeholder='22']");
    private SelenideElement nameField = $x("//span[@class='input__top'][contains(text(), 'Владелец')]/parent::span//span[@class='input__box']/input[@class='input__control']");
    private SelenideElement cvvField = $("[placeholder='999']");
    private SelenideElement sendField = $$("button").find(Condition.exactText("Продолжить"));


    public void creditFieldClick() {

        creditField.click();
    }

    public void cardNumberFieldValue(String cardNumber) {

        cardNumberField.setValue(cardNumber);
    }

    public void monthFieldValue(String month) {

        monthField.setValue(month);
    }

    public void yearFieldValue(String year) {

        yearField.setValue(year);
    }

    public void nameFieldValue(String name) {

        nameField.setValue(name);
    }

    public void cvvFieldValue(String cvv) {

        cvvField.setValue(cvv);
    }

    public void sendFieldClick() {

        sendField.click();
    }


    public void verifyNotificationOkVisibility() {

        $(".notification__content").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Операция одобрена банком."));
    }

    public void verifyErrorNotificationVisibility() {

        $(".notification__content").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Ошибка! Банк отказал в проведении операции."));
    }

    public void wrongFormat() {

        $(".input__sub").shouldBe(Condition.visible).shouldHave(Condition.exactText("Неверный формат"));
    }

    public void necessarilyField() {

        $(".input__sub").shouldBe(Condition.visible).shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    public void wrongValidity() {

        $(".input__sub").shouldBe(Condition.visible).shouldHave(Condition.exactText("Неверно указан срок действия карты"));
    }

    public void endOfValidity() {

        $(".input__sub").shouldBe(Condition.visible).shouldHave(Condition.exactText("Истёк срок действия карты"));
    }
}

