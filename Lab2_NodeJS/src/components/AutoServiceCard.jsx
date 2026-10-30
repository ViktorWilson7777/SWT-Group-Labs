import React from 'react';

/**
 * Default fallback service item for EGA Autocare.
 */
export const DEFAULT_SERVICE = {
  id: 'EGA-SRV-01',
  name: 'Gói Bảo Dưỡng Cấp Cao & Thay Dầu Castrol',
  category: 'Chăm sóc xe định kỳ',
  originalPrice: 1500000,
  promotionalPrice: 1200000,
  warrantyMonths: 6,
  warrantyKm: 10000,
  rating: 4.9,
  isHot: true,
  features: [
    'Kiểm tra toàn diện 32 hạng mục an toàn',
    'Thay dầu nhớt Castrol Magnatec chính hãng',
    'Vệ sinh lọc gió động cơ và điều hòa',
    'Cân chỉnh áp suất lốp 4 bánh'
  ]
};

/**
 * AutoServiceCard component for EGA Autocare catalog.
 * Showcases service details, pricing with discounts, warranty, and badges.
 * Used for Jest Snapshot testing.
 */
export default function AutoServiceCard({ service = DEFAULT_SERVICE }) {
  const {
    id,
    name,
    category,
    originalPrice,
    promotionalPrice,
    warrantyMonths,
    warrantyKm,
    rating,
    isHot,
    features
  } = service;

  const hasDiscount = originalPrice > promotionalPrice && promotionalPrice > 0;
  const discountPercent = hasDiscount
    ? Math.round(((originalPrice - promotionalPrice) / originalPrice) * 100)
    : 0;

  return (
    <div className="ega-service-card" data-testid={`service-card-${id}`}>
      <div className="service-card-header">
        <span className="service-category">{category}</span>
        {isHot && <span className="hot-badge" data-testid="hot-badge">NỔI BẬT</span>}
      </div>

      <h3 className="service-name" data-testid="service-name">{name}</h3>

      <div className="service-rating" data-testid="service-rating">
        <span className="star-icon">★</span>
        <span className="rating-value">{rating.toFixed(1)} / 5.0</span>
      </div>

      <div className="service-pricing" data-testid="service-pricing">
        {hasDiscount ? (
          <>
            <span className="promo-price" data-testid="promo-price">
              {promotionalPrice.toLocaleString('vi-VN')} đ
            </span>
            <span className="original-price" data-testid="original-price">
              {originalPrice.toLocaleString('vi-VN')} đ
            </span>
            <span className="discount-tag" data-testid="discount-tag">
              -{discountPercent}%
            </span>
          </>
        ) : (
          <span className="promo-price" data-testid="promo-price">
            {originalPrice.toLocaleString('vi-VN')} đ
          </span>
        )}
      </div>

      <div className="service-warranty" data-testid="service-warranty">
        Bảo hành: <strong>{warrantyMonths} tháng</strong> hoặc <strong>{warrantyKm.toLocaleString('vi-VN')} km</strong>
      </div>

      <ul className="service-features" data-testid="service-features">
        {features.map((feature, idx) => (
          <li key={`${id}-feature-${idx}`} className="feature-item">
            ✓ {feature}
          </li>
        ))}
      </ul>

      <button type="button" className="btn-detail" data-testid="btn-view-detail">
        Xem chi tiết dịch vụ
      </button>
    </div>
  );
}
