const path = require('path');
const { calculateServiceInvoice } = require('../src/services/discountService');
const { loadCsvData } = require('../src/utils/csvLoader');

describe('Lab 2 - Requirement (iv): Test Hàm có điều kiện if-else (discountService)', () => {
  const csvPath = path.resolve(__dirname, 'fixtures/discount_test_data.csv');
  const testCases = loadCsvData(csvPath);

  describe('Data-Driven Testing from CSV (test.each)', () => {
    test.each(testCases)(
      'Data-Driven Case $testId: Tier=$customerTier, Cost=$baseCost, Voucher=$voucherCode, FirstTime=$isFirstTime',
      ({
        testId,
        customerTier,
        baseCost,
        voucherCode,
        isFirstTime,
        hasVatInvoice,
        expectedDiscountRate,
        expectedVoucherDiscount,
        expectedTotalDiscount,
        expectedIsCapped,
        expectedVat,
        expectedFinalCost,
        expectedError
      }) => {
        const costNum = parseFloat(baseCost);
        const firstTimeBool = isFirstTime === 'true';
        const vatBool = hasVatInvoice === 'true';

        if (expectedError) {
          expect(() => {
            calculateServiceInvoice({
              customerTier,
              baseCost: costNum,
              voucherCode,
              isFirstTime: firstTimeBool,
              hasVatInvoice: vatBool
            });
          }).toThrow(expectedError);
        } else {
          const result = calculateServiceInvoice({
            customerTier,
            baseCost: costNum,
            voucherCode,
            isFirstTime: firstTimeBool,
            hasVatInvoice: vatBool
          });

          expect(result.discountRate).toBeCloseTo(parseFloat(expectedDiscountRate), 2);
          expect(result.voucherDiscount).toBe(parseInt(expectedVoucherDiscount, 10));
          expect(result.totalDiscount).toBe(parseInt(expectedTotalDiscount, 10));
          expect(result.isCapped).toBe(expectedIsCapped === 'true');
          expect(result.vatAmount).toBe(parseInt(expectedVat, 10));
          expect(result.finalCost).toBe(parseInt(expectedFinalCost, 10));
        }
      }
    );
  });

  describe('Comprehensive Branch & Boundary Coverage (100% Coverage Target)', () => {
    test('should throw error when baseCost is missing or undefined', () => {
      expect(() => calculateServiceInvoice({})).toThrow('Chi phí dịch vụ không hợp lệ');
      expect(() => calculateServiceInvoice()).toThrow('Chi phí dịch vụ không hợp lệ');
    });

    test('should throw error when baseCost is null', () => {
      expect(() => calculateServiceInvoice({ customerTier: 'VIP', baseCost: null }))
        .toThrow('Chi phí dịch vụ không hợp lệ');
    });

    test('should throw error when baseCost is not a number or NaN', () => {
      expect(() => calculateServiceInvoice({ customerTier: 'VIP', baseCost: 'one million' }))
        .toThrow('Chi phí dịch vụ không hợp lệ');
      expect(() => calculateServiceInvoice({ customerTier: 'VIP', baseCost: NaN }))
        .toThrow('Chi phí dịch vụ không hợp lệ');
    });

    test('should throw error when baseCost is zero or negative', () => {
      expect(() => calculateServiceInvoice({ customerTier: 'VIP', baseCost: 0 }))
        .toThrow('Chi phí dịch vụ không hợp lệ');
      expect(() => calculateServiceInvoice({ customerTier: 'VIP', baseCost: -100 }))
        .toThrow('Chi phí dịch vụ không hợp lệ');
    });

    test('should throw error when customerTier is missing, empty, or not a string', () => {
      expect(() => calculateServiceInvoice({ baseCost: 500000 }))
        .toThrow('Hạng thành viên không hợp lệ');
      expect(() => calculateServiceInvoice({ baseCost: 500000, customerTier: '' }))
        .toThrow('Hạng thành viên không hợp lệ');
      expect(() => calculateServiceInvoice({ baseCost: 500000, customerTier: 123 }))
        .toThrow('Hạng thành viên không hợp lệ');
    });

    test('should throw error for unsupported customerTier name', () => {
      expect(() => calculateServiceInvoice({ baseCost: 500000, customerTier: 'DIAMOND' }))
        .toThrow('Hạng thành viên không được hỗ trợ: DIAMOND');
    });

    test('should handle voucherCode when undefined or non-string gracefully', () => {
      const result = calculateServiceInvoice({
        baseCost: 500000,
        customerTier: 'GOLD',
        voucherCode: undefined
      });
      expect(result.voucherDiscount).toBe(0);
      expect(result.customerTier).toBe('GOLD');
      expect(result.discountRate).toBe(0.15);
    });

    test('should handle default parameters (isFirstTime=false, hasVatInvoice=false)', () => {
      const result = calculateServiceInvoice({
        baseCost: 1000000,
        customerTier: 'SILVER'
      });
      expect(result.firstTimeDiscount).toBe(0);
      expect(result.vatAmount).toBe(0);
      expect(result.subTotal).toBe(900000);
      expect(result.finalCost).toBe(900000);
    });
  });
});
