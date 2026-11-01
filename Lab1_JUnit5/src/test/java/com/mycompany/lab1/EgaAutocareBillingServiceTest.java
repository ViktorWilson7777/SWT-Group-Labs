package com.mycompany.lab1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

/**
 * Bộ Unit Test JUnit 5 Data-Driven (đọc dữ liệu từ 7 file CSV) cho Lab 1.
 * Đảm bảo 100% Instruction & Branch Coverage trên toàn bộ 7 hàm của EgaAutocareBillingService.
 */
@TestMethodOrder(OrderAnnotation.class)
public class EgaAutocareBillingServiceTest {

    private final EgaAutocareBillingService billingService = new EgaAutocareBillingService();
    private static final double DELTA = 0.01;

    @Order(1)
    @DisplayName("Hàm 1: Kiểm thử isValidOrderInput từ file CSV")
    @ParameterizedTest(name = "{0}: qty={1}, price={2}, distance={3} => expected={4}")
    @CsvFileSource(resources = "/valid_order_input.csv", numLinesToSkip = 1)
    public void test01_IsValidOrderInput(
            String testCaseId,
            int itemQuantity,
            double unitPrice,
            double distanceKm,
            boolean expectedValid) {
        boolean actual = billingService.isValidOrderInput(itemQuantity, unitPrice, distanceKm);
        assertEquals(expectedValid, actual, "Sai kết quả tại Test Case: " + testCaseId);
    }

    @Order(2)
    @DisplayName("Hàm 2: Kiểm thử calculatePartsSubtotal từ file CSV")
    @ParameterizedTest(name = "{0}: qty={1}, price={2} => expected={3}")
    @CsvFileSource(resources = "/parts_subtotal.csv", numLinesToSkip = 1)
    public void test02_CalculatePartsSubtotal(
            String testCaseId,
            int itemQuantity,
            double unitPrice,
            double expectedSubtotal) {
        double actual = billingService.calculatePartsSubtotal(itemQuantity, unitPrice);
        assertEquals(expectedSubtotal, actual, DELTA, "Sai kết quả tại Test Case: " + testCaseId);
    }

    @Order(3)
    @DisplayName("Hàm 3: Kiểm thử calculateInstallationFee từ file CSV")
    @ParameterizedTest(name = "{0}: pkg={1}, qty={2} => expected={3}")
    @CsvFileSource(resources = "/installation_fee.csv", numLinesToSkip = 1)
    public void test03_CalculateInstallationFee(
            String testCaseId,
            String servicePackage,
            int itemQuantity,
            double expectedFee) {
        double actual = billingService.calculateInstallationFee(servicePackage, itemQuantity);
        assertEquals(expectedFee, actual, DELTA, "Sai kết quả tại Test Case: " + testCaseId);
    }

    @Order(4)
    @DisplayName("Hàm 4: Kiểm thử calculateMemberDiscount từ file CSV")
    @ParameterizedTest(name = "{0}: base={1}, tier={2} => expected={3}")
    @CsvFileSource(resources = "/member_discount.csv", numLinesToSkip = 1)
    public void test04_CalculateMemberDiscount(
            String testCaseId,
            double baseAmount,
            String memberTier,
            double expectedDiscount) {
        double actual = billingService.calculateMemberDiscount(baseAmount, memberTier);
        assertEquals(expectedDiscount, actual, DELTA, "Sai kết quả tại Test Case: " + testCaseId);
    }

    @Order(5)
    @DisplayName("Hàm 5: Kiểm thử calculateShippingFee từ file CSV")
    @ParameterizedTest(name = "{0}: distance={1}, afterDiscount={2} => expected={3}")
    @CsvFileSource(resources = "/shipping_fee.csv", numLinesToSkip = 1)
    public void test05_CalculateShippingFee(
            String testCaseId,
            double distanceKm,
            double amountAfterDiscount,
            double expectedShippingFee) {
        double actual = billingService.calculateShippingFee(distanceKm, amountAfterDiscount);
        assertEquals(expectedShippingFee, actual, DELTA, "Sai kết quả tại Test Case: " + testCaseId);
    }

    @Order(6)
    @DisplayName("Hàm 6: Kiểm thử calculateVatTax từ file CSV")
    @ParameterizedTest(name = "{0}: taxable={1}, vat={2} => expected={3}")
    @CsvFileSource(resources = "/vat_tax.csv", numLinesToSkip = 1)
    public void test06_CalculateVatTax(
            String testCaseId,
            double taxableAmount,
            boolean requireVatInvoice,
            double expectedVat) {
        double actual = billingService.calculateVatTax(taxableAmount, requireVatInvoice);
        assertEquals(expectedVat, actual, DELTA, "Sai kết quả tại Test Case: " + testCaseId);
    }

    @Order(7)
    @DisplayName("Hàm 7: Kiểm thử calculateFinalInvoiceTotal (gọi liên hoàn Hàm 1->6) từ file CSV")
    @ParameterizedTest(name = "{0}: qty={1}, price={2}, pkg={3}, tier={4}, dist={5}, vat={6} => expected={7}")
    @CsvFileSource(resources = "/final_invoice_total.csv", numLinesToSkip = 1)
    public void test07_CalculateFinalInvoiceTotal(
            String testCaseId,
            int itemQuantity,
            double unitPrice,
            String servicePackage,
            String memberTier,
            double distanceKm,
            boolean requireVatInvoice,
            double expectedFinalTotal) {
        double actual = billingService.calculateFinalInvoiceTotal(
                itemQuantity,
                unitPrice,
                servicePackage,
                memberTier,
                distanceKm,
                requireVatInvoice);
        assertEquals(expectedFinalTotal, actual, DELTA, "Sai kết quả tại Test Case: " + testCaseId);
    }
}

