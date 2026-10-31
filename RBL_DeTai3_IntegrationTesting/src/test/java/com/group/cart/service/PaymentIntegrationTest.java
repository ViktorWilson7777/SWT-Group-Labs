package com.group.cart.service;

import com.group.cart.model.CartItem;
import com.group.cart.model.PaymentReceipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentIntegrationTest {

    @Mock
    private PaymentGateway paymentGateway;

    // =========================================================================
    // SUITE 1: UNIT & STANDALONE PAYMENT GATEWAY VERIFICATION (processPayment)
    // =========================================================================
    @Nested
    @DisplayName("Suite 1: Mockito Gateway Verification & Payment Processing")
    class StandalonePaymentGatewaySuite {

        private PaymentService paymentService;

        @BeforeEach
        public void setUp() {
            paymentService = new PaymentService(new DiscountService(), paymentGateway);
        }

        @Test
        @DisplayName("TC-PAY-01: Thanh toán Credit Card thành công với Mock PaymentGateway")
        public void testCreditCard_Success() {
            when(paymentGateway.charge(eq("CREDIT_CARD"), anyDouble())).thenReturn(true);
            PaymentReceipt receipt = paymentService.processPayment(500.0, "CREDIT_CARD");
            assertEquals("SUCCESS", receipt.getStatus());
            assertEquals(10.0, receipt.getFee(), 0.001); // 2% của 500 = 10.0
            assertEquals(510.0, receipt.getTotalAmount(), 0.001);
            verify(paymentGateway, times(1)).charge("CREDIT_CARD", 510.0);
        }

        @Test
        @DisplayName("TC-PAY-02: Thanh toán COD thành công trong hạn mức")
        public void testCOD_SuccessWithinLimit() {
            when(paymentGateway.charge(eq("COD"), anyDouble())).thenReturn(true);
            PaymentReceipt receipt = paymentService.processPayment(1500.0, "COD");
            assertEquals("SUCCESS", receipt.getStatus());
            assertEquals(15.0, receipt.getFee(), 0.001);
            assertEquals(1515.0, receipt.getTotalAmount(), 0.001);
            verify(paymentGateway, times(1)).charge("COD", 1515.0);
        }

        @Test
        @DisplayName("TC-PAY-03: Thanh toán COD vượt quá hạn mức 2000.0 bị từ chối")
        public void testCOD_ExceedLimit_Fails() {
            PaymentReceipt receipt = paymentService.processPayment(2500.0, "COD");
            assertEquals("FAILED", receipt.getStatus());
            assertTrue(receipt.getMessage().contains("vượt quá hạn mức"));
            verifyNoInteractions(paymentGateway);
        }

        @Test
        @DisplayName("TC-PAY-04: Thanh toán E-Wallet trên 200.0 được giảm phí 10.0")
        public void testEWallet_DiscountApplied() {
            when(paymentGateway.charge(eq("E_WALLET"), anyDouble())).thenReturn(true);
            PaymentReceipt receipt = paymentService.processPayment(300.0, "E_WALLET");
            assertEquals("SUCCESS", receipt.getStatus());
            assertEquals(-10.0, receipt.getFee(), 0.001);
            assertEquals(290.0, receipt.getTotalAmount(), 0.001);
            verify(paymentGateway, times(1)).charge("E_WALLET", 290.0);
        }

        @Test
        @DisplayName("TC-PAY-05: Ngoại lệ khi phương thức thanh toán không hợp lệ")
        public void testInvalidMethod_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                paymentService.processPayment(100.0, "BITCOIN");
            });
            verifyNoInteractions(paymentGateway);
        }

        @Test
        @DisplayName("TC-PAY-06: Ngoại lệ khi số tiền netAmount <= 0")
        public void testInvalidAmount_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                paymentService.processPayment(-50.0, "CREDIT_CARD");
            });
            verifyNoInteractions(paymentGateway);
        }

        @Test
        @DisplayName("TC-PAY-07: Cổng thanh toán từ chối giao dịch (charge trả về false)")
        public void testGatewayChargeDeclined() {
            when(paymentGateway.charge(anyString(), anyDouble())).thenReturn(false);
            PaymentReceipt receipt = paymentService.processPayment(500.0, "CREDIT_CARD");
            assertEquals("PAYMENT_FAILED", receipt.getStatus());
            assertTrue(receipt.getMessage().contains("thất bại"));
        }
    }

    // =========================================================================
    // PHẦN BỔ SUNG BẮT BUỘC CHO THÀNH VIÊN 4:
    // KIỂM THỬ TÍCH HỢP TOP-DOWN (CartService + DiscountService Stub + PaymentService)
    // VÀ PHÁT HIỆN LỖI GIAO TIẾP (INTERFACE DEFECTS)
    // =========================================================================
    @Nested
    @DisplayName("Suite 2: Top-Down Component Integration Testing & Interface Defects (checkout)")
    class CheckoutModuleIntegrationSuite {

        @Mock
        private DiscountService discountServiceStub;

        private CartService cartService;
        private PaymentService integratedPaymentService;

        @BeforeEach
        public void setUpIntegration() {
            // Module 1: Khởi tạo giỏ hàng thực tế (đã được kiểm thử TDD ở bước trước)
            cartService = new CartService();
            cartService.addItem(new CartItem("P01", "Gaming Laptop", 1000.0, 1)); // Subtotal = 1000.0

            // Tích hợp Top-Down: Tiêm Stub của DiscountService (Module 2) và PaymentGateway vào PaymentService (Module 3)
            integratedPaymentService = new PaymentService(discountServiceStub, paymentGateway);
        }

        @Test
        @DisplayName("TC-INT-01: Tích hợp Top-Down dùng Mockito Stub (Canned Response) cho DiscountService")
        public void testTopDownIntegration_WithDiscountStub_Success() {
            // Giả lập Stub (Tier 2 - Hardcoded/Computed Reply theo Slide SWT2):
            // Khi PaymentService gọi xuống DiscountService với đúng thứ tự tham số (1000.0, "VIP", "SALE50", false),
            // Stub trả về mức giảm giá định sẵn là 200.0
            when(discountServiceStub.calculateDiscountAmount(1000.0, "VIP", "SALE50", false))
                    .thenReturn(200.0);
            when(paymentGateway.charge(anyString(), eq(816.0))).thenReturn(true);

            // Thực thi hàm tích hợp checkout
            PaymentReceipt receipt = integratedPaymentService.checkout(
                    cartService, "VIP", "SALE50", false, "CREDIT_CARD", false
            );

            // Kiểm chứng sự phối hợp dữ liệu giữa các module:
            // Subtotal (1000.0) - Discount từ Stub (200.0) = 800.0
            // Phí CREDIT_CARD nội địa 2% của 800.0 = 16.0 -> Tổng thanh toán = 816.0
            assertEquals(16.0, receipt.getFee(), 0.001);
            assertEquals(816.0, receipt.getTotalAmount(), 0.001);
            assertEquals("SUCCESS", receipt.getStatus());

            // Xác thực giao tiếp (Interface Verification): Đảm bảo tham số truyền qua giao diện đúng thứ tự
            verify(discountServiceStub, times(1))
                    .calculateDiscountAmount(eq(1000.0), eq("VIP"), eq("SALE50"), eq(false));
        }

        @Test
        @DisplayName("TC-INT-02: Phát hiện Lỗi Giao tiếp 1 (Parameter Ordering Defect) khi truyền ngược customerTier và couponCode")
        public void testInterfaceDefect_ParameterOrderingMismatch() {
            // Tích hợp thực tế với DiscountService thật để kiểm tra tương tác dữ liệu
            DiscountService realDiscountService = new DiscountService();
            PaymentService realIntegratedService = new PaymentService(realDiscountService, paymentGateway);
            when(paymentGateway.charge(anyString(), anyDouble())).thenReturn(true);

            // Kịch bản lỗi giao tiếp (Interface Defect):
            // Giả sử ở phía gọi hoặc trong hàm tích hợp, thứ tự 2 tham số String bị truyền ngược:
            // truyền ("SALE50", "VIP") thay vì ("VIP", "SALE50").
            // Khi đó: customerTier nhận giá trị "SALE50" (rơi vào nhánh else -> rate = 0.0)
            //         couponCode nhận giá trị "VIP" (không khớp "SALE50" -> cộng 0.0)
            // Hậu quả: Tổng giảm giá bằng 0.0 thay vì 200.0, khiến khách bị thu 1020.0 thay vì 816.0!
            PaymentReceipt receiptWithSwappedArgs = realIntegratedService.checkout(
                    cartService, "SALE50", "VIP", false, "CREDIT_CARD", false
            );

            // Ghi nhận hiện tượng lỗi giao tiếp làm sai lệch số tiền thanh toán:
            assertNotEquals(816.0, receiptWithSwappedArgs.getTotalAmount(),
                    "INTERFACE DEFECT: Truyền ngược thứ tự 2 tham số String (customerTier, couponCode) làm mất toàn bộ giảm giá!");
            assertEquals(1020.0, receiptWithSwappedArgs.getTotalAmount(), 0.001);
        }

        @Test
        @DisplayName("TC-INT-03: Kiểm thử biên giao tiếp (Boundary Interface Contract) khi giỏ hàng rỗng hoặc null")
        public void testCheckoutIntegration_EmptyOrNullCart_ThrowsException() {
            CartService emptyCart = new CartService();
            assertThrows(IllegalArgumentException.class, () -> {
                integratedPaymentService.checkout(emptyCart, "VIP", "SALE50", false, "CREDIT_CARD", false);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                integratedPaymentService.checkout(null, "VIP", "SALE50", false, "CREDIT_CARD", false);
            });

            // Đảm bảo khi vi phạm hợp đồng đầu vào từ CartService, hệ thống ngắt ngay và không gọi xuống các module dưới
            verifyNoInteractions(discountServiceStub);
            verifyNoInteractions(paymentGateway);
        }
    }
}
