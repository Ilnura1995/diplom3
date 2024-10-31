package praktikum;

import handlers.ApiClient;
import handlers.Parameters;
import handlers.WebDriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pageobjects.AuthPage;
import pageobjects.ForgotPasswordPage;
import pageobjects.MainPage;
import pageobjects.RegisterPage;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

@DisplayName("Авторизация пользователя")
public class AuthTests {

    private WebDriver webDriver;
    private AuthPage authPage;
    private MainPage mainPage;
    private RegisterPage registerPage;
    private ForgotPasswordPage forgotPasswordPage;
    private String name, email, password;
    private ApiClient apiClient;

    @Before
    @Step("Подготовка к тесту: запуск браузера и создание тестовых данных")
    public void setUp() {
        initializeWebDriver();
        initializeTestData();
        initializePages();

        Allure.addAttachment("Имя", name);
        Allure.addAttachment("Email", email);
        Allure.addAttachment("Пароль", password);

        apiClient = new ApiClient();
        boolean isUserCreated = apiClient.createUser(name, email, password);
        MatcherAssert.assertThat("Не удалось создать тестового пользователя", isUserCreated);
    }

    @After
    @Step("Завершение теста: закрытие браузера и очистка данных")
    public void tearDown() {
        webDriver.quit();
        boolean isUserDeleted = apiClient.deleteTestUser(email, password);
        MatcherAssert.assertThat("Не удалось удалить тестового пользователя", isUserDeleted);
    }

    private void initializeWebDriver() {
        WebDriverFactory webDriverFactory = new WebDriverFactory();
        webDriver = webDriverFactory.getWebDriver();
        webDriver.get(Parameters.URL_MAIN_PAGE);
    }

    private void initializeTestData() {
        name = "name";
        email = "email_" + UUID.randomUUID() + "@gmail.com";
        password = "pass_" + UUID.randomUUID();
    }

    private void initializePages() {
        authPage = new AuthPage(webDriver);
        mainPage = new MainPage(webDriver);
        registerPage = new RegisterPage(webDriver);
        forgotPasswordPage = new ForgotPasswordPage(webDriver);
    }

    @Step("Процесс авторизации пользователя")
    private void authUser() {
        authPage.setEmail(email);
        authPage.setPassword(password);
        authPage.clickAuthButton();
        authPage.waitForFormSubmission();
    }

    @Test
    @DisplayName("Вход по кнопке «Войти в аккаунт» на главной")
    public void authFromMainIsSuccess() {
        Allure.parameter("Браузер", webDriver.getClass().getSimpleName());

        mainPage.clickAuthButton();
        authPage.waitForAuthForm();

        authUser();

        MatcherAssert.assertThat(
                "Ожидается надпись «Оформить заказ» на кнопке в корзине",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }

    @Test
    @DisplayName("Успешный вход через основную форму авторизации")
    public void authFromAuthFormIsSuccess() {
        Allure.parameter("Браузер", webDriver.getClass().getSimpleName());

        mainPage.clickAuthButton();
        authPage.waitForAuthForm();

        authUser();

        MatcherAssert.assertThat(
                "Ожидается надпись «Оформить заказ» на кнопке в корзине",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }

    @Test
    @DisplayName("Вход через кнопку «Личный кабинет»")
    public void authFromLinkToProfileIsSuccess() {
        Allure.parameter("Браузер", webDriver.getClass().getSimpleName());

        mainPage.clickProfileLink();
        authPage.waitForAuthForm();

        authUser();

        MatcherAssert.assertThat(
                "Ожидается надпись «Оформить заказ» на кнопке в корзине",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    public void authLinkFromRegFormIsSuccess() {
        Allure.parameter("Браузер", webDriver.getClass().getSimpleName());

        webDriver.get(Parameters.URL_REGISTER_PAGE);

        registerPage.clickAuthLink();
        authPage.
                waitForAuthForm();

        authUser();

        MatcherAssert.assertThat(
                "Ожидается надпись «Оформить заказ» на кнопке в корзине",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    public void authLinkFromForgotPasswordFormIsSuccess() {
        Allure.parameter("Браузер", webDriver.getClass().getSimpleName());

        webDriver.get(Parameters.URL_FORGOT_PASSWORD_PAGE);

        forgotPasswordPage.clickAuthLink();
        authPage.waitForAuthForm();

        authUser();

        MatcherAssert.assertThat(
                "Ожидается надпись «Оформить заказ» на кнопке в корзине",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }
}