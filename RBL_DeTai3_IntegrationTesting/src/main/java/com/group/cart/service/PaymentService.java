package com.group.cart.service;

import com.group.cart.model.PaymentReceipt;

public class PaymentService {
    private final DiscountService discountService;
    private final PaymentGateway paymentGateway;

    public PaymentService(DiscountService discountService, PaymentGateway paymentGateway) {
        this.discountService = discountService;
        this.paymentGateway = paymentGateway;
    }

    public PaymentService(DiscountService discountService) {
        this(discountService, (accountOrMethod, amount) -> true);
    }

    public PaymentService() {
        this(new DiscountService(), (accountOrMethod, amount) -> true);
    }

    /**
     * HÀM XỬ LÝ THANH TOÁN TRỰC TIẾP (processPayment):
     * Phục vụ kiểm thử đơn vị độc lập và kiểm tra cổng thanh toán PaymentGateway Mock.
     */
    public PaymentReceipt processPayment(double netAmount, String paymentMethod) {
        if (netAmount <= 0.0) {
            throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0");
        }
        double fee = 0.0;
        String status = "SUCCESS";
        String message = "Thanh toán thành công";

        String method = (paymentMethod != null) ? paymentMethod.trim().toUpperCase() : "";

        if ("CREDIT_CARD".equals(method)) {
            fee = netAmount * 0.02; // Phí thẻ 2%
        } else if ("COD".equals(method)) {
            if (netAmount <= 2000.0) {
                fee = 15.0; // Phí COD cố định 15.0
            } else {
                status = "FAILED";
                message = "Đơn hàng vượt quá hạn mức thanh toán COD (tối đa 2,000)";
                return new PaymentReceipt(status, netAmount, 0.0, netAmount, 0.0, 0.0, paymentMethod, message);
            }
        } else if ("E_WALLET".equals(method)) {
            if (netAmount >= 200.0) {
                fee = -10.0; // Giảm 10.0 cho ví điện tử khi đơn >= 200
            } else {
                fee = 0.0;
            }
        } else {
            throw new IllegalArgumentException("Phương thức thanh toán không được hỗ trợ: " + paymentMethod);
        }

        double totalPayable = netAmount + fee;
        if (paymentGateway != null) {
            boolean charged = paymentGateway.charge(method, totalPayable);
            if (!charged) {
                status = "PAYMENT_FAILED";
                message = "Giao dịch qua cổng thanh toán thất bại";
            }
        }

        return new PaymentReceipt(status, netAmount, 0.0, netAmount, fee, totalPayable, paymentMethod, message);
    }

    /**
     * HÀM TRỌNG TÂM TÍCH HỢP HỆ THỐNG (checkout - 6 tham số):
     * Tích hợp Top-Down: CartService -> DiscountService (Stub) -> PaymentGateway (Mock)
     */
    public PaymentReceipt checkout(CartService cart, String customerTier, String couponCode, 
                                   boolean isFlashSale, String paymentMethod, boolean isSpecialDiscount) {
        if (cart == null) {
            throw new IllegalArgumentException("Giỏ hàng không được null");
        }
        double subtotal = cart.calculateSubtotal();
        if (subtotal <= 0.0) {
            throw new IllegalArgumentException("Giỏ hàng rỗng hoặc không hợp lệ");
        }

        // Gọi DiscountService (hoặc Stub trong kiểm thử tích hợp)
        double discount = discountService.calculateDiscountAmount(subtotal, customerTier, couponCode, isFlashSale);
        double netAmount = subtotal - discount;

        double fee = 0.0;
        String status = "SUCCESS";
        String message = "Thanh toán thành công";

        String method = (paymentMethod != null) ? paymentMethod.trim().toUpperCase() : "";

        // Rẽ nhánh phương thức thanh toán
        if ("CREDIT_CARD".equals(method)) {
            fee = netAmount * 0.02;
        } else if ("COD".equals(method)) {
            if (netAmount <= 2000.0) {
                fee = 15.0;
            } else {
                status = "FAILED";
                message = "Đơn hàng vượt quá hạn mức thanh toán COD (tối đa 2,000)";
                return new PaymentReceipt(status, subtotal, discount, netAmount, 0.0, 0.0, paymentMethod, message);
            }
        } else if ("E_WALLET".equals(method)) {
            if (netAmount >= 200.0) {
                fee = -10.0;
            } else {
                fee = 0.0;
            }
        } else {
            throw new IllegalArgumentException("Phương thức thanh toán không được hỗ trợ: " + paymentMethod);
        }

        double totalPayable = netAmount + fee;

        if (paymentGateway != null) {
            boolean charged = paymentGateway.charge(method, totalPayable);
            if (!charged) {
                status = "PAYMENT_FAILED";
                message = "Giao dịch qua cổng thanh toán thất bại";
            }
        }

        return new PaymentReceipt(status, subtotal, discount, netAmount, fee, totalPayable, paymentMethod, message);
    }

    /**
     * HÀM TRỌNG TÂM TÍCH HỢP (checkout - 4 tham số mặc định)
     */
    public PaymentReceipt checkout(CartService cart, String customerTier, String couponCode, String paymentMethod) {
        return checkout(cart, customerTier, couponCode, false, paymentMethod, false);
    }
}
