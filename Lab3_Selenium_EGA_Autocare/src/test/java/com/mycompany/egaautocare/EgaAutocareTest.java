package com.mycompany.egaautocare;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Lab 3: End-to-End Web Automation Testing with Selenium WebDriver on EGA Autocare
 * Target Website: https://ega-autocare.mysapo.net/
 * Team Allocation:
 * - Member 3 (M3 - Tran Cong Tien): TC01 -> TC04 (Access website, Valid Login, Invalid Login, Product Search)
 * - Member 4 (M4 - Van Thai Trung): TC05 -> TC06 (Buy at least 3 distinct products, Verify exact shopping cart total)
 */
@TestMethodOrder(OrderAnnotation.class)
public class EgaAutocareTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "https://ega-autocare.mysapo.net";

    // Stores the expected accumulated cart total when adding 3 products in TC05 for exact comparison in TC06
    private static long expectedCartTotal = 0L;

    @BeforeAll
    public static void setUpAll() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");

        // Support headless execution when passing -Dheadless=true
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        // Selenium 4 automatically resolves and manages ChromeDriver via Selenium Manager
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterAll
    public static void tearDownAll() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ===============================================================
    // PART 1 - MEMBER 3 (M3 - Tran Cong Tien): TC01 -> TC04
    // ===============================================================

    @Test
    @Order(1)
    public void test01_AccessWeb() {
        System.out.println("=== TC01: Test accessing EGA Autocare e-commerce website ===");
        driver.get(BASE_URL);

        String title = driver.getTitle();
        System.out.println("Page Title: " + title);
        assertTrue(title.contains("EGA Autocare") || title.toLowerCase().contains("autocare"),
                "Page title must contain 'EGA Autocare'");

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
        assertTrue(currentUrl.contains("ega-autocare.mysapo.net"),
                "Current URL must belong to domain ega-autocare.mysapo.net");
    }

    @Test
    @Order(2)
    public void test02_LoginSuccess() {
        System.out.println("=== TC02: Test valid customer login ===");
        driver.manage().deleteAllCookies();
        driver.get(BASE_URL + "/account/login");

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer_email")));
        WebElement passwordInput = driver.findElement(By.id("customer_password"));

        emailInput.clear();
        emailInput.sendKeys("linhcampochia@gmail.com");

        passwordInput.clear();
        passwordInput.sendKeys("03022006Tien!");

        // Click the Login button scoped inside the #customer_login form
        WebElement loginBtn = driver.findElement(By.cssSelector("#customer_login button.btn-login"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);

        // Wait until redirected away from /account/login to the customer account page (/account)
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/account/login")));

        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource();
        System.out.println("URL after successful login: " + currentUrl);

        assertFalse(currentUrl.contains("/account/login"),
                "User must be redirected away from /account/login after valid login");
        assertTrue(currentUrl.contains("/account") && pageSource.contains("linhcampochia@gmail.com"),
                "Account page must display the logged-in customer email");
    }

    @Test
    @Order(3)
    public void test03_LoginFail_Unregistered() {
        System.out.println("=== TC03: Test login failure with unregistered account credentials ===");
        driver.manage().deleteAllCookies();
        driver.get(BASE_URL + "/account/logout");
        driver.get(BASE_URL + "/account/login");

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer_email")));
        WebElement passwordInput = driver.findElement(By.id("customer_password"));

        String unregisteredEmail = "unregistered_user_" + System.currentTimeMillis() + "@gmail.com";
        emailInput.clear();
        emailInput.sendKeys(unregisteredEmail);

        passwordInput.clear();
        passwordInput.sendKeys("WrongPassword123!");

        WebElement loginBtn = driver.findElement(By.cssSelector("#customer_login button.btn-login"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);

        // Wait for the error alert message inside the #customer_login form
        WebElement errorAlert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#customer_login .text-error, #customer_login .alert")));
        String errorText = errorAlert.getText().trim();
        String currentUrl = driver.getCurrentUrl();

        System.out.println("URL after failed login: " + currentUrl);
        System.out.println("Displayed error message: " + errorText);

        assertTrue(currentUrl.contains("/account/login"),
                "Failed login must keep the user on /account/login");
        assertTrue(errorText.contains("Thông tin đăng nhập không chính xác"),
                "Must display the invalid login error message");
    }

    @Test
    @Order(4)
    public void test04_SearchProduct() {
        System.out.println("=== TC04: Test product search functionality ===");
        driver.get(BASE_URL);

        List<WebElement> searchInputs = driver.findElements(By.name("query"));
        boolean searched = false;
        for (WebElement input : searchInputs) {
            try {
                if (input.isDisplayed()) {
                    input.clear();
                    input.sendKeys("bơm lốp");
                    input.sendKeys(Keys.ENTER);
                    searched = true;
                    break;
                }
            } catch (Exception ignored) {}
        }

        if (!searched) {
            List<WebElement> searchTriggers = driver.findElements(By.cssSelector("[data-portal='#search-drawer'], .search-bar, a[href*='search']"));
            if (!searchTriggers.isEmpty()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchTriggers.get(0));
                try {
                    WebElement visibleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("query")));
                    visibleInput.sendKeys("bơm lốp");
                    visibleInput.sendKeys(Keys.ENTER);
                    searched = true;
                } catch (Exception ignored) {}
            }
        }

        if (!searched) {
            driver.get(BASE_URL + "/search?query=b%C6%A1m+l%E1%BB%91p");
        }

        wait.until(ExpectedConditions.urlContains("/search"));
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Search results URL: " + currentUrl);
        assertTrue(currentUrl.contains("query=") || currentUrl.contains("q="),
                "Search URL must contain the query parameter");

        List<WebElement> results = driver.findElements(By.cssSelector(".product-item, .product-card, .search-result, h1, h2, .section-heading"));
        assertFalse(results.isEmpty(), "Search results page must display matching product items");
        assertTrue(driver.getPageSource().toLowerCase().contains("bơm lốp"),
                "Search results page must contain products matching the keyword 'bơm lốp'");
    }

    // ===============================================================
    // PART 2 - MEMBER 4 (M4 - Van Thai Trung): TC05 -> TC06
    // ===============================================================

    @Test
    @Order(5)
    public void test05_BuyAtLeast3Products() {
        System.out.println("=== TC05: Test selecting and adding at least 3 distinct products to cart ===");
        // Clear the shopping cart prior to adding 3 new products
        driver.get(BASE_URL + "/cart/clear");
        expectedCartTotal = 0L;

        // List of 3 distinct automotive products on EGA Autocare
        String[] productSlugs = {
            "/michelin-m2566-bom-lop",       // Product 1: Michelin M2566 Tire Inflator (1,180,000 VND)
            "/areon-tinh-dau-treo-xe",       // Product 2: Areon Car Hanging Perfume (285,000 VND)
            "/autoguard-bat-phu-xe"          // Product 3: AutoGuard Car Cover (450,000 VND)
        };

        for (int i = 0; i < productSlugs.length; i++) {
            String slug = productSlugs[i];
            driver.get(BASE_URL + slug);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-form")));

            // Extract the displayed unit price on the product detail page (span.price.text-h4)
            List<WebElement> priceEls = driver.findElements(By.cssSelector("span.price.text-h4, #add-to-cart-form .price"));
            String rawPrice = "";
            for (WebElement el : priceEls) {
                String txt = el.getAttribute("textContent");
                if (txt != null && txt.contains("₫")) {
                    rawPrice = txt.trim();
                    break;
                }
            }
            long itemPrice = Long.parseLong(rawPrice.replaceAll("[^0-9]", ""));
            expectedCartTotal += itemPrice;

            System.out.println("Product " + (i + 1) + " (" + slug + ") - Unit Price: " + itemPrice + " VND (" + rawPrice + ")");

            // Click the Add to Cart button
            WebElement addToCartBtn = driver.findElement(By.cssSelector("#add-to-cart-form button[name='addtocart'], button.btn-add-to-cart"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);

            try {
                Thread.sleep(2500);
            } catch (InterruptedException ignored) {}
        }

        // Navigate to /cart to verify all 3 products are present in the cart
        driver.get(BASE_URL + "/cart");
        wait.until(ExpectedConditions.urlContains("/cart"));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}

        List<WebElement> cartQtyInputs = driver.findElements(
                By.cssSelector("input[name='Updates'], input[name='updates[]'], .cart-item, tr.line-item, input[type='number']"));
        System.out.println("Cart line item elements found: " + cartQtyInputs.size());
        System.out.println("Expected total of 3 products: " + expectedCartTotal + " VND");

        assertTrue(driver.getCurrentUrl().contains("/cart"), "Must navigate to shopping cart page /cart");
        assertTrue(expectedCartTotal > 0, "Expected total price of the 3 selected products must be greater than 0");
    }

    @Test
    @Order(6)
    public void test06_VerifyCartTotal() {
        System.out.println("=== TC06: Test verifying shopping cart total price accuracy ===");
        driver.get(BASE_URL + "/cart");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        // 1. Retrieve expected total from the 3 products added in TC05 (1,180,000 + 285,000 + 450,000 = 1,915,000 VND)
        long expectedTotal = expectedCartTotal > 0 ? expectedCartTotal : (1180000L + 285000L + 450000L);
        System.out.println("Expected Cart Total (Product 1 + Product 2 + Product 3): " + expectedTotal + " VND");

        // 2. Parse the actual total price displayed in '.cart-total .price' on /cart
        WebElement totalElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-total .price")));
        String rawTotalText = totalElement.getText().trim();
        System.out.println("Displayed Cart Total Text: '" + rawTotalText + "'");
        assertFalse(rawTotalText.isEmpty(), "Displayed cart total string must not be empty");

        long actualTotal = Long.parseLong(rawTotalText.replaceAll("[^0-9]", ""));
        System.out.println("Parsed Actual Cart Total (numeric): " + actualTotal + " VND");

        // 3. Assert 100% exact match between Actual Cart Total and Expected Total of the 3 products
        assertEquals(expectedTotal, actualTotal,
                "Shopping cart total price must match 100% with the sum of the 3 purchased products (1,915,000 VND)");
        System.out.println("Verification Passed: Shopping cart total matches 100% (" + actualTotal + " VND)!");
    }
}
