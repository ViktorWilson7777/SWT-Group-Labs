package com.mycompany.lab1;

/**
 * Lab 1 - JUnit 5: Automotive Parts & Maintenance Billing Service for EGA Autocare.
 * Consists of 7 interconnected methods, each containing conditional if-else branches.
 * Author: M1 - Nguyen Hoang Vinh (Leader)
 */
public class EgaAutocareBillingService {

    /**
     * Function 1: Validate order input parameters (quantity, unit price, and delivery distance).
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
     * Function 2: Calculate spare parts subtotal (applies bulk discounts for >= 4 items or >= 10 items).
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
     * Function 3: Calculate garage installation labor fee by service package (NONE, PREMIUM, STANDARD, or BASIC).
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
     * Function 4: Calculate loyalty membership discount amount by tier (VIP, GOLD, SILVER, REGULAR).
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
     * Function 5: Calculate delivery shipping fee (free for garage pickup or net order >= 2,000,000 VND).
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
     * Function 6: Calculate 8% Value-Added Tax (VAT) when a VAT invoice is requested.
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
     * Function 7: Orchestrator method chaining Functions 1 through 6 to compute the final invoice total.
     * High-value invoices (>= 10,000,000 VND) receive an additional 200,000 VND direct rebate.
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
