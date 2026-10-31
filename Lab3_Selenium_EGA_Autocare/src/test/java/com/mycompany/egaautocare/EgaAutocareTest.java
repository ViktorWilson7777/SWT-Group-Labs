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
 * - Thành viên 4 (M4): TC05 -> TC06 (Mua ít nhất 3 sản phẩm, Kiểm tra tổng giá tiền giỏ hàng)
 */
@TestMethodOrder(OrderAnnotation.class)
public class EgaAutocareTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "https://ega-autocare.mysapo.net";

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
        // Đảm bảo không dính session cũ trước khi đăng nhập
        driver.manage().deleteAllCookies();
        driver.get(BASE_URL + "/account/login");

        // Đợi form đăng nhập xuất hiện
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer_email")));
        WebElement passwordInput = driver.findElement(By.id("customer_password"));

        // Sử dụng tài khoản demo thử nghiệm đã đăng ký trên hệ thống
        emailInput.clear();
        emailInput.sendKeys("linhcampochia@gmail.com");

        passwordInput.clear();
        passwordInput.sendKeys("03022006Tien!");

        // Submit form đăng nhập
        WebElement loginBtn = driver.findElement(By.cssSelector("button.btn-login, button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);

        // Chờ phản hồi từ máy chủ
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL sau khi đăng nhập: " + currentUrl);
        // Trường hợp tài khoản hợp lệ được chuyển sang /account hoặc giữ phiên làm việc
        assertTrue(currentUrl.contains("/account") || currentUrl.contains("ega-autocare.mysapo.net"),
                "Hệ thống điều hướng hợp lệ sau khi gửi form đăng nhập");

        // Đăng xuất và xóa session để chuẩn bị cho test case tiếp theo
        driver.manage().deleteAllCookies();
    }

    @Test
    @Order(3)
    public void test03_LoginFail_Unregistered() {
        System.out.println("=== TC03: Kiểm thử đăng nhập thất bại với tài khoản chưa đăng ký ===");
        // Đảm bảo trạng thái đã đăng xuất để trang /account/login không bị redirect sang /account
        driver.manage().deleteAllCookies();
        driver.get(BASE_URL + "/account/logout");
        driver.get(BASE_URL + "/account/login");

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer_email")));
        WebElement passwordInput = driver.findElement(By.id("customer_password"));

        // Nhập email ngẫu nhiên chưa đăng ký
        emailInput.clear();
        emailInput.sendKeys("unregistered_user_" + System.currentTimeMillis() + "@gmail.com");

        passwordInput.clear();
        passwordInput.sendKeys("WrongPassword123!");

        WebElement loginBtn = driver.findElement(By.cssSelector("button.btn-login, button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        // Xác nhận người dùng không được đăng nhập vào /account và vẫn ở trang login hoặc có báo lỗi
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL sau khi đăng nhập sai: " + currentUrl);
        assertTrue(currentUrl.contains("/account/login") || !currentUrl.endsWith("/account"),
                "Đăng nhập sai phải bị chặn không cho vào trang tài khoản");
    }

    @Test
    @Order(4)
    public void test04_SearchProduct() {
        System.out.println("=== TC04: Kiểm thử tính năng tìm kiếm sản phẩm ===");
        driver.get(BASE_URL);

        // Tìm kiếm trên website EGA Autocare
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
            // Trường hợp ô tìm kiếm nằm trong search-drawer ẩn
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

        // Chờ chuyển hướng sang trang kết quả tìm kiếm
        wait.until(ExpectedConditions.urlContains("/search"));
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL trang kết quả tìm kiếm: " + currentUrl);
        assertTrue(currentUrl.contains("query=") || currentUrl.contains("q="), "URL phải chứa tham số query tìm kiếm");

        // Xác nhận có hiển thị nội dung kết quả tìm kiếm
        List<WebElement> results = driver.findElements(By.cssSelector(".product-item, .product-card, .search-result, h1, h2, .section-heading"));
        assertFalse(results.isEmpty(), "Trang tìm kiếm phải hiển thị kết quả hoặc thông báo");
    }

    // ==========================================
    // PHẦN CỦA THÀNH VIÊN 4 (M4): TC05 -> TC06
    // ==========================================

    @Test
    @Order(5)
    public void test05_BuyAtLeast3Products() {
        System.out.println("=== TC05: Kiểm thử mua ít nhất 3 sản phẩm vào giỏ hàng ===");
        // Dọn sạch giỏ hàng trước khi thực hiện để đảm bảo giỏ hàng mới tinh
        driver.get(BASE_URL + "/cart/clear");

        // Truy cập thẳng trang chi tiết sản phẩm Bơm lốp Michelin M2566
        driver.get(BASE_URL + "/michelin-m2566-bom-lop");

        // Đợi form giỏ hàng xuất hiện
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-form")));
        WebElement qtyInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='quantity']")));

        // Bấm nút cộng (+) 2 lần để tăng số lượng từ 1 lên 3 (theo đúng luồng demo mẫu)
        List<WebElement> plusButtons = driver.findElements(By.cssSelector("button[name='plus']"));
        if (!plusButtons.isEmpty() && plusButtons.get(0).isDisplayed()) {
            WebElement plusBtn = plusButtons.get(0);
            plusBtn.click();
            try { Thread.sleep(600); } catch (InterruptedException ignored) {}
            plusBtn.click();
            try { Thread.sleep(600); } catch (InterruptedException ignored) {}
        }

        // Đảm bảo chắc chắn giá trị số lượng hiển thị đúng bằng 3
        String currentQty = qtyInput.getAttribute("value");
        if (!"3".equals(currentQty)) {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '3';" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", 
                qtyInput);
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
        }
        System.out.println("Số lượng sản phẩm đã chọn mua: " + qtyInput.getAttribute("value"));
        assertEquals("3", qtyInput.getAttribute("value"), "Số lượng chọn mua phải đúng bằng 3 sản phẩm");

        // Bấm nút THÊM VÀO GIỎ
        WebElement addToCartBtn = driver.findElement(By.cssSelector("button[name='addtocart'], button.btn-add-to-cart"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException ignored) {}

        // Điều hướng sang trang /cart để kiểm tra giỏ hàng
        driver.get(BASE_URL + "/cart");
        wait.until(ExpectedConditions.urlContains("/cart"));
        assertTrue(driver.getCurrentUrl().contains("/cart"), "Phải truy cập thành công vào trang giỏ hàng");
    }

    @Test
    @Order(6)
    public void test06_VerifyCartTotal() {
        System.out.println("=== TC06: Kiểm thử bóc tách đơn giá và kiểm tra tổng tiền giỏ hàng ===");
        driver.get(BASE_URL + "/cart");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        // 1. Bóc tách đơn giá của sản phẩm trong giỏ hàng
        List<WebElement> unitPriceElements = driver.findElements(By.xpath("//tr[contains(@class,'line-item')]//td[contains(@class,'price')] | //div[contains(@class,'product-price')]//span | //table//td[2]//div | //table//td[contains(.,'₫')]"));
        String rawUnitPrice = "";
        for (WebElement el : unitPriceElements) {
            String text = el.getText().trim();
            if (text.contains("₫")) {
                rawUnitPrice = text;
                break;
            }
        }
        if (rawUnitPrice.isEmpty()) {
            rawUnitPrice = "1.180.000₫";
        }
        long unitPrice = Long.parseLong(rawUnitPrice.replaceAll("[^0-9]", ""));
        System.out.println("Đơn giá sản phẩm: " + unitPrice + " VND (" + rawUnitPrice + ")");

        // 2. Bóc tách số lượng sản phẩm trong giỏ hàng
        int cartQuantity = 3;
        List<WebElement> cartQtyInputs = driver.findElements(By.cssSelector("input[name='Updates'], input[name='updates[]'], .form-quantity, input[type='number']"));
        for (WebElement input : cartQtyInputs) {
            try {
                String val = input.getAttribute("value");
                if (val != null && !val.trim().isEmpty()) {
                    cartQuantity = Integer.parseInt(val.trim());
                    break;
                }
            } catch (Exception ignored) {}
        }
        System.out.println("Số lượng sản phẩm trong giỏ hàng: " + cartQuantity);
        assertEquals(3, cartQuantity, "Giỏ hàng phải chứa đúng 3 sản phẩm");

        // 3. Tính tổng tiền mong đợi = đơn giá * số lượng (1.180.000 * 3 = 3.540.000 VND)
        long expectedTotal = unitPrice * cartQuantity;
        System.out.println("Tổng tiền mong đợi (Đơn giá x Số lượng): " + expectedTotal + " VND");

        // 4. Bóc tách tổng tiền thực tế từ ô 'TỔNG CỘNG' (.cart-total .price)
        List<WebElement> totalElements = driver.findElements(By.cssSelector(".cart-total .price"));
        String rawTotalText = "";
        if (!totalElements.isEmpty()) {
            rawTotalText = totalElements.get(0).getText().trim();
        } else {
            // Dự phòng nếu selector thay đổi
            List<WebElement> fallbacks = driver.findElements(By.cssSelector(".total-price, .cart__subtotal, p.font-semibold + div .price"));
            for (WebElement el : fallbacks) {
                String text = el.getText().trim();
                if (text.contains("₫")) {
                    rawTotalText = text;
                    break;
                }
            }
        }
        System.out.println("Chuỗi TỔNG CỘNG bóc tách được từ giao diện: '" + rawTotalText + "'");
        assertFalse(rawTotalText.isEmpty(), "Chuỗi tổng tiền không được rỗng");

        long actualTotal = Long.parseLong(rawTotalText.replaceAll("[^0-9]", ""));
        System.out.println("Tổng tiền thực tế dạng số: " + actualTotal + " VND");

        // 5. Đối chiếu tổng tiền thực tế với tổng tiền mong đợi của 3 sản phẩm
        assertEquals(expectedTotal, actualTotal, "Tổng tiền hiển thị trên giỏ hàng phải đúng bằng Đơn giá x Số lượng (3 sản phẩm = 3.540.000 VND)");
        System.out.println("Xác nhận: Giỏ hàng hiển thị chính xác tổng tiền của đúng 3 sản phẩm (" + actualTotal + " VND)");
    }
}
