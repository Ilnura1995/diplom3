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
import pageobjects.AuthPage;
import pageobjects.MainPage;
import pageobjects.ProfilePage;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;

@DisplayName("Проверки личного кабинета пользователя")
@RunWith(JUnit4.class)
public class ProfilePageTests {
    private WebDriver driver;
    private AuthPage authPage;
    private MainPage mainPage;
    private ProfilePage profilePage;
    private String name, email, password;
    private ApiClient apiClient;
    private WebDriverFactory webDriverFactory;

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
        driver.quit();
        boolean isUserDeleted = apiClient.deleteTestUser(email, password);
        MatcherAssert.assertThat("Не удалось удалить тестового пользователя", isUserDeleted);
    }

    private void initializeWebDriver() {
        webDriverFactory = new WebDriverFactory();
        driver = webDriverFactory.getWebDriver();
        driver.get(Parameters.URL_MAIN_PAGE);
    }

    private void initializeTestData() {
        name = "name";
        email = "email_" + UUID.randomUUID() + "@gmail.com";
        password = "pass_" + UUID.randomUUID();
    }

    private void initializePages() {
        authPage = new AuthPage(driver);
        mainPage = new MainPage(driver);
        profilePage = new ProfilePage(driver);
    }

    @Step("Процесс авторизации пользователя")
    private void authUser() {
        authPage.setEmail(email);
        authPage.setPassword(password);
        authPage.clickAuthButton();
        authPage.waitForFormSubmission();
    }

    @Step("Переход в личный кабинет")
    private void goToProfile() {
        driver.get(Parameters.URL_LOGIN_PAGE);
        authPage.waitForAuthForm();
        authUser();
        mainPage.clickProfileLink();
        profilePage.waitForAuthForm();
    }

    @Test
    @DisplayName("Проверка перехода по клику на «Личный кабинет»")
    public void checkLinkToProfileIsSuccess() {
        Allure.parameter("Браузер", webDriverFactory.getBrowserName());
        goToProfile();

        MatcherAssert.assertThat(
                "Некорректный URL страницы Личного кабинета",
                driver.getCurrentUrl(),
                containsString("/account/profile")
        );
    }
}