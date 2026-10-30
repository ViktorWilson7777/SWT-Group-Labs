import React from 'react';
import { render } from '@testing-library/react';
import AutoServiceCard from '../src/components/AutoServiceCard';

describe('Lab 2 - Requirement (iii): Test Snapshot (AutoServiceCard)', () => {
  test('matches snapshot with default service prop', () => {
    const { container } = render(<AutoServiceCard />);
    expect(container).toMatchSnapshot();
  });

  test('matches snapshot with custom promotional discount service', () => {
    const customService = {
      id: 'EGA-SRV-CERAMIC',
      name: 'Gói Phủ Ceramic 9H Bảo Vệ Sơn Cao Cấp',
      category: 'Làm đẹp & Chăm sóc xe',
      originalPrice: 8000000,
      promotionalPrice: 6500000,
      warrantyMonths: 24,
      warrantyKm: 30000,
      rating: 5.0,
      isHot: true,
      features: [
        'Phủ 3 lớp Ceramic 9H độ bóng sâu',
        'Kháng tia UV, chống ố nước mưa',
        'Bảo dưỡng định kỳ 6 tháng/lần miễn phí'
      ]
    };

    const { container } = render(<AutoServiceCard service={customService} />);
    expect(container).toMatchSnapshot();
  });

  test('matches snapshot with non-discounted service (isHot is false and no promotional discount)', () => {
    const nonDiscountService = {
      id: 'EGA-SRV-TIRE-CHECK',
      name: 'Kiểm Tra Áp Suất & Cân Mâm Bấm Chì',
      category: 'Dịch vụ lốp',
      originalPrice: 200000,
      promotionalPrice: 200000,
      warrantyMonths: 1,
      warrantyKm: 1000,
      rating: 4.8,
      isHot: false,
      features: ['Cân mâm điện tử chính xác', 'Bơm khí Nitơ tinh khiết']
    };

    const { container } = render(<AutoServiceCard service={nonDiscountService} />);
    expect(container).toMatchSnapshot();
  });
});
