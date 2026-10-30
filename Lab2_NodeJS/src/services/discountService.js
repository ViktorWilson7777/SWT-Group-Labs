/**
 * Service calculating invoice, tiered discounts, vouchers, and taxes
 * for automotive care services at EGA Autocare.
 * Contains clear and verifiable if-else logic for branch coverage.
 */

/**
 * @typedef {Object} InvoiceCalculationParams
 * @property {string} customerTier - Membership tier: VIP, GOLD, SILVER, STANDARD
 * @property {number} baseCost - Base service cost in VND (must be > 0)
 * @property {string} [voucherCode] - Optional voucher code: EGAMEGA, EGASUPER, EGAFREE
 * @property {boolean} [isFirstTime=false] - Whether this is the customer's first visit
 * @property {boolean} [hasVatInvoice=false] - Whether a 10% VAT invoice is requested
 */

function calculateServiceInvoice(params = {}) {
  const {
    customerTier,
    baseCost,
    voucherCode,
    isFirstTime = false,
    hasVatInvoice = false
  } = params;

  // 1. Validation for base service cost
  if (
    baseCost === undefined ||
    baseCost === null ||
    typeof baseCost !== 'number' ||
    Number.isNaN(baseCost) ||
    baseCost <= 0
  ) {
    throw new Error('Chi phí dịch vụ không hợp lệ: phải là số lớn hơn 0');
  }

  // 2. Validation for customer tier
  if (!customerTier || typeof customerTier !== 'string') {
    throw new Error('Hạng thành viên không hợp lệ: phải là chuỗi ký tự');
  }

  const tier = customerTier.trim().toUpperCase();
  let discountRate = 0;

  // 3. Tier discount rate branches
  if (tier === 'VIP') {
    discountRate = 0.20;
  } else if (tier === 'GOLD') {
    discountRate = 0.15;
  } else if (tier === 'SILVER') {
    discountRate = 0.10;
  } else if (tier === 'STANDARD') {
    discountRate = 0.05;
  } else {
    throw new Error(`Hạng thành viên không được hỗ trợ: ${customerTier}`);
  }

  const tierDiscountAmount = Math.round(baseCost * discountRate);

  // 4. Voucher discount branches
  let voucherDiscount = 0;
  const cleanVoucher = typeof voucherCode === 'string' ? voucherCode.trim().toUpperCase() : '';

  if (cleanVoucher === 'EGAMEGA') {
    if (baseCost >= 500000) {
      voucherDiscount = 300000;
    } else {
      voucherDiscount = 0;
    }
  } else if (cleanVoucher === 'EGASUPER') {
    if (baseCost >= 1000000) {
      voucherDiscount = 100000;
    } else {
      voucherDiscount = 0;
    }
  } else if (cleanVoucher === 'EGAFREE') {
    if (baseCost >= 300000) {
      voucherDiscount = 50000;
    } else {
      voucherDiscount = 0;
    }
  } else if (cleanVoucher.length > 0) {
    voucherDiscount = 0;
  } else {
    voucherDiscount = 0;
  }

  // 5. First-time customer bonus branch
  let firstTimeDiscount = 0;
  if (isFirstTime === true) {
    firstTimeDiscount = Math.round(baseCost * 0.05);
  } else {
    firstTimeDiscount = 0;
  }

  // 6. Cap check branch: total discount cannot exceed 50% of base cost
  const rawTotalDiscount = tierDiscountAmount + voucherDiscount + firstTimeDiscount;
  const maxAllowedDiscount = Math.round(baseCost * 0.50);

  let totalDiscount = 0;
  let isCapped = false;

  if (rawTotalDiscount > maxAllowedDiscount) {
    totalDiscount = maxAllowedDiscount;
    isCapped = true;
  } else {
    totalDiscount = rawTotalDiscount;
    isCapped = false;
  }

  const subTotal = baseCost - totalDiscount;

  // 7. VAT invoice calculation branch
  let vatAmount = 0;
  if (hasVatInvoice === true) {
    vatAmount = Math.round(subTotal * 0.10);
  } else {
    vatAmount = 0;
  }

  const finalCost = subTotal + vatAmount;

  return {
    baseCost,
    customerTier: tier,
    discountRate,
    tierDiscountAmount,
    voucherDiscount,
    firstTimeDiscount,
    totalDiscount,
    isCapped,
    subTotal,
    vatAmount,
    finalCost
  };
}

module.exports = {
  calculateServiceInvoice
};
