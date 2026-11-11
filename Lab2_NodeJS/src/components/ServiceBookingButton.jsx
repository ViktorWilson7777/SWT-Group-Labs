import React, { useState, useRef } from 'react';

/**
 * ServiceBookingButton component for EGA Autocare.
 * Allows customers to interactively book car maintenance slots,
 * tracking slot capacity, disabling the button when full, and resetting state.
 */
export default function ServiceBookingButton({
  initialSlots = 3,
  serviceName = 'Bảo dưỡng xe định kỳ',
  onBookingSuccess = null
}) {
  const safeInitialSlots =
    typeof initialSlots === 'number' && Number.isFinite(initialSlots) && initialSlots >= 0
      ? Math.floor(initialSlots)
      : 0;
  const [slots, setSlots] = useState(safeInitialSlots);
  const bookingCountRef = useRef(0);
  const [statusMessage, setStatusMessage] = useState('Chưa có lịch hẹn nào');

  const handleBook = () => {
    const nextSlots = Math.max(0, slots - 1);
    bookingCountRef.current += 1;
    const bookingCode = `EGA-${1000 + bookingCountRef.current}`;

    setSlots(nextSlots);

    let message = '';
    if (nextSlots === 0) {
      message = `Đã đặt suất cuối cùng! Hết chỗ nhận xe hôm nay. Mã đặt: ${bookingCode}`;
    } else {
      message = `Đặt lịch thành công cho ${serviceName}! Mã đặt: ${bookingCode}`;
    }
    setStatusMessage(message);

    if (typeof onBookingSuccess === 'function') {
      onBookingSuccess({
        bookingId: bookingCode,
        bookingCode,
        serviceName,
        remainingSlots: nextSlots
      });
    }
  };

  const handleReset = () => {
    setSlots(safeInitialSlots);
    setStatusMessage('Đã đặt lại trạng thái ban đầu.');
  };

  const isFull = slots <= 0;

  return (
    <div className="service-booking-container" data-testid="service-booking-card">
      <h3 data-testid="service-title">{serviceName}</h3>
      <p className="slots-info" data-testid="slots-info">
        Số chỗ khả dụng: <span data-testid="slot-count">{slots}</span>
      </p>

      <div className="button-group">
        <button
          type="button"
          data-testid="booking-btn"
          disabled={isFull}
          onClick={handleBook}
          className={`btn-booking ${isFull ? 'disabled' : 'active'}`}
        >
          {isFull ? 'Hết chỗ nhận xe hôm nay' : `Xác nhận đặt lịch (${slots} chỗ còn lại)`}
        </button>

        <button
          type="button"
          data-testid="reset-btn"
          onClick={handleReset}
          className="btn-reset"
        >
          Hủy và đặt lại
        </button>
      </div>

      <div
        className={`status-notification ${isFull ? 'status-full' : 'status-ok'}`}
        data-testid="booking-status"
      >
        {statusMessage}
      </div>
    </div>
  );
}
