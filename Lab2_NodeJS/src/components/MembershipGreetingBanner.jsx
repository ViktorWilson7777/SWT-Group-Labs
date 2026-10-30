import React from 'react';

/**
 * MembershipGreetingBanner component for EGA Autocare customer portal.
 * Renders tailored greeting strings, tier status, points conversion,
 * maintenance reminder, and optional broadcast announcements.
 */
export default function MembershipGreetingBanner({
  customerName = null,
  tier = 'STANDARD',
  loyaltyPoints = 0,
  isWarrantyExpiring = false,
  customAnnouncement = null
}) {
  // 1. Render string for greeting
  let greetingText = '';
  if (customerName && customerName.trim().length > 0) {
    greetingText = `Xin chào, ${customerName.trim()}! Chào mừng bạn đến với EGA Autocare.`;
  } else {
    greetingText = 'Xin chào quý khách! Chào mừng bạn đến với EGA Autocare.';
  }

  // 2. Render string for tier description
  const normalizedTier = (tier || '').toUpperCase();
  let tierBadgeText = '';
  if (normalizedTier === 'VIP') {
    tierBadgeText = 'Hạng thành viên: VIP - Giảm 20% phụ tùng và miễn phí cứu hộ 24/7.';
  } else if (normalizedTier === 'GOLD') {
    tierBadgeText = 'Hạng thành viên: Vàng - Giảm 15% phụ tùng và rửa xe miễn phí.';
  } else if (normalizedTier === 'SILVER') {
    tierBadgeText = 'Hạng thành viên: Bạc - Giảm 10% chi phí dịch vụ bảo dưỡng.';
  } else {
    tierBadgeText = 'Hạng thành viên: Tiêu chuẩn - Tích điểm để nhận ưu đãi hấp dẫn.';
  }

  // 3. Render string for loyalty points
  let pointsText = '';
  if (typeof loyaltyPoints === 'number' && loyaltyPoints > 0) {
    const formattedVnd = (loyaltyPoints * 1000).toLocaleString('vi-VN');
    pointsText = `Điểm tích lũy: ${loyaltyPoints} điểm (quy đổi: ${formattedVnd} VNĐ).`;
  } else {
    pointsText = 'Điểm tích lũy: 0 điểm. Hãy sử dụng dịch vụ để nhận điểm thưởng.';
  }

  // 4. Render string for vehicle warranty/maintenance reminder
  let warrantyAlertText = '';
  if (isWarrantyExpiring === true) {
    warrantyAlertText = 'Cảnh báo: Xe của bạn sắp đến hạn bảo dưỡng định kỳ! Vui lòng đặt hẹn ngay.';
  } else {
    warrantyAlertText = 'Tình trạng xe: Lịch bảo dưỡng vẫn đang trong thời hạn an toàn.';
  }

  return (
    <div className="ega-membership-banner" data-testid="membership-banner">
      <h2 data-testid="greeting-header">{greetingText}</h2>
      <div className="tier-info" data-testid="tier-info">
        <span className="tier-badge">{tierBadgeText}</span>
      </div>
      <p className="points-summary" data-testid="points-summary">
        {pointsText}
      </p>
      <div
        className={`warranty-status ${isWarrantyExpiring ? 'warning' : 'normal'}`}
        data-testid="warranty-status"
      >
        {warrantyAlertText}
      </div>
      {customAnnouncement && (
        <div className="custom-announcement" data-testid="custom-announcement">
          {`Thông báo đặc biệt: ${customAnnouncement}`}
        </div>
      )}
    </div>
  );
}
