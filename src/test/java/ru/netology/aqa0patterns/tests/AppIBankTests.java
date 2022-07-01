package ru.netology.aqa0patterns.tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.aqa0patterns.data.DataGenerator.*;
import static ru.netology.aqa0patterns.data.DataGenerator.Registration.*;

public class AppIBankTests {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//*[@data-test-id='login']//input").sendKeys(registeredUser.getLogin());
        $x("//*[@data-test-id='password']//input").sendKeys(registeredUser.getPassword());
        $("button.button").click();
        $(".heading")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//*[@data-test-id='login']//input").sendKeys(notRegisteredUser.getLogin());
        $x("//*[@data-test-id='password']//input").sendKeys(notRegisteredUser.getPassword());
        $("button.button").click();
        $(".notification.notification_visible[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $x("//*[@data-test-id='login']//input").sendKeys(blockedUser.getLogin());
        $x("//*[@data-test-id='password']//input").sendKeys(blockedUser.getPassword());
        $("button.button").click();
        $(".notification.notification_visible[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x("//*[@data-test-id='login']//input").sendKeys(wrongLogin);
        $x("//*[@data-test-id='password']//input").sendKeys(registeredUser.getPassword());
        $("button.button").click();
        $(".notification.notification_visible[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x("//*[@data-test-id='login']//input").sendKeys(registeredUser.getLogin());
        $x("//*[@data-test-id='password']//input").sendKeys(wrongPassword);
        $("button.button").click();
        $(".notification.notification_visible[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }
}
