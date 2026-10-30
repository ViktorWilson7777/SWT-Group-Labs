import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import ServiceBookingButton from '../src/components/ServiceBookingButton';

describe('Lab 2 - Requirement (i): Test Button (ServiceBookingButton)', () => {
  test('renders initial button state with 3 slots and default labels', () => {
    render(<ServiceBookingButton />);

    const bookingBtn = screen.getByTestId('booking-btn');
    const slotCount = screen.getByTestId('slot-count');
    const status = screen.getByTestId('booking-status');

    expect(bookingBtn).toBeInTheDocument();
    expect(bookingBtn).toBeEnabled();
    expect(bookingBtn).toHaveTextContent('Xác nhận đặt lịch (3 chỗ còn lại)');
    expect(slotCount).toHaveTextContent('3');
    expect(status).toHaveTextContent('Chưa có lịch hẹn nào');
  });

  test('clicking button decrements slots and invokes callback', () => {
    const handleSuccess = jest.fn();
    render(
      <ServiceBookingButton
        initialSlots={2}
        serviceName="Thay nhớt Castrol"
        onBookingSuccess={handleSuccess}
      />
    );

    const bookingBtn = screen.getByTestId('booking-btn');
    const slotCount = screen.getByTestId('slot-count');
    const status = screen.getByTestId('booking-status');

    // First click: 2 -> 1
    fireEvent.click(bookingBtn);
    expect(slotCount).toHaveTextContent('1');
    expect(bookingBtn).toHaveTextContent('Xác nhận đặt lịch (1 chỗ còn lại)');
    expect(status).toHaveTextContent('Đặt lịch thành công cho Thay nhớt Castrol! Mã đặt: EGA-1001');
    expect(handleSuccess).toHaveBeenCalledTimes(1);
    expect(handleSuccess).toHaveBeenCalledWith({
      serviceName: 'Thay nhớt Castrol',
      bookingCode: 'EGA-1001',
      remainingSlots: 1
    });

    // Second click: 1 -> 0 (final slot)
    fireEvent.click(bookingBtn);
    expect(slotCount).toHaveTextContent('0');
    expect(bookingBtn).toBeDisabled();
    expect(bookingBtn).toHaveTextContent('Hết chỗ nhận xe hôm nay');
    expect(status).toHaveTextContent('Đã đặt suất cuối cùng! Hết chỗ nhận xe hôm nay. Mã đặt: EGA-1002');
    expect(handleSuccess).toHaveBeenCalledTimes(2);
  });

  test('cannot click booking button when disabled or when slots are 0', () => {
    const handleSuccess = jest.fn();
    render(<ServiceBookingButton initialSlots={0} onBookingSuccess={handleSuccess} />);

    const bookingBtn = screen.getByTestId('booking-btn');
    expect(bookingBtn).toBeDisabled();
    expect(bookingBtn).toHaveTextContent('Hết chỗ nhận xe hôm nay');

    fireEvent.click(bookingBtn);
    expect(handleSuccess).not.toHaveBeenCalled();
    expect(screen.getByTestId('slot-count')).toHaveTextContent('0');
  });

  test('reset button restores slots and initial booking state', () => {
    render(<ServiceBookingButton initialSlots={2} />);

    const bookingBtn = screen.getByTestId('booking-btn');
    const resetBtn = screen.getByTestId('reset-btn');

    // Book 2 times to reach 0
    fireEvent.click(bookingBtn);
    fireEvent.click(bookingBtn);
    expect(bookingBtn).toBeDisabled();

    // Click reset
    fireEvent.click(resetBtn);
    expect(bookingBtn).toBeEnabled();
    expect(bookingBtn).toHaveTextContent('Xác nhận đặt lịch (2 chỗ còn lại)');
    expect(screen.getByTestId('slot-count')).toHaveTextContent('2');
    expect(screen.getByTestId('booking-status')).toHaveTextContent('Đã đặt lại trạng thái ban đầu.');
  });

  test('handles invalid initialSlots gracefully by defaulting to 0', () => {
    render(<ServiceBookingButton initialSlots={-5} />);
    expect(screen.getByTestId('slot-count')).toHaveTextContent('0');
    expect(screen.getByTestId('booking-btn')).toBeDisabled();

    render(<ServiceBookingButton initialSlots="invalid_slot" />);
    expect(screen.getAllByTestId('slot-count')[1]).toHaveTextContent('0');
  });

  test('functions normally without passing onBookingSuccess callback', () => {
    render(<ServiceBookingButton initialSlots={1} onBookingSuccess={null} />);
    const bookingBtn = screen.getByTestId('booking-btn');
    expect(() => fireEvent.click(bookingBtn)).not.toThrow();
  });
});
