package com.test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import java.time.Duration;

public class TrelloTest {

    WebDriver driver;
    WebDriverWait wait;
    String boardName;

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-data-dir=C:/temp/chrome-profile");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        boardName = "QA Scrum Board - AK - " + System.currentTimeMillis();
    }

    @Test
    public void fullFlow() {
        // LOGIN
        driver.get("https://trello.com/login");

        WebElement email = wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        email.sendKeys("sheriljain67@gmail.com");

        driver.findElement(By.id("login-submit")).click();

        WebElement password = wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.id("sherile5"))
        );
        password.sendKeys("YOUR_PASSWORD");

        driver.findElement(By.id("login-submit")).click();

        wait.until(ExpectedConditions.urlContains("boards"));

        // CREATE LISTS
        createList("To Do");
        createList("In Progress");
        createList("Done");

        // CREATE CARDS
        createCard("To Do", "Task 1");
        createCard("In Progress", "Task 2");
        createCard("Done", "Task 3");

        // MOVE CARD
        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[text()='Task 1']")));

        WebElement target = driver.findElement(
                By.xpath("//h2[text()='In Progress']"));

        new Actions(driver).dragAndDrop(card, target).perform();

        WebElement doneList = driver.findElement(
                By.xpath("//h2[text()='Done']"));

        new Actions(driver).dragAndDrop(card, doneList).perform();
    }

    public void createList(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[name='name']"))).sendKeys(name);

        driver.findElement(By.xpath("//input[@value='Add list']")).click();
    }

    public void createCard(String listName, String cardName) {
        WebElement list = driver.findElement(By.xpath(
                "//h2[text()='" + listName + "']/ancestor::div[contains(@class,'list')]"));

        list.findElement(By.xpath(".//button[contains(text(),'Add a card')]")).click();
        list.findElement(By.tagName("textarea")).sendKeys(cardName);
        list.findElement(By.xpath(".//input[@value='Add card']")).click();
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}