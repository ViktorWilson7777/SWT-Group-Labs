package com.mycompany.lab1;

/**
 * Lab 1 - JUnit 5: Hệ thống tính hóa đơn mua phụ tùng & lắp đặt bảo dưỡng ô tô EGA Autocare.
 * Gồm 7 hàm liên kết chặt chẽ với nhau, mỗi hàm đều có điều kiện rẽ nhánh if-else.
 */
public class EgaAutocareBillingService {

    /**
     * Hàm 1: Kiểm tra tính hợp lệ của thông tin đầu vào đơn hàng phụ tùng.
     */
    public boolean isValidOrderInput(int itemQuantity, double unitPrice, double distanceKm) {
        if (itemQuantity <= 0 || itemQuantity > 100) {
            return false;
        } else if (unitPrice <= 0 || distanceKm < 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Hàm 2: Tính tổng tiền phụ tùng (áp dụng chiết khấu khi mua theo bộ từ 4 món hoặc mua sỉ từ 10 món).
     */
    public double calculatePartsSubtotal(int itemQuantity, double unitPrice) {
        if (itemQuantity <= 0 || unitPrice <= 0) {
            return 0.0;
        } else if (itemQuantity >= 10) {
            return itemQuantity * unitPrice * 0.90;
        } else if (itemQuantity >= 4) {
            return itemQuantity * unitPrice * 0.95;
        } else {
            return itemQuantity * unitPrice;
        }
    }

    /**
     * Hàm 3: Tính phí công lắp đặt tại gara EGA Autocare theo gói dịch vụ (NONE, STANDARD, PREMIUM, hoặc gói cơ bản).
     */
    public double calculateInstallationFee(String servicePackage, int itemQuantity) {
        if (itemQuantity <= 0 || "NONE".equalsIgnoreCase(servicePackage)) {
            return 0.0;
        } else if ("PREMIUM".equalsIgnoreCase(servicePackage)) {
            return itemQuantity * 150000.0;
        } else if ("STANDARD".equalsIgnoreCase(servicePackage)) {
            return itemQuantity * 80000.0;
        } else {
            return itemQuantity * 50000.0;
        }
    }

    /**
     * Hàm 4: Tính số tiền giảm giá theo hạng thẻ thành viên (VIP, GOLD, SILVER, REGULAR).
     */
    public double calculateMemberDiscount(double baseAmount, String memberTier) {
        if (baseAmount <= 0) {
            return 0.0;
        } else if ("VIP".equalsIgnoreCase(memberTier)) {
            return baseAmount * 0.15;
        } else if ("GOLD".equalsIgnoreCase(memberTier)) {
            return baseAmount * 0.10;
        } else if ("SILVER".equalsIgnoreCase(memberTier)) {
            return baseAmount * 0.05;
        } else {
            return 0.0;
        }
    }

    /**
     * Hàm 5: Tính phí giao phụ tùng tận nơi (miễn phí khi nhận tại gara hoặc đơn sau giảm >= 2.000.000đ).
     */
    public double calculateShippingFee(double distanceKm, double amountAfterDiscount) {
        if (distanceKm <= 0 || amountAfterDiscount >= 2000000.0) {
            return 0.0;
        } else if (distanceKm <= 10.0) {
            return 30000.0;
        } else {
            return 30000.0 + (distanceKm - 10.0) * 5000.0;
        }
    }

    /**
     * Hàm 6: Tính thuế GTGT (VAT 8%) khi khách hàng yêu cầu xuất hóa đơn VAT.
     */
    public double calculateVatTax(double taxableAmount, boolean requireVatInvoice) {
        if (taxableAmount <= 0) {
            return 0.0;
        } else if (requireVatInvoice) {
            return taxableAmount * 0.08;
        } else {
            return 0.0;
        }
    }

    /**
     * Hàm 7: Hàm tổng hợp điều phối gọi liên hoàn từ Hàm 1 đến Hàm 6 để tính tổng thanh toán cuối cùng.
     * Hóa đơn lớn từ 10.000.000đ trở lên được tặng thêm ưu đãi giảm trực tiếp 200.000đ.
     */
    public double calculateFinalInvoiceTotal(
            int itemQuantity,
            double unitPrice,
            String servicePackage,
            String memberTier,
            double distanceKm,
            boolean requireVatInvoice) {
        if (!isValidOrderInput(itemQuantity, unitPrice, distanceKm)) {
            return -1.0;
        } else {
            double partsSubtotal = calculatePartsSubtotal(itemQuantity, unitPrice);
            double installationFee = calculateInstallationFee(servicePackage, itemQuantity);
            double baseAmount = partsSubtotal + installationFee;

            double memberDiscount = calculateMemberDiscount(baseAmount, memberTier);
            double amountAfterDiscount = baseAmount - memberDiscount;

            double shippingFee = calculateShippingFee(distanceKm, amountAfterDiscount);
            double vatTax = calculateVatTax(amountAfterDiscount, requireVatInvoice);

            double finalTotal = amountAfterDiscount + shippingFee + vatTax;

            if (finalTotal >= 10000000.0) {
                return Math.round((finalTotal - 200000.0) * 100.0) / 100.0;
            } else {
                return Math.round(finalTotal * 100.0) / 100.0;
            }
        }
    }
}

