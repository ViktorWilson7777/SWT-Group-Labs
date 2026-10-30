import React from 'react';
import { render, screen } from '@testing-library/react';
import path from 'path';
import MembershipGreetingBanner from '../src/components/MembershipGreetingBanner';
import { loadCsvData } from '../src/utils/csvLoader';

describe('Lab 2 - Requirement (ii): Test Render String (MembershipGreetingBanner)', () => {
  const csvPath = path.resolve(__dirname, 'fixtures/greeting_test_data.csv');
  const csvCases = loadCsvData(csvPath);

  describe('Data-Driven Testing from CSV for Render Strings', () => {
    test.each(csvCases)(
      'Data-Driven Case $testId: Name="$customerName", Tier=$tier, Points=$loyaltyPoints, Expiring=$isWarrantyExpiring',
      ({
        customerName,
        tier,
        loyaltyPoints,
        isWarrantyExpiring,
        customAnnouncement,
        expectedGreetingSnippet,
        expectedTierSnippet,
        expectedPointsSnippet,
        expectedWarrantySnippet,
        expectedAnnouncementSnippet
      }) => {
        render(
          <MembershipGreetingBanner
            customerName={customerName || undefined}
            tier={tier}
            loyaltyPoints={parseInt(loyaltyPoints, 10)}
            isWarrantyExpiring={isWarrantyExpiring === 'true'}
            customAnnouncement={customAnnouncement || undefined}
          />
        );

        // Verify exact or snippet string rendering
        expect(screen.getByTestId('greeting-header')).toHaveTextContent(expectedGreetingSnippet);
        expect(screen.getByTestId('tier-info')).toHaveTextContent(expectedTierSnippet);
        expect(screen.getByTestId('points-summary')).toHaveTextContent(expectedPointsSnippet);
        expect(screen.getByTestId('warranty-status')).toHaveTextContent(expectedWarrantySnippet);

        if (expectedAnnouncementSnippet) {
          expect(screen.getByTestId('custom-announcement')).toHaveTextContent(expectedAnnouncementSnippet);
        } else {
          expect(screen.queryByTestId('custom-announcement')).toBeNull();
        }
      }
    );
  });

  describe('Granular String Rendering & Branch Verification (100% Coverage)', () => {
    test('renders default fallback strings when no props are provided', () => {
      render(<MembershipGreetingBanner />);

      expect(screen.getByTestId('greeting-header')).toHaveTextContent(
        'Xin chào quý khách! Chào mừng bạn đến với EGA Autocare.'
      );
      expect(screen.getByTestId('tier-info')).toHaveTextContent(
        'Hạng thành viên: Tiêu chuẩn - Tích điểm để nhận ưu đãi hấp dẫn.'
      );
      expect(screen.getByTestId('points-summary')).toHaveTextContent(
        'Điểm tích lũy: 0 điểm. Hãy sử dụng dịch vụ để nhận điểm thưởng.'
      );
      expect(screen.getByTestId('warranty-status')).toHaveTextContent(
        'Tình trạng xe: Lịch bảo dưỡng vẫn đang trong thời hạn an toàn.'
      );
      expect(screen.queryByTestId('custom-announcement')).toBeNull();
    });

    test('renders generic greeting when customerName contains only whitespaces', () => {
      render(<MembershipGreetingBanner customerName="    " />);
      expect(screen.getByTestId('greeting-header')).toHaveTextContent(
        'Xin chào quý khách! Chào mừng bạn đến với EGA Autocare.'
      );
    });

    test('renders zero-points string when loyaltyPoints is not a number or negative', () => {
      render(<MembershipGreetingBanner loyaltyPoints={-10} />);
      expect(screen.getByTestId('points-summary')).toHaveTextContent(
        'Điểm tích lũy: 0 điểm. Hãy sử dụng dịch vụ để nhận điểm thưởng.'
      );

      render(<MembershipGreetingBanner loyaltyPoints="invalid" />);
      expect(screen.getAllByTestId('points-summary')[1]).toHaveTextContent(
        'Điểm tích lũy: 0 điểm. Hãy sử dụng dịch vụ để nhận điểm thưởng.'
      );
    });

    test('renders null tier gracefully as standard tier badge', () => {
      render(<MembershipGreetingBanner tier={null} />);
      expect(screen.getByTestId('tier-info')).toHaveTextContent(
        'Hạng thành viên: Tiêu chuẩn - Tích điểm để nhận ưu đãi hấp dẫn.'
      );
    });
  });
});
