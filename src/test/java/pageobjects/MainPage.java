package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By headerLinks = By.xpath(".//p[starts-with(@class,'AppHeader_header__linkText')]");
    private final By basketButton = By.xpath(".//div[starts-with(@class,'BurgerConstructor_basket__container')]/button");
    private final By ingredientsButtons = By.xpath(".//section[starts-with(@class, 'BurgerIngredients_ingredients')]/div/div");
    private final By ingredientsTitles = By.xpath(".//div[starts-with(@class, 'BurgerIngredients_ingredients__menuContainer')]/h2");
    private final By header = By.xpath(".//main//h1");
    private final By modalOverlay = By.xpath(".//div[starts-with(@class, 'App_App')]/div/div[starts-with(@class, 'Modal_modal_overlay')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(60));
    }

    public void clickAuthButton() {
        waitUntilClickable();
        driver.findElement(basketButton).click();
    }

    private void waitUntilClickable() {
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(modalOverlay)));
    }

    public void waitForHeader() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(header));
    }

    private void waitForIngredientsScroll(int navNumber) {
        wait.until(driver -> driver.findElements(ingredientsTitles).get(navNumber).getLocation().getY() == 243);
    }

    public String getBasketButtonText() {
        return driver.findElement(basketButton).getText();
    }

    public void clickProfileLink() {
        waitUntilClickable();
        driver.findElements(headerLinks).get(2).click();
    }

    public int getIngredientTitleExpectedLocation() {
        return driver.findElements(ingredientsButtons).get(0).getLocation().getY()
                + driver.findElements(ingredientsButtons).get(0).getSize().getHeight();
    }

    public void clickBunsButton() {
        waitUntilClickable();
        driver.findElements(ingredientsButtons).get(0).click();
        waitForIngredientsScroll(0);
    }

    public void clickToppingsButton() {
        waitUntilClickable();
        driver.findElements(ingredientsButtons).get(1).click();
        waitForIngredientsScroll(1);
    }

    public void clickFillingsButton() {
        waitUntilClickable();
        driver.findElements(ingredientsButtons).get(2).click();
        waitForIngredientsScroll(2);
    }

    public int getBunsLocation() {
        return driver.findElements(ingredientsTitles).get(0).getLocation().getY();
    }

    public int getToppingsLocation() {
        return driver.findElements(ingredientsTitles).get(1).getLocation().getY();
    }

    public int getFillingsLocation() {
        return driver.findElements(ingredientsTitles).get(2).getLocation().getY();
    }
}