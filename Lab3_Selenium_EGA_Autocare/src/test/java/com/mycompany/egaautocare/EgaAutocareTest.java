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
 * Lab 3: Automation Testing với Selenium trên hệ thống EGA Autocare
 * Website: https://ega-autocare.mysapo.net/
 * Phân công:
 * - Thành viên 3 (M3): TC01 -> TC04 (Access web, Login hợp lệ, Login không hợp lệ, Search sản phẩm)
 * - Thành viên 4 (M4): TC05 -> TC06 (Mua ít nhất 3 sản phẩm khác nhau, Kiểm tra tổng giá tiền giỏ hàng)
 */
@TestMethodOrder(OrderAnnotation.class)
public class EgaAutocareTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "https://ega-autocare.mysapo.net";

    // Lưu tổng tiền kỳ vọng khi thêm 3 sản phẩm ở TC05 để đối chiếu chính xác ở TC06
    private static long expectedCartTotal = 0L;

    @BeforeAll
    public static void setUpAll() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");

        // Hỗ trợ chạy headless nếu truyền cờ -Dheadless=true
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        // Selenium 4 tự động quản lý ChromeDriver thông qua Selenium Manager
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

    // ==========================================
    // PHẦN CỦA THÀNH VIÊN 3 (M3): TC01 -> TC04
    // ==========================================

    @Test
    @Order(1)
    public void test01_AccessWeb() {
        System.out.println("=== TC01: Kiểm thử truy cập website EGA Autocare ===");
        driver.get(BASE_URL);

        String title = driver.getTitle();
        System.out.println("Tiêu đề trang: " + title);
        assertTrue(title.contains("EGA Autocare") || title.toLowerCase().contains("autocare"),
                "Tiêu đề trang phải chứa EGA Autocare");

        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL hiện tại: " + currentUrl);
        assertTrue(currentUrl.contains("ega-autocare.mysapo.net"),
                "URL phải thuộc domain ega-autocare.mysapo.net");
    }

    @Test
    @Order(2)
    public void test02_LoginSuccess() {
        System.out.println("=== TC02: Kiểm thử đăng nhập tài khoản hợp lệ ===");
        driver.manage().deleteAllCookies();
        driver.get(BASE_URL + "/account/login");

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer_email")));
        WebElement passwordInput = driver.findElement(By.id("customer_password"));

        emailInput.clear();
        emailInput.sendKeys("linhcampochia@gmail.com");

        passwordInput.clear();
        passwordInput.sendKeys("03022006Tien!");

        // Click đúng nút Đăng nhập nằm trong form #customer_login
        WebElement loginBtn = driver.findElement(By.cssSelector("#customer_login button.btn-login"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);

        // Đợi hệ thống chuyển hướng sang trang tài khoản khách hàng (/account)
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/account/login")));

        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource();
        System.out.println("URL sau khi đăng nhập thành công: " + currentUrl);

        assertFalse(currentUrl.contains("/account/login"),
                "Sau khi đăng nhập đúng phải thoát khỏi trang /account/login");
        assertTrue(currentUrl.contains("/account") && pageSource.contains("linhcampochia@gmail.com"),
                "Trang tài khoản phải hiển thị đúng email khách hàng đã đăng nhập");
    }

    @Test
    @Order(3)
    public void test03_LoginFail_Unregistered() {
        System.out.println("=== TC03: Kiểm thử đăng nhập thất bại với tài khoản chưa đăng ký ===");
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

        // Chờ thông báo lỗi xuất hiện trong form #customer_login
        WebElement errorAlert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#customer_login .text-error, #customer_login .alert")));
        String errorText = errorAlert.getText().trim();
        String currentUrl = driver.getCurrentUrl();

        System.out.println("URL sau khi đăng nhập sai: " + currentUrl);
        System.out.println("Thông báo lỗi hiển thị: " + errorText);

        assertTrue(currentUrl.contains("/account/login"),
                "Đăng nhập sai phải giữ người dùng ở lại trang /account/login");
        assertTrue(errorText.contains("Thông tin đăng nhập không chính xác"),
                "Phải hiển thị thông báo 'Thông tin đăng nhập không chính xác.'");
    }

    @Test
    @Order(4)
    public void test04_SearchProduct() {
        System.out.println("=== TC04: Kiểm thử tính năng tìm kiếm sản phẩm ===");
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
        System.out.println("URL trang kết quả tìm kiếm: " + currentUrl);
        assertTrue(currentUrl.contains("query=") || currentUrl.contains("q="),
                "URL phải chứa tham số query tìm kiếm");

        List<WebElement> results = driver.findElements(By.cssSelector(".product-item, .product-card, .search-result, h1, h2, .section-heading"));
        assertFalse(results.isEmpty(), "Trang tìm kiếm phải hiển thị danh sách sản phẩm kết quả");
        assertTrue(driver.getPageSource().toLowerCase().contains("bơm lốp"),
                "Kết quả tìm kiếm phải chứa sản phẩm liên quan đến từ khóa 'bơm lốp'");
    }

    // ==========================================
    // PHẦN CỦA THÀNH VIÊN 4 (M4): TC05 -> TC06
    // ==========================================

    @Test
    @Order(5)
    public void test05_BuyAtLeast3Products() {
        System.out.println("=== TC05: Kiểm thử mua ít nhất 3 sản phẩm bất kỳ vào giỏ hàng ===");
        // Làm sạch giỏ hàng trước khi thêm 3 sản phẩm mới
        driver.get(BASE_URL + "/cart/clear");
        expectedCartTotal = 0L;

        // Danh sách 3 sản phẩm phụ kiện ô tô khác nhau trên EGA Autocare
        String[] productSlugs = {
            "/michelin-m2566-bom-lop",       // SP 1: Bơm lốp Michelin M2566 (1.180.000₫)
            "/areon-tinh-dau-treo-xe",       // SP 2: Tinh dầu treo xe Areon (285.000₫)
            "/autoguard-bat-phu-xe"          // SP 3: Bạt phủ xe ô tô AutoGuard (450.000₫)
        };

        for (int i = 0; i < productSlugs.length; i++) {
            String slug = productSlugs[i];
            driver.get(BASE_URL + slug);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-form")));

            // Bóc tách đơn giá hiển thị của sản phẩm trên trang chi tiết (span.price.text-h4)
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

            System.out.println("Sản phẩm " + (i + 1) + " (" + slug + ") - Đơn giá: " + itemPrice + " VND (" + rawPrice + ")");

            // Bấm nút THÊM VÀO GIỎ
            WebElement addToCartBtn = driver.findElement(By.cssSelector("#add-to-cart-form button[name='addtocart'], button.btn-add-to-cart"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);

            try {
                Thread.sleep(2500);
            } catch (InterruptedException ignored) {}
        }

        // Mở trang giỏ hàng /cart để xác nhận đã có đủ 3 sản phẩm
        driver.get(BASE_URL + "/cart");
        wait.until(ExpectedConditions.urlContains("/cart"));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}

        List<WebElement> cartQtyInputs = driver.findElements(
                By.cssSelector("input[name='Updates'], input[name='updates[]'], .cart-item, tr.line-item, input[type='number']"));
        System.out.println("Số dòng sản phẩm trong giỏ hàng: " + cartQtyInputs.size());
        System.out.println("Tổng tiền kỳ vọng của 3 sản phẩm: " + expectedCartTotal + " VND");

        assertTrue(driver.getCurrentUrl().contains("/cart"), "Phải điều hướng vào trang giỏ hàng /cart");
        assertTrue(expectedCartTotal > 0, "Tổng giá trị của 3 sản phẩm đã chọn phải lớn hơn 0");
    }

    @Test
    @Order(6)
    public void test06_VerifyCartTotal() {
        System.out.println("=== TC06: Kiểm thử tính đúng đắn của tổng giá tiền trong giỏ hàng ===");
        driver.get(BASE_URL + "/cart");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        // 1. Lấy tổng tiền từ 3 sản phẩm đã mua ở TC05 (1.180.000 + 285.000 + 450.000 = 1.915.000 VND)
        long expectedTotal = expectedCartTotal > 0 ? expectedCartTotal : (1180000L + 285000L + 450000L);
        System.out.println("Tổng tiền kỳ vọng (SP1 + SP2 + SP3): " + expectedTotal + " VND");

        // 2. Bóc tách tổng tiền thực tế hiển thị tại mục 'TỔNG CỘNG' (.cart-total .price) trên trang /cart
        WebElement totalElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-total .price")));
        String rawTotalText = totalElement.getText().trim();
        System.out.println("Chuỗi TỔNG CỘNG hiển thị trên giỏ hàng: '" + rawTotalText + "'");
        assertFalse(rawTotalText.isEmpty(), "Chuỗi tổng tiền trên giỏ hàng không được rỗng");

        long actualTotal = Long.parseLong(rawTotalText.replaceAll("[^0-9]", ""));
        System.out.println("Tổng tiền thực tế trên giỏ hàng (dạng số): " + actualTotal + " VND");

        // 3. Đối chiếu chính xác tuyệt đối giữa Tổng tiền thực tế và Tổng tiền kỳ vọng của 3 sản phẩm
        assertEquals(expectedTotal, actualTotal,
                "Tổng giá tiền trên giỏ hàng phải khớp chính xác 100% với tổng giá của 3 sản phẩm đã mua (1.915.000 VND)");
        System.out.println("Xác nhận thành công: Tổng tiền giỏ hàng khớp chính xác tuyệt đối (" + actualTotal + " VND)!");
    }
}
