package com.group.cart.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountServiceStatementTest {
    @Test
    public void testStatementCoverageOnly() {
        DiscountService discountService = new DiscountService();
        // Test Case 1: subtotal <= 0 (chạm vào nhánh bảo vệ)
        assertEquals(0.0, discountService.calculateDiscountAmount(0.0, "VIP", "NORMAL", false));
        // Test Case 2: Khách VIP mua lớn, mã SALE50, ngày thường (isFlashSale = false)
        // Chạm qua toàn bộ các dòng lệnh trong DiscountService bao gồm cả maxCap = subtotal * 0.30
        double result = discountService.calculateDiscountAmount(1200.0, "VIP", "SALE50", false);
        assertEquals(230.0, result); // (1200 * 0.15) + 50 = 230.0
    }
}

