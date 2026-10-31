package com.group.cart.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountServiceDecisionTest {
    @Test
    public void testDecisionCoverageAndCatchImplicitElseBug() {
        DiscountService discountService = new DiscountService();
        // 1. Kiểm thử các nhánh điều kiện hạng khách hàng (VIP lớn, VIP nhỏ, MEMBER, Khác)
        assertEquals(0.0, discountService.calculateDiscountAmount(1000.0, "VIP", "", true));
        assertEquals(0.0, discountService.calculateDiscountAmount(900.0, "VIP", "", true));
        assertEquals(0.0, discountService.calculateDiscountAmount(1000.0, "MEMBER", "", true));
        assertEquals(0.0, discountService.calculateDiscountAmount(1000.0, "NORMAL", "", true));

        // 2. Kiểm thử khi ngày thường (isFlashSale = false) để thấy giảm giá đúng
        assertEquals(150.0, discountService.calculateDiscountAmount(1000.0, "VIP", "", false));
        assertEquals(90.0, discountService.calculateDiscountAmount(900.0, "VIP", "", false));
        assertEquals(50.0, discountService.calculateDiscountAmount(1000.0, "MEMBER", "", false));
        assertEquals(0.0, discountService.calculateDiscountAmount(1000.0, "NORMAL", "", false));
        assertEquals(200.0, discountService.calculateDiscountAmount(1000.0, "VIP", "SALE50", false));

        // 3. KÍCH HOẠT NHÁNH FALSE CỦA isFlashSale (isFlashSale = true) để đạt 100% Decision Coverage:
        // [BẮT ĐƯỢC LỖI]: Do isFlashSale = true, maxCap = 0.0, làm tổng giảm giá bị ép về 0.0 thay vì 230.0!
        double flashSaleResult = discountService.calculateDiscountAmount(1200.0, "VIP", "SALE50", true);
        
        // Đoạn assert này chứng minh bẫy lỗi Implicit Else: maxCap = 0 làm mất tiền giảm giá của khách!
        assertEquals(0.0, flashSaleResult, "Lỗi Implicit Else: Khi Flash Sale, maxCap = 0 làm mất tiền giảm giá!");
    }
}

