package handlers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebDriverFactory {
    private static final Logger LOGGER = Logger.getLogger(WebDriverFactory.class.getName());

    public static String getBrowserName() {
        Properties properties = new Properties();
        try (InputStream input = WebDriverFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                LOGGER.severe("Не удалось найти файл config.properties");
                return "chrome";
            }
            properties.load(input);
            return properties.getProperty("browser", "chrome");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка загрузки файла свойств", e);
            return "chrome";
        }
    }

    public WebDriver getWebDriver() {
        String browserName = getBrowserName();
        LOGGER.info("Запуск тестов в браузере: " + browserName);

        switch (browserName.toLowerCase()) {
            case "chrome":
                return setupChromeDriver();

            case "firefox":
                return setupFirefoxDriver();

            default:
                throw new RuntimeException("Некорректное имя браузера. Поддерживаемые браузеры: chrome, firefox.");
        }
    }

    private WebDriver setupChromeDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage","--headless");
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(chromeOptions);
    }

    private WebDriver setupFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--headless"); // Если нужен безголовый режим
        return new FirefoxDriver(firefoxOptions);
    }
}