package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {


    public static SelenideElement paymentField = $$("button").find(Condition.exactText("Купить"));
    public static SelenideElement cardNumberField=$("[placeholder='0000 0000 0000 0000']");
    public static SelenideElement monthField= $("[placeholder='08']");
    public static SelenideElement yearField= $("[placeholder='22']");
    public static SelenideElement nameField=$x("//span[@class='input__top'][contains(text(), 'Владелец')]/parent::span//span[@class='input__box']/input[@class='input__control']");
    public static SelenideElement cvvField=$("[placeholder='999']");
    public static SelenideElement sendField=$$("button").find(Condition.exactText("Продолжить"));


//    public static SelenideElement cardNumberError=$$("input__sub").find(Condition.exactText("Неверный формат"));
 //   public static SelenideElement monthError=$$("input__sub").find(Condition.exactText("Неверно указан срок действия карты"));
  //  public static SelenideElement yearError=$$("input__sub").find(Condition.exactText("Истёк срок действия карты"));
 //   public static SelenideElement nameError=$$("input__sub").find(Condition.exactText("Поле обязательно для заполнения"));
  //  public static SelenideElement cvvError=$$("input__sub").find(Condition.exactText("Неверный формат"));

    public static void verifyNotificationOkVisibility() {

        $(".notification__content").shouldBe(Condition.visible, Duration.ofSeconds(10)).shouldHave(Condition.exactText("Операция одобрена банком."));
    }
    public static void verifyErrorNotificationVisibility() {

        $(".notification__content").shouldBe(Condition.visible, Duration.ofSeconds(10)).shouldHave(Condition.exactText("Ошибка! Банк отказал в проведении операции."));
    }


}
