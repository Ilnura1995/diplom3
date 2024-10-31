package pageobjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {
    private final WebDriver webDriver;
    private final WebDriverWait wait;

    private final By inputs = By.xpath(".//form[starts-with(@class, 'Auth_form')]//fieldset//div[@class='input__container']//input");
    private final By registerButton = By.xpath(".//form[starts-with(@class, 'Auth_form')]/button");
    private final By errorMessage = By.xpath(".//form[starts-with(@class, 'Auth_form')]//fieldset//div[@class='input__container']//p[starts-with(@class,'input__error')]");
    private final By title = By.xpath(".//main//h2");
    private final By authLink = By.xpath(".//a[starts-with(@class,'Auth_link')]");
    private final By modalOverlay = By.xpath(".//div[starts-with(@class, 'App_App')]/div[starts-with(@class, 'Modal_modal')]");

    public RegisterPage(WebDriver driver) {
        this.webDriver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @Step("Заполнение имени")
    public void setName(String name) {
        webDriver.findElements(inputs).get(0).sendKeys(name);
    }

    @Step("Заполнение email")
    public void setEmail(String email) {
        webDriver.findElements(inputs).get(1).sendKeys(email);
    }

    @Step("Заполнение пароля")
    public void setPassword(String password) {
        webDriver.findElements(inputs).get(2).sendKeys(password);
    }

    @Step("Нажатие на кнопку регистрации")
    public void clickRegisterButton() {
        waitUntilClickable();
        webDriver.findElement(registerButton).click();
    }

    private void waitUntilClickable() {
        wait.until(ExpectedConditions.invisibilityOf(webDriver.findElement(modalOverlay)));
    }

    public void waitForFormSubmission(String expectedTitle) {
        wait.until(ExpectedConditions.textToBe(title, expectedTitle));
    }

    public void waitForError() {
        wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(errorMessage)));
    }

    public String getErrorMessage() {
        return webDriver.findElement(errorMessage).getText();
    }

    public void clickAuthLink() {
        waitUntilClickable();
        webDriver.findElement(authLink).click();
    }


}