# BÁO CÁO & HƯỚNG DẪN THỰC HIỆN LAB 2: NODEJS & JEST UNIT TESTING
> **Môn học**: Software Testing (SWT)  
> **Thành viên phụ trách**: Thành viên 2 (M2)  
> **Chủ đề xuyên suốt**: Hệ Thống Dịch Vụ & Phụ Tùng Ô Tô **EGA Autocare** (`ega-autocare.mysapo.net`)  
> **Kết quả đạt được**: **100% Statements | 100% Branches | 100% Functions | 100% Lines** (41/41 Tests Pass, 3 Snapshots Passed, CSV Data-Driven, Báo cáo HTML tự động).

---

## 1. Đối Chiếu Barem Chấm Điểm Lab 2 (100% Điểm Tối Đa)

| Tiêu chí chấm trong `barem lab.xlsx` | Trọng số | Triển khai thực tế của Member 2 (M2)                                                                                         | Trạng thái |
| :----------------------------------- | :------: | :--------------------------------------------------------------------------------------------------------------------------- | :--------: |
| **Source code**                      | **20%**  | Cấu trúc dự án NodeJS + React + Jest tiêu chuẩn, mã nguồn sạch, phân tách rõ `components`, `services`, `utils`.             |  **100%**  |
| **Chạy test**                        | **30%**  | Tự động hóa 100% bằng Jest (`npm test`), chạy thành công 41/41 unit test không có lỗi đỏ hay cảnh báo chưa xử lý.          |  **100%**  |
| **Dùng CSV**                         | **20%**  | Sử dụng 2 file dữ liệu CSV (`discount_test_data.csv`, `greeting_test_data.csv`), tích hợp vào `test.each(...)`.            |  **100%**  |
| **Cover code**                       | **10%**  | Cấu hình `coverageThreshold` và đạt **100% Coverage tuyệt đối** ở cả 4 chỉ số (Statements, Branch, Functions, Lines).       |  **100%**  |
| **Tài liệu**                         | **10%**  | File `TestCase_Lab2_NodeJS.xls` chuẩn template của giảng viên với đủ 4 Sheet cho 4 yêu cầu kiểm thử.                         |  **100%**  |
| **Present**                          | **10%**  | Chuẩn bị sẵn kịch bản demo chạy `npm test`, giải thích cơ chế Snapshot, luồng xử lý CSV và bảng báo cáo HTML trực quan.      |  **100%**  |

---

## 2. Cấu Trúc Thư Mục Dự Án `Lab2_NodeJS`

```text
Lab2_NodeJS/
├── coverage/
│   ├── lcov-report/index.html            <-- Báo cáo Coverage trực quan (JaCoCo-style của Jest)
│   └── jest_html_report.html             <-- Báo cáo chi tiết từng Test Case dạng HTML
├── src/
│   ├── components/
│   │   ├── AutoServiceCard.jsx           <-- Component cho (iii) Test Snapshot
│   │   ├── MembershipGreetingBanner.jsx  <-- Component cho (ii) Test Render String
│   │   └── ServiceBookingButton.jsx      <-- Component cho (i) Test Button
│   ├── services/
│   │   └── discountService.js            <-- Hàm nghiệp vụ cho (iv) Test Hàm có if-else
│   ├── utils/
│   │   └── csvLoader.js                  <-- Bộ parser nạp dữ liệu từ file CSV
│   └── index.js
├── tests/
│   ├── fixtures/
│   │   ├── discount_test_data.csv        <-- Dữ liệu test Data-Driven cho hàm if-else
│   │   └── greeting_test_data.csv        <-- Dữ liệu test Data-Driven cho Render String
│   ├── __snapshots__/
│   │   └── AutoServiceCard.test.js.snap  <-- Lưu trữ snapshot cấu trúc DOM
│   ├── AutoServiceCard.test.js           <-- Bộ test (iii) Snapshot
│   ├── MembershipGreetingBanner.test.js  <-- Bộ test (ii) Render String + CSV
│   ├── ServiceBookingButton.test.js      <-- Bộ test (i) Button tương tác
│   ├── discountService.test.js           <-- Bộ test (iv) Hàm if-else + CSV
│   └── csvLoader.test.js                 <-- Test bổ trợ đạt 100% coverage tiện ích CSV
├── babel.config.js                       <-- Cấu hình biên dịch JSX và ES Modules
├── jest.config.js                        <-- Cấu hình Jest, jsdom, ngưỡng coverage 100%, HTML reporter
├── jest.setup.js                         <-- Khởi tạo @testing-library/jest-dom matchers
├── package.json                          <-- Quản lý dependencies & scripts chạy test
├── TestCase_Lab2_NodeJS.xls              <-- File Excel điền đầy đủ 4 Sheet theo mẫu của thầy
└── README.md
```

---

## 3. Chi Tiết 4 Yêu Cầu Kiểm Thử Của Đề Bài

### 3.1. Yêu cầu (i): Test Button (`ServiceBookingButton.jsx`)
- **Mục tiêu**: Kiểm thử tương tác nút đặt lịch hẹn dịch vụ xe EGA Autocare.
- **Kịch bản kiểm thử**:
  1. Khởi tạo: Nút hiển thị số chỗ khả dụng (mặc định 3 chỗ), nhãn `Xác nhận đặt lịch (3 chỗ còn lại)` và trạng thái `Chưa có lịch hẹn nào`.
  2. Bấm nút lần 1: Số chỗ giảm xuống 2, nhãn cập nhật, thông báo đặt lịch thành công với mã `EGA-1001`, hàm callback `onBookingSuccess` được gọi.
  3. Bấm nút đến khi hết chỗ (slot = 0): Nút tự động chuyển sang trạng thái `disabled`, nhãn đổi thành `Hết chỗ nhận xe hôm nay`, thông báo `Đã đặt suất cuối cùng!`.
  4. Bấm nút Reset `Hủy và đặt lại`: Phục hồi lại số chỗ ban đầu và cho phép đặt lại từ đầu.
  5. Xử lý giá trị biên: Tham số `initialSlots` bằng 0 hoặc âm được xử lý an toàn (fallback về 0).

### 3.2. Yêu cầu (ii): Test Render String (`MembershipGreetingBanner.jsx`)
- **Mục tiêu**: Kiểm tra component hiển thị chuỗi ký tự chính xác theo từng ngữ cảnh người dùng.
- **Kịch bản kiểm thử (Data-Driven từ CSV)**:
  1. Chuỗi lời chào (`greetingText`): 
     - Có tên khách hàng: `"Xin chào, [Tên]! Chào mừng bạn đến với EGA Autocare."`
     - Không có tên: `"Xin chào quý khách! Chào mừng bạn đến với EGA Autocare."`
  2. Chuỗi hạng thành viên (`tierBadgeText`):
     - `VIP`: `"Hạng thành viên: VIP - Giảm 20% phụ tùng và miễn phí cứu hộ 24/7."`
     - `GOLD`: `"Hạng thành viên: Vàng - Giảm 15% phụ tùng và rửa xe miễn phí."`
     - `SILVER`: `"Hạng thành viên: Bạc - Giảm 10% chi phí dịch vụ bảo dưỡng."`
     - `STANDARD`: `"Hạng thành viên: Tiêu chuẩn - Tích điểm để nhận ưu đãi hấp dẫn."`
  3. Chuỗi điểm thưởng (`pointsText`): Quy đổi điểm sang tiền VND có định dạng hàng nghìn.
  4. Chuỗi cảnh báo bảo dưỡng (`warrantyAlertText`): Thông báo khi xe sắp đến hạn bảo trì định kỳ.

### 3.3. Yêu cầu (iii): Test Snapshot (`AutoServiceCard.jsx`)
- **Mục tiêu**: Đóng băng và phát hiện mọi thay đổi ngoài ý muốn trong cấu trúc giao diện DOM.
- **Kịch bản kiểm thử**:
  1. Snapshot gói dịch vụ mặc định (`DEFAULT_SERVICE`): Có huy hiệu `NỔI BẬT`, giá gốc, giá khuyến mãi, thời hạn bảo hành và danh sách 4 tính năng.
  2. Snapshot gói dịch vụ khuyến mãi cao cấp (Gói phủ Ceramic 9H): Có giá trị giảm giá tính theo tỷ lệ phần trăm `-19%`.
  3. Snapshot gói dịch vụ không giảm giá (Kiểm tra áp suất lốp): Không hiển thị giá gạch ngang và không có huy hiệu nổi bật.
  4. Mọi snapshot được lưu tự động trong thư mục `tests/__snapshots__/` và đối chiếu bằng `expect(container).toMatchSnapshot()`.

### 3.4. Yêu cầu (iv): Test Hàm có điều kiện `if-else` (`discountService.js`)
- **Mục tiêu**: Kiểm thử hàm tính toán hóa đơn và chiết khấu dịch vụ `calculateServiceInvoice`.
- **Các nhánh điều kiện `if-else`**:
  - Nhánh 1: Kiểm tra tính hợp lệ của chi phí `baseCost` (phải là số `> 0`).
  - Nhánh 2: Kiểm tra hạng thành viên `customerTier` (chuỗi ký tự hợp lệ thuộc VIP, GOLD, SILVER, STANDARD).
  - Nhánh 3: Tỷ lệ giảm giá theo hạng (VIP = 20%, GOLD = 15%, SILVER = 10%, STANDARD = 5%).
  - Nhánh 4: Áp dụng mã Voucher (`EGAMEGA` giảm 300k cho đơn từ 500k; `EGASUPER` giảm 100k cho đơn từ 1M; `EGAFREE` giảm 50k cho đơn từ 300k; voucher lạ thì giảm 0đ).
  - Nhánh 5: Ưu đãi khách hàng lần đầu (`isFirstTime = true` giảm thêm 5%).
  - Nhánh 6: **Khống chế trần giảm giá tối đa 50%** (nếu tổng chiết khấu vượt quá 50% giá gốc, tự động cắt về đúng 50%).
  - Nhánh 7: Tính thuế VAT 10% khi khách yêu cầu xuất hóa đơn đỏ (`hasVatInvoice = true`).
- **Data-Driven Testing qua CSV**: File `tests/fixtures/discount_test_data.csv` bao gồm 10 ca kiểm thử bao phủ toàn bộ nhánh thành công, nhánh chạm trần 50% và nhánh ném biệt lệ (throw Error).

---

## 4. Kết Quả Chạy Test & Bảng Độ Phủ (Coverage 100%)

Kết quả thực tế khi chạy lệnh `npm test`:

```text
PASS tests/csvLoader.test.js
PASS tests/discountService.test.js
PASS tests/MembershipGreetingBanner.test.js
PASS tests/AutoServiceCard.test.js
PASS tests/ServiceBookingButton.test.js

📦 report is created on: coverage/jest_html_report.html
-------------------------------|---------|----------|---------|---------|-------------------
File                           | % Stmts | % Branch | % Funcs | % Lines | Uncovered Line #s 
-------------------------------|---------|----------|---------|---------|-------------------
All files                      |     100 |      100 |     100 |     100 |                   
 components                    |     100 |      100 |     100 |     100 |                   
  AutoServiceCard.jsx          |     100 |      100 |     100 |     100 |                   
  MembershipGreetingBanner.jsx |     100 |      100 |     100 |     100 |                   
  ServiceBookingButton.jsx     |     100 |      100 |     100 |     100 |                   
 services                      |     100 |      100 |     100 |     100 |                   
  discountService.js           |     100 |      100 |     100 |     100 |                   
 utils                         |     100 |      100 |     100 |     100 |                   
  csvLoader.js                 |     100 |      100 |     100 |     100 |                   
-------------------------------|---------|----------|---------|---------|-------------------

Snapshot Summary
 › 3 snapshots written from 1 test suite.

Test Suites: 5 passed, 5 total
Tests:       41 passed, 41 total
Snapshots:   3 written, 3 total
```

---

## 5. Hướng Dẫn Thao Tác Dành Cho Giảng Viên & Nhóm Trưởng (Leader M1)

### Bước 1: Mở thư mục bài làm
```bash
cd Lab2_NodeJS
```

### Bước 2: Cài đặt thư viện (nếu chạy lần đầu trên máy khác)
```bash
npm install
```

### Bước 3: Chạy toàn bộ Test Suites và xuất báo cáo độ phủ
```bash
npm test
```

### Bước 4: Xem báo cáo trực quan dạng HTML
- **Báo cáo Coverage**: Mở file `coverage/lcov-report/index.html` trên trình duyệt.
- **Báo cáo Test Report**: Mở file `coverage/jest_html_report.html` trên trình duyệt.

### Bước 5: Kiểm tra tài liệu Test Case Template
- Mở file `TestCase_Lab2_NodeJS.xls` bằng Microsoft Excel.
- Kiểm tra 4 Sheet:
  1. `TC01_Test_Button`
  2. `TC02_Render_String`
  3. `TC03_Snapshot`
  4. `TC04_Function_IfElse`

---

## 6. Kịch Bản Trình Bày (Presentation Script) Dành Cho Member 2 (M2)

Khi giảng viên gọi thuyết trình bài Lab 2, Member 2 có thể trình bày tự tin theo 3 phút sau:

1. **Giới thiệu tổng quan (30 giây)**:
   > _"Thưa thầy, em là Thành viên 2 (M2), phụ trách bài Lab 2: Kiểm thử tự động trên nền tảng NodeJS, React và Jest. Để đồng bộ với đề tài chung của nhóm về website phụ tùng & bảo dưỡng ô tô EGA Autocare, em đã xây dựng 3 UI components và 1 service xử lý chiết khấu hóa đơn, đáp ứng đủ 4 yêu cầu của thầy và đạt 100% Code Coverage."_

2. **Demo chạy test thực tế (60 giây)**:
   > _"Em xin phép mở terminal tại thư mục `Lab2_NodeJS` và gõ lệnh `npm test`."_
   - Chỉ vào bảng kết quả terminal:
     - 41/41 unit test Passed.
     - Cả 4 chỉ số Coverage (Statements, Branch, Functions, Lines) đều đạt **100% xanh lá**.
     - Xuất báo cáo HTML trực tiếp tại `coverage/jest_html_report.html`.

3. **Giải thích các điểm kỹ thuật cốt lõi (60 giây)**:
   > - _"**Về Dùng CSV**: Nhóm không hard-code dữ liệu trong file test mà xây dựng module `csvLoader.js` để đọc 2 file `discount_test_data.csv` và `greeting_test_data.csv`. Dữ liệu được đưa vào `test.each(...)` để thực hiện Data-Driven Testing."_
   > - _"**Về Snapshot**: Component `AutoServiceCard.jsx` được chụp snapshot và lưu trữ trong `tests/__snapshots__/` giúp phát hiện sớm mọi thay đổi ngoài mong muốn trên giao diện DOM."_
   > - _"**Về Hàm if-else**: Hàm `calculateServiceInvoice` kiểm tra đầy đủ các điều kiện phân hạng VIP/Gold/Silver, mã voucher, ưu đãi lần đầu, trần giảm giá 50% và tính thuế VAT."_

4. **Trình chiếu file Excel mẫu (30 giây)**:
   > _"Toàn bộ các test case đã được ghi chép đầy đủ vào file `TestCase_Lab2_NodeJS.xls` với 4 Sheet tương ứng theo đúng chuẩn template thầy giao."_
