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
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebDriver;
import pageobjects.RegisterPage;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Регистрация пользователя")
@RunWith(JUnit4.class)
public class RegisterPageTests {
    private WebDriver webDriver;
    private RegisterPage registerPage;
    private String email, name, password;
    private WebDriverFactory webDriverFactory;
    private ApiClient apiClient;

    @Before
    @Step("Подготовка к тесту: запуск браузера и создание тестовых данных")
    public void setUp() {
        initializeWebDriver();
        initializeTestData();
        initializeRegisterPage();

        Allure.addAttachment("Имя", name);
        Allure.addAttachment("Email", email);
        Allure.addAttachment("Пароль", password);
    }

    @After
    @Step("Завершение теста: закрытие браузера и очистка данных")
    public void tearDown() {
        apiClient.deleteTestUser(email, password);
        webDriver.quit();


    }

    private void initializeWebDriver() {
        webDriverFactory = new WebDriverFactory();
        webDriver = webDriverFactory.getWebDriver();
        webDriver.get(Parameters.URL_REGISTER_PAGE);
    }

    private void initializeTestData() {
        email = "email_" + UUID.randomUUID() + "@gmail.com";
        name = "name";
        password = "pass_" + UUID.randomUUID();
    }

    private void initializeRegisterPage() {
        registerPage = new RegisterPage(webDriver);
        apiClient = new ApiClient();
    }

    @Test
    @DisplayName("Успешная регистрация")
    public void registerNewUserIsSuccess() {
        Allure.parameter("Браузер", webDriverFactory.getBrowserName());

        performRegistration(email, name, password);

        registerPage.waitForFormSubmission("Вход");
        checkFormReload();
    }

    @Test
    @DisplayName("Регистрация с коротким паролем")
    public void registerNewUserLowPasswordIsFailed() {
        Allure.parameter("Браузер", webDriverFactory.getBrowserName());

        performRegistration(email, name, password.substring(0, 3));

        registerPage.waitForError();
        checkErrorMessage();
    }

    @Step("Регистрация нового пользователя")
    private void performRegistration(String email, String name, String password) {
        registerPage.setEmail(email);
        registerPage.setName(name);
        registerPage.setPassword(password);
        registerPage.clickRegisterButton();
    }

    @Step("Проверка перезагрузки формы регистрации")
    private void checkFormReload() {
        MatcherAssert.assertThat(
                "Форма регистрации не перезагрузилась",
                webDriver.getCurrentUrl(),
                containsString("/login")
        );
    }

    @Step("Проверка появления сообщения об ошибке")
    private void checkErrorMessage() {
        MatcherAssert.assertThat(
                "Некорректное сообщение об ошибке",
                registerPage.getErrorMessage(),
                equalTo("Некорректный пароль")
        );
    }
}