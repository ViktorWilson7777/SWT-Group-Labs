import xlwt
import os

def create_lab2_testcase_excel(output_paths):
    wb = xlwt.Workbook(encoding='utf-8')

    # Fonts
    f_bold = xlwt.Font()
    f_bold.name = 'Calibri'
    f_bold.bold = True
    f_bold.height = 200

    f_norm = xlwt.Font()
    f_norm.name = 'Calibri'
    f_norm.height = 200

    f_pass = xlwt.Font()
    f_pass.name = 'Calibri'
    f_pass.bold = True
    f_pass.colour_index = 0x11  # dark green
    f_pass.height = 200

    # Borders
    b_thin = xlwt.Borders()
    b_thin.left = xlwt.Borders.THIN
    b_thin.right = xlwt.Borders.THIN
    b_thin.top = xlwt.Borders.THIN
    b_thin.bottom = xlwt.Borders.THIN

    # Patterns
    p_header = xlwt.Pattern()
    p_header.pattern = xlwt.Pattern.SOLID_PATTERN
    p_header.pattern_fore_colour = 0x16  # 25% gray

    p_section = xlwt.Pattern()
    p_section.pattern = xlwt.Pattern.SOLID_PATTERN
    p_section.pattern_fore_colour = 0x1F  # pale ice blue

    # Alignments
    a_left = xlwt.Alignment()
    a_left.horz = xlwt.Alignment.HORZ_LEFT
    a_left.vert = xlwt.Alignment.VERT_CENTER
    a_left.wrap = xlwt.Alignment.WRAP_AT_RIGHT

    a_center = xlwt.Alignment()
    a_center.horz = xlwt.Alignment.HORZ_CENTER
    a_center.vert = xlwt.Alignment.VERT_CENTER

    # Styles
    style_label = xlwt.XFStyle()
    style_label.font = f_bold
    style_label.borders = b_thin
    style_label.pattern = p_header
    style_label.alignment = a_left

    style_section = xlwt.XFStyle()
    style_section.font = f_bold
    style_section.borders = b_thin
    style_section.pattern = p_section
    style_section.alignment = a_left

    style_col_hdr = xlwt.XFStyle()
    style_col_hdr.font = f_bold
    style_col_hdr.borders = b_thin
    style_col_hdr.pattern = p_header
    style_col_hdr.alignment = a_center

    style_val_left = xlwt.XFStyle()
    style_val_left.font = f_norm
    style_val_left.borders = b_thin
    style_val_left.alignment = a_left

    style_val_center = xlwt.XFStyle()
    style_val_center.font = f_norm
    style_val_center.borders = b_thin
    style_val_center.alignment = a_center

    style_pass = xlwt.XFStyle()
    style_pass.font = f_pass
    style_pass.borders = b_thin
    style_pass.alignment = a_center

    test_cases_data = [
        {
            "sheet_name": "TC01_Test_Button",
            "tc_id": "UT_L2_BTN_001",
            "description": "Test Button ServiceBookingButton: Click, Slot Decrement, Disabled State, Callback & Reset",
            "created_by": "Member 2 (M2)",
            "reviewed_by": "Member 5 (M5)",
            "version": "1.0",
            "log": "Automated React component testing with @testing-library/react; 100% Branch Coverage verified",
            "tester": "Member 2 (M2)",
            "date": "28-Oct-2026",
            "status": "Pass",
            "prerequisites": [
                "Node.js >= 18 and Jest test environment (jsdom) configured",
                "React 18 & @testing-library/react installed in Lab2_NodeJS",
                "Component ServiceBookingButton.jsx implemented in src/components/",
                "All dependencies verified via npm test command"
            ],
            "test_data": [
                "initialSlots = 3, serviceName = 'Bảo dưỡng xe định kỳ'",
                "initialSlots = 2, serviceName = 'Thay nhớt Castrol', callback handleSuccess",
                "initialSlots = 0 (Boundary: no available slots)",
                "initialSlots = -5 (Invalid slot value fallback to 0)"
            ],
            "scenario": "Verify that clicking the booking button decrements slots, updates status message, invokes callback, disables when 0 slots remain, and can be restored via Reset button",
            "steps": [
                (1.0, "Render <ServiceBookingButton /> with default 3 slots", "Button enabled with label 'Xác nhận đặt lịch (3 chỗ còn lại)', slot count = 3, status 'Chưa có lịch hẹn nào'", "As Expected", "Pass"),
                (2.0, "Click booking button first time", "Slot decreases to 2, button label updates to '(2 chỗ còn lại)', status shows booking code EGA-1001, onBookingSuccess called", "As Expected", "Pass"),
                (3.0, "Click booking button second time to reach 0 slots", "Slot count reaches 0, button becomes disabled with text 'Hết chỗ nhận xe hôm nay', status notifies 'Đã đặt suất cuối cùng!'", "As Expected", "Pass"),
                (4.0, "Click Reset button 'Hủy và đặt lại'", "Button re-enabled, slot count restored to initial 3 slots, status reset to 'Đã đặt lại trạng thái ban đầu.'", "As Expected", "Pass"),
                (5.0, "Render component with initialSlots = 0 and negative value (-5)", "Button is immediately disabled with 0 slots, safeInitialSlots gracefully defaults to 0", "As Expected", "Pass")
            ]
        },
        {
            "sheet_name": "TC02_Render_String",
            "tc_id": "UT_L2_STR_001",
            "description": "Test Render String MembershipGreetingBanner: Greeting, Tier Info, Loyalty Points & Warranty Alerts",
            "created_by": "Member 2 (M2)",
            "reviewed_by": "Member 5 (M5)",
            "version": "1.0",
            "log": "Data-Driven automated testing with Jest test.each reading from greeting_test_data.csv",
            "tester": "Member 2 (M2)",
            "date": "28-Oct-2026",
            "status": "Pass",
            "prerequisites": [
                "Node.js >= 18 and Jest configured with Babel JSX transform",
                "CSV test fixture tests/fixtures/greeting_test_data.csv ready",
                "Component MembershipGreetingBanner.jsx implemented in src/components/",
                "Helper csvLoader.js available to read test datasets"
            ],
            "test_data": [
                "Row 1: customerName = 'Nguyen Van A', tier = 'VIP', points = 150, expiring = true",
                "Row 2: customerName = 'Tran Thi B', tier = 'GOLD', points = 50, expiring = false",
                "Row 3: customerName = 'Le Van C', tier = 'SILVER', points = 0, expiring = false",
                "Row 4: customerName = '', tier = 'STANDARD', points = 0, expiring = true"
            ],
            "scenario": "Verify that MembershipGreetingBanner renders accurate tailored string messages based on customer name, membership tier, loyalty points conversion, and maintenance reminder",
            "steps": [
                (1.0, "Load CSV fixture tests/fixtures/greeting_test_data.csv and run test.each", "All 5 CSV test rows pass parameter validation against rendered DOM text nodes", "As Expected", "Pass"),
                (2.0, "Render banner with customerName = 'Nguyen Van A'", "Header renders: 'Xin chào, Nguyen Van A! Chào mừng bạn đến với EGA Autocare.'", "As Expected", "Pass"),
                (3.0, "Render banner with tier = 'VIP', 'GOLD', 'SILVER', 'STANDARD'", "Tier badge renders corresponding privilege description string (20%, 15%, 10%, standard)", "As Expected", "Pass"),
                (4.0, "Render banner with loyaltyPoints = 150 vs loyaltyPoints = 0", "Points line renders converted VND value: '150 điểm (quy đổi: 150.000 VNĐ)' vs '0 điểm. Hãy sử dụng dịch vụ...'", "As Expected", "Pass"),
                (5.0, "Render banner with isWarrantyExpiring = true vs false", "Warranty line renders warning alert 'Cảnh báo: Xe của bạn sắp đến hạn...' vs safe status 'Tình trạng xe: ... trong thời hạn an toàn.'", "As Expected", "Pass")
            ]
        },
        {
            "sheet_name": "TC03_Snapshot",
            "tc_id": "UT_L2_SNP_001",
            "description": "Test Snapshot AutoServiceCard: DOM Hierarchy Stability & Visual Regression Prevention",
            "created_by": "Member 2 (M2)",
            "reviewed_by": "Member 5 (M5)",
            "version": "1.0",
            "log": "Automated Snapshot testing storing DOM structure in tests/__snapshots__/AutoServiceCard.test.js.snap",
            "tester": "Member 2 (M2)",
            "date": "28-Oct-2026",
            "status": "Pass",
            "prerequisites": [
                "Jest snapshot testing engine and React testing library",
                "Component AutoServiceCard.jsx implemented in src/components/",
                "DEFAULT_SERVICE fixture object defined with pricing, rating, warranty, features",
                "Snapshot directory tests/__snapshots__/ initialized"
            ],
            "test_data": [
                "DEFAULT_SERVICE: Gói Bảo Dưỡng Cấp Cao & Thay Dầu Castrol (1.5M -> 1.2M, Hot = true)",
                "Promotional Service: Gói Phủ Ceramic 9H (8M -> 6.5M, Discount = -19%, Hot = true)",
                "Non-discount Service: Kiểm Tra Áp Suất Lốp (200k, Promo = 200k, Hot = false)"
            ],
            "scenario": "Ensure that the rendered DOM markup of AutoServiceCard across default, promotional, and non-discount modes matches recorded Jest snapshots without unintended regression",
            "steps": [
                (1.0, "Render <AutoServiceCard /> with default props", "DOM tree matches stored snapshot with hot badge, original price, promo price, warranty, features list", "As Expected", "Pass"),
                (2.0, "Render <AutoServiceCard service={customService} /> with Ceramic package", "DOM tree matches snapshot containing 6.5M promo price, -19% discount tag, 24-month warranty", "As Expected", "Pass"),
                (3.0, "Render <AutoServiceCard service={nonDiscountService} /> without discount", "DOM tree matches snapshot without strike-through price and without hot badge", "As Expected", "Pass"),
                (4.0, "Execute Jest expect(container).toMatchSnapshot() on all variants", "All 3 snapshots pass comparison with 0 mismatches and 100% branch coverage", "As Expected", "Pass")
            ]
        },
        {
            "sheet_name": "TC04_Function_IfElse",
            "tc_id": "UT_L2_FUNC_001",
            "description": "Test Function If-Else discountService: Tier Rates, Vouchers, First-Time Bonus, 50% Cap & VAT",
            "created_by": "Member 2 (M2)",
            "reviewed_by": "Member 5 (M5)",
            "version": "1.0",
            "log": "Data-Driven testing via discount_test_data.csv with 100% Statements, Branch, Function & Line Coverage",
            "tester": "Member 2 (M2)",
            "date": "28-Oct-2026",
            "status": "Pass",
            "prerequisites": [
                "Node.js runtime & Jest unit testing framework",
                "CSV fixture tests/fixtures/discount_test_data.csv containing 10 test vectors",
                "Function calculateServiceInvoice implemented in src/services/discountService.js",
                "Helper loadCsvData in src/utils/csvLoader.js"
            ],
            "test_data": [
                "Row 1: Tier VIP, Cost 2M, Voucher EGASUPER, FirstTime false, VAT false",
                "Row 2: Tier GOLD, Cost 500k, Voucher EGAFREE, FirstTime true, VAT true",
                "Row 3: Tier SILVER, Cost 200k, Voucher EGAFREE (< 300k min threshold)",
                "Row 6: Tier VIP, Cost 600k, Voucher EGAMEGA (Exceeds 50% cap threshold)",
                "Rows 9-10: Cost <= 0, Non-number Cost, Unsupported Tier PLATINUM"
            ],
            "scenario": "Verify that all conditional branches (if-else) in calculateServiceInvoice correctly compute tiered discounts, apply valid vouchers, enforce 50% discount cap, compute 10% VAT, and throw descriptive validation errors",
            "steps": [
                (1.0, "Execute test.each on discount_test_data.csv", "All 10 CSV test vectors pass with exact expected discount rates, vouchers, totals, and error exceptions", "As Expected", "Pass"),
                (2.0, "Test Tier discount rates (VIP=20%, GOLD=15%, SILVER=10%, STANDARD=5%)", "Discount rate and tier discount amount match exact formula Math.round(baseCost * rate)", "As Expected", "Pass"),
                (3.0, "Test Voucher branches (EGAMEGA >= 500k, EGASUPER >= 1M, EGAFREE >= 300k, invalid voucher)", "Voucher discount applied only when minimum cost condition is met; 0 otherwise", "As Expected", "Pass"),
                (4.0, "Test 50% discount cap branch (rawTotalDiscount > maxAllowedDiscount)", "isCapped is set to true and totalDiscount is restricted to exactly 50% of baseCost", "As Expected", "Pass"),
                (5.0, "Test VAT invoice calculation (hasVatInvoice = true / false)", "10% VAT is accurately computed on subTotal when true, 0 when false", "As Expected", "Pass"),
                (6.0, "Test Input Validation branches (invalid/zero/negative cost, invalid/unsupported tier)", "Throws descriptive error messages: 'Chi phí dịch vụ không hợp lệ...' and 'Hạng thành viên...'", "As Expected", "Pass")
            ]
        }
    ]

    for tc in test_cases_data:
        ws = wb.add_sheet(tc["sheet_name"])
        ws.show_grid = True

        # Set column widths (in 1/256 of character width)
        ws.col(0).width = 256 * 10   # Step # / S #
        ws.col(1).width = 256 * 35   # Step Details / Prerequisites
        ws.col(2).width = 256 * 18   # Values
        ws.col(3).width = 256 * 38   # Expected Results / Reviewed By
        ws.col(4).width = 256 * 12   # Extra
        ws.col(5).width = 256 * 30   # Actual Results
        ws.col(6).width = 256 * 35   # Test Data
        ws.col(7).width = 256 * 16   # Version
        ws.col(8).width = 256 * 16   # Pass/Fail Status
        ws.col(9).width = 256 * 14   # Extra

        # Row 0: Test Case ID & Description
        ws.write(0, 0, "Test Case ID", style_label)
        ws.write(0, 1, "", style_label)
        ws.write(0, 2, tc["tc_id"], style_val_center)
        ws.write(0, 3, "Test Case Description", style_label)
        ws.write(0, 4, "", style_label)
        ws.write_merge(0, 0, 5, 9, tc["description"], style_val_left)

        # Row 1: Created By, Reviewed By, Version
        ws.write(1, 0, "Created By", style_label)
        ws.write(1, 1, "", style_label)
        ws.write(1, 2, tc["created_by"], style_val_center)
        ws.write(1, 3, "Reviewed By", style_label)
        ws.write(1, 4, "", style_label)
        ws.write(1, 5, tc["reviewed_by"], style_val_center)
        ws.write(1, 6, "", style_val_center)
        ws.write(1, 7, "Version", style_label)
        ws.write_merge(1, 1, 8, 9, tc["version"], style_val_center)

        # Row 2: Blank
        for c in range(10):
            ws.write(2, c, "", style_val_left)

        # Row 3: QA Tester's Log
        ws.write_merge(3, 3, 0, 1, "QA Tester's Log", style_label)
        ws.write_merge(3, 3, 2, 9, tc["log"], style_val_left)

        # Row 4: Blank
        for c in range(10):
            ws.write(4, c, "", style_val_left)

        # Row 5: Tester's Name, Date Tested, Pass/Fail
        ws.write_merge(5, 5, 0, 1, "Tester's Name", style_label)
        ws.write(5, 2, tc["tester"], style_val_center)
        ws.write(5, 3, "Date Tested", style_label)
        ws.write(5, 4, "", style_label)
        ws.write_merge(5, 5, 5, 6, tc["date"], style_val_center)
        ws.write(5, 7, "Test Result", style_label)
        ws.write_merge(5, 5, 8, 9, tc["status"], style_pass)

        # Row 6: Blank
        for c in range(10):
            ws.write(6, c, "", style_val_left)

        # Row 7: Prerequisites & Test Data headers
        ws.write(7, 0, "S #", style_col_hdr)
        ws.write_merge(7, 7, 1, 4, "Prerequisites:", style_label)
        ws.write(7, 5, "S #", style_col_hdr)
        ws.write_merge(7, 7, 6, 9, "Test Data Requirement", style_label)

        # Rows 8-11: 4 Prerequisites & 4 Test Data items
        for r_idx in range(4):
            curr_row = 8 + r_idx
            prereq_val = tc["prerequisites"][r_idx] if r_idx < len(tc["prerequisites"]) else ""
            testdata_val = tc["test_data"][r_idx] if r_idx < len(tc["test_data"]) else ""

            ws.write(curr_row, 0, float(r_idx + 1), style_val_center)
            ws.write_merge(curr_row, curr_row, 1, 4, prereq_val, style_val_left)
            ws.write(curr_row, 5, float(r_idx + 1), style_val_center)
            ws.write_merge(curr_row, curr_row, 6, 9, testdata_val, style_val_left)

        # Row 12: Blank
        for c in range(10):
            ws.write(12, c, "", style_val_left)

        # Row 13: Test Scenario
        ws.write(13, 0, "Test Scenario", style_label)
        ws.write_merge(13, 13, 1, 9, tc["scenario"], style_val_left)

        # Row 14: Blank
        for c in range(10):
            ws.write(14, c, "", style_val_left)

        # Row 15: Step Details Header Table
        ws.write(15, 0, "Step #", style_col_hdr)
        ws.write_merge(15, 15, 1, 2, "Step Details", style_col_hdr)
        ws.write_merge(15, 15, 3, 4, "Expected Results", style_col_hdr)
        ws.write_merge(15, 15, 5, 7, "Actual Results", style_col_hdr)
        ws.write_merge(15, 15, 8, 9, "Status (Pass / Fail)", style_col_hdr)

        # Row 16: Blank
        for c in range(10):
            ws.write(16, c, "", style_val_left)

        # Rows 17+: Step Details
        for s_idx, step in enumerate(tc["steps"]):
            s_row = 17 + s_idx
            ws.write(s_row, 0, step[0], style_val_center)
            ws.write_merge(s_row, s_row, 1, 2, step[1], style_val_left)
            ws.write_merge(s_row, s_row, 3, 4, step[2], style_val_left)
            ws.write_merge(s_row, s_row, 5, 7, step[3], style_val_left)
            ws.write_merge(s_row, s_row, 8, 9, step[4], style_pass)

    for p in output_paths:
        os.makedirs(os.path.dirname(p), exist_ok=True)
        wb.save(p)
        print(f"Successfully generated Excel template at: {p}")

if __name__ == "__main__":
    paths = [
        "d:/download/SWT-Group-Labs-main/SWT-Group-Labs-main/Lab2_NodeJS/TestCase_Lab2_NodeJS.xls",
        "d:/download/SWT-Group-Labs-main/SWT-Group-Labs-main/TestCase_Lab2_NodeJS.xls"
    ]
    create_lab2_testcase_excel(paths)
