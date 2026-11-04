# LAB 4: ISTQB SOFTWARE TEST PLAN & AI APPLICATION EVIDENCE (EGA AUTOCARE SYSTEM)

> **Document ID**: `TP-SWT-EGA-2026-V1.0`  
> **Standards Applied**: **ISTQB® CTFL v4.0 / IEEE Std 829 / ISO/IEC/IEEE 29119-3**  
> **System Under Test (SUT)**: **EGA Autocare** Automotive Parts & Maintenance E-Commerce System (`https://ega-autocare.mysapo.net/`)  
> **Primary Author**: **M5 - Trần Thanh Nguyên** (in collaboration with **M1 - Nguyễn Hoàng Vinh**, **M2 - Đinh Thành Trung**, **M3 - Trần Công Tiến**, **M4 - Văn Thái Trung**)  

---

## PART A: ISTQB SOFTWARE TEST PLAN (SECTIONS 1 TO 12)

---

### 1. Test Plan Identifier

| Attribute | Detailed Description |
| :--- | :--- |
| **Test Plan Document ID** | `TP-SWT-EGA-2026-V1.0` |
| **Project / System Under Test** | **EGA Autocare** Automotive Parts & Garage Maintenance System (`https://ega-autocare.mysapo.net/`) |
| **Document Version** | `1.0 (Final Release)` |
| **Release Date** | `03-Nov-2026` |
| **Reference Standards** | ISTQB® Certified Tester Foundation Level (CTFL v4.0) & IEEE Std 829 / ISO/IEC/IEEE 29119-3 |
| **Authors & Project Team** | • **M1 - Nguyễn Hoàng Vinh (Leader)**: Lab 1 Lead (Java / JUnit 5 / JaCoCo) & Test Plan Approval<br>• **M2 - Đinh Thành Trung**: Lab 2 Lead (Node.js / React / Jest / RTL)<br>• **M3 - Trần Công Tiến**: Lab 3 Lead - Part 1 (Selenium Access, Valid Login, Invalid Login, Search)<br>• **M4 - Văn Thái Trung**: Lab 3 Lead - Part 2 (Selenium Multi-Product Cart & Total Price Verification)<br>• **M5 - Trần Thanh Nguyên**: Lab 4 Lead (ISTQB Test Plan, AI Evidence Log & Test Case Standardization) |

---

### 2. Introduction & Purpose

#### 2.1. Project Background
**EGA Autocare** (`https://ega-autocare.mysapo.net/`) is an e-commerce platform specializing in genuine automotive parts, car care accessories (Michelin tire inflators, Areon car perfumes, AutoGuard car covers, etc.), and scheduled garage maintenance services. To guarantee financial calculation accuracy (quantity discounts, installation fees, membership tier discounts, shipping fees, and VAT tax), UI component stability, and end-to-end online shopping reliability, our team conducts a comprehensive software testing campaign adhering to **ISTQB** standards.

#### 2.2. Purpose of This Document
1. Define the testing scope (**In-Scope** and **Out-of-Scope**) across three testing levels: **Unit Testing (Lab 1)**, **Component & Snapshot Testing (Lab 2)**, and **System End-to-End Web Testing (Lab 3)**.
2. Specify the test design methodologies (White-Box Testing, Black-Box EP/BVA, and Data-Driven Testing via `.csv` files) required to achieve **100% Code Coverage** and **100% Test Automation**.
3. Establish clear **Item Pass/Fail Criteria**, **Suspension/Resumption Requirements**, **Resource & Schedule Milestones**, **Risk Management**, and **Test Deliverables** for the entire 5-member team.

---

### 3. Test Items

| Item ID | Subsystem / Module Name | Technology Stack & Versions | Source Code Path in Repository |
| :---: | :--- | :--- | :--- |
| **TI-01** | **Automotive Parts & Installation Billing Service (`EgaAutocareBillingService`)** | Java 17, Maven 3.9, JUnit Jupiter 5.10.2, JaCoCo 0.8.12 | `Lab1_JUnit5/src/main/java/com/mycompany/lab1/EgaAutocareBillingService.java` |
| **TI-02** | **Frontend React UI Components & Service Invoice Calculator** | Node.js, React 18.2, Jest 29.7, React Testing Library 14.2 | `Lab2_NodeJS/src/components/*.jsx`<br>`Lab2_NodeJS/src/services/discountService.js`<br>`Lab2_NodeJS/src/utils/csvLoader.js` |
| **TI-03** | **EGA Autocare Live E-Commerce Web Storefront** | Sapo Web Platform (`https://ega-autocare.mysapo.net/`), Selenium WebDriver 4.25.0 | `Lab3_Selenium_EGA_Autocare/src/test/java/com/mycompany/egaautocare/EgaAutocareTest.java` |

---

### 4. Features to be Tested (In-Scope)

| Feature ID | Feature Name & Business Specification | Test Level | Priority | Linked Test Case IDs |
| :---: | :--- | :---: | :---: | :--- |
| **FT-01** | Validate order input boundaries (`isValidOrderInput`: quantity in `1..100`, unitPrice `> 0`, distanceKm `>= 0`) | Unit Test (Lab 1) | High | `UT_L1_01` (`8 CSV rows`) |
| **FT-02** | Calculate parts subtotal with bulk quantity discounts (`calculatePartsSubtotal`: `>= 10` items gets 10% off, `>= 4` items gets 5% off) | Unit Test (Lab 1) | High | `UT_L1_02` (`6 CSV rows`) |
| **FT-03** | Calculate garage installation labor fees by service package (`calculateInstallationFee`: `NONE`, `PREMIUM`, `STANDARD`, `BASIC`) | Unit Test (Lab 1) | Medium | `UT_L1_03` (`5 CSV rows`) |
| **FT-04** | Calculate loyalty membership tier discounts (`calculateMemberDiscount`: `VIP` 15%, `GOLD` 10%, `SILVER` 5%, `REGULAR` 0%) | Unit Test (Lab 1) | High | `UT_L1_04` (`6 CSV rows`) |
| **FT-05** | Calculate delivery shipping fees (`calculateShippingFee`: free for garage pickup `0km` or net order `>= 2,000,000 VND`, urban vs. suburban rates) | Unit Test (Lab 1) | Medium | `UT_L1_05` (`5 CSV rows`) |
| **FT-06** | Calculate 8% VAT tax (`calculateVatTax`) & End-to-End Chained Final Invoice Total (`calculateFinalInvoiceTotal` with `200,000 VND` rebate for orders `>= 10M VND`) | Unit & Integration (Lab 1) | Critical | `UT_L1_06` & `UT_L1_07` (`9 CSV rows`) |
| **FT-07** | Interactive Garage Service Booking Button (`ServiceBookingButton`: slot decrement, booking code `EGA-1001`, disabled state at `0` slots, Reset button) | Component Test (Lab 2) | High | `UT_L2_BTN_001` (`6 test methods`) |
| **FT-08** | Dynamic Membership Greeting, Tier Privilege Badge, Loyalty Point Conversion & Maintenance Alert (`MembershipGreetingBanner`) | Component CSV Test (Lab 2) | High | `UT_L2_STR_001` (`TC_GREET_01` → `05`) |
| **FT-09** | DOM Structure Regression & Snapshot Verification for Service Package Cards (`AutoServiceCard`: Default, Hot Deal Promo, Basic) | Snapshot Test (Lab 2) | Medium | `UT_L2_SNAP_001` (`3 Snapshots`) |
| **FT-10** | Maintenance Service Invoice Calculation (`calculateServiceInvoice`: input validation, vouchers `EGAMEGA`/`EGASUPER`/`EGAFREE`, first-time 5% bonus, 50% cap, 10% VAT) | Unit CSV Test (Lab 2) | Critical | `UT_L2_FUNC_001` (`TC_DISC_01` → `ERR_02`) |
| **FT-11** | Storefront Access (`ega-autocare.mysapo.net`), Valid Customer Login (`linhcampochia@gmail.com`), and Unregistered Account Login Rejection | System E2E Test (Lab 3) | Critical | `ST_L3_001` → `ST_L3_003` |
| **FT-12** | Product Keyword Search (`"bơm lốp"`), Adding at Least 3 Distinct Products to Cart, and Verifying Exact Cart Total Price (`1,915,000 VND`) | System E2E Test (Lab 3) | Critical | `ST_L3_004` → `ST_L3_006` |

---

### 5. Features NOT to be Tested (Out-of-Scope)

To focus resources on core requirements and prevent unintended side effects on the live production storefront, the following features are excluded from scope:
1. **Real Payment Gateway Execution & Final Order Submission**: Automation stops at shopping cart verification (`/cart`) to avoid generating fake commercial orders on the live `ega-autocare.mysapo.net` store.
2. **Performance, Load & Stress Testing**: High-concurrency traffic simulation against Sapo cloud servers is out of scope.
3. **Penetration & Security Exploitation Testing**: Only functional authentication validation (`ST_L3_002`, `ST_L3_003`) is tested; SQL injection or DDoS testing is excluded.
4. **Back-Office Admin CMS & Inventory Management**: Testing is strictly scoped to customer-facing storefront workflows and standalone billing/UI modules.

---

### 6. Approach / Test Strategy

Our test strategy follows the **Testing Pyramid** combined with **100% Full Test Automation**:

1. **White-Box Testing Technique**:
   - Enforces **100% Statement/Instruction Coverage** and **100% Branch/Decision Coverage** across all conditional `if - else if - else` structures and compound boolean expressions (`||`, `&&`).
   - Measured automatically using **JaCoCo Maven Plugin `0.8.12`** (Lab 1) and **Jest Coverage Engine** with `coverageThreshold = 100%` (Lab 2).
2. **Black-Box Testing Technique**:
   - **Equivalence Partitioning (EP)**: Divides inputs into valid partitions (quantities `1..3`, `4..9`, `10..100`; tiers `VIP`, `GOLD`, `SILVER`, `REGULAR`) and invalid partitions (`<= 0`, `> 100`, negative prices, unsupported tiers).
   - **Boundary Value Analysis (BVA)**: Targets exact boundary thresholds (`itemQuantity = 0, 1, 4, 10, 100, 101`; `distanceKm = 0, 10`; free-shipping threshold `2,000,000 VND`; high-value order threshold `10,000,000 VND`; maximum discount cap `50%`).
3. **Data-Driven Testing (DDT via CSV Fixtures)**:
   - Decouples test inputs and expected outputs from test scripts into external `.csv` files (`7 CSV files` in Lab 1 and `2 CSV files` in Lab 2) loaded dynamically at runtime.
4. **End-to-End Web UI Automation (Selenium WebDriver 4)**:
   - Uses **Selenium 4.25.0** with built-in *Selenium Manager*, explicit `WebDriverWait` (`ExpectedConditions`), scoped CSS selectors, and numeric price parsing (`replaceAll("[^0-9]", "")`) to verify exact shopping cart totals.

---

### 7. Item Pass/Fail Criteria

#### 7.1. Pass Criteria by Test Level
| Test Level | Execution Criteria | Code Coverage Criteria | Reporting & Documentation Criteria |
| :--- | :--- | :--- | :--- |
| **Lab 1 (JUnit 5)** | **39/39** automated CSV test cases `Pass` (`0 Failures, 0 Errors`) | **100%** Instructions (`225/225`)<br>**100%** Branches (`46/46`)<br>**100%** Methods (`8/8`) | JaCoCo HTML/XML/CSV reports + `TestCase_Lab1_JUnit5.xls` completed |
| **Lab 2 (NodeJS)** | **41/41** test cases & **3/3** Snapshots `Pass` | **100%** Statements (`143/143`)<br>**100%** Branches (`113/113`)<br>**100%** Functions & Lines | Jest LCOV HTML report + Snapshot file + `TestCase_Lab2_NodeJS.xls` completed |
| **Lab 3 (Selenium)** | **6/6** ordered E2E scenarios (`@Order(1)` → `@Order(6)`) `Pass` on live `ega-autocare.mysapo.net` | Covers 100% of required functional scenarios `(i)` through `(vi)` | 3-product cart total matches exact `1,915,000 VND` + `TestCase_Lab3_Selenium.xls` completed |

#### 7.2. Fail Criteria
- Any automated unit, component, snapshot, or Selenium E2E test results in `Failed` or `Error`.
- Statement or Branch Coverage in JaCoCo (Lab 1) or Jest (Lab 2) drops below `100%`.
- Selenium E2E assertions fail to verify live DOM values or hardcode trivial assertions.

---

### 8. Suspension Criteria and Resumption Requirements

#### 8.1. Suspension Criteria
1. **Live Storefront Outage or Anti-Bot Captcha (Lab 3)**: `https://ega-autocare.mysapo.net/` becomes unreachable (HTTP 5xx or timeout > 30s) or enables interactive Captcha on `/account/login`.
2. **Product Inventory Change on Live Store**: Any of the 3 target products (`/michelin-m2566-bom-lop`, `/areon-tinh-dau-treo-xe`, `/autoguard-bat-phu-xe`) becomes out-of-stock or unpublished.
3. **Build Environment Failure (Lab 1 & Lab 2)**: Corrupted Maven `.m2` repository or missing `node_modules` dependencies preventing test compilation.

#### 8.2. Resumption Requirements
1. Network connectivity and `ega-autocare.mysapo.net` availability are restored (`test01_AccessWeb` passes).
2. If a sample product goes out of stock, the product slug array `productSlugs` in `EgaAutocareTest.java` is updated with an available item and the full regression suite (`@Order(1)` → `@Order(6)`) is re-executed.
3. JDK/Maven and Node.js dependencies are restored and verified via `mvn clean test` and `npx jest --coverage`.

---

### 9. Test Deliverables

| No. | Deliverable Name | Format | Repository Location | Status |
| :---: | :--- | :---: | :--- | :---: |
| **1** | Lab 1 Java Source (7 methods) + JUnit 5 Test Suite + 7 CSV Datasets + 100% JaCoCo Report | `.java`, `.csv`, `.html`, `.xml` | `Lab1_JUnit5/` | Completed |
| **2** | Lab 1 Test Case Specification Workbook (7 Sheets for 7 functions) | `.xls` | `Lab1_JUnit5/TestCase_Lab1_JUnit5.xls` | Completed |
| **3** | Lab 2 React/Node.js Source + Jest Test Suite + 2 CSV Datasets + 3 Snapshots + 100% Coverage Report | `.jsx`, `.js`, `.csv`, `.snap`, `.html` | `Lab2_NodeJS/` | Completed |
| **4** | Lab 2 Test Case Specification Workbook (4 Sheets: Button, String, Snapshot, Function If-Else) | `.xls` | `Lab2_NodeJS/TestCase_Lab2_NodeJS.xls` | Completed |
| **5** | Lab 3 Selenium WebDriver E2E Test Suite (6 scenarios on EGA Autocare) + Surefire Reports | `.java`, `.xml`, `.txt` | `Lab3_Selenium_EGA_Autocare/` | Completed |
| **6** | Lab 3 Test Case Specification Workbook (6 Sheets for 6 E2E flows) | `.xls` | `Lab3_Selenium_EGA_Autocare/TestCase_Lab3_Selenium.xls` | Completed |
| **7** | **ISTQB Software Test Plan Document (Sections 1 to 12)** | `.docx`, `.md` | `Lab4_ISTQB_TestPlan/Lab4_ISTQB_TestPlan.docx` | Completed |
| **8** | **AI Application Evidence & Prompt Engineering Log (10% Rubric Score)** | `.docx`, `.md` | `Lab4_ISTQB_TestPlan/Lab4_AI_Application_Evidence.docx` | Completed |

---

### 10. Testing Tasks, Staffing & Schedule

#### 10.1. Staffing & Responsibilities Matrix

| Member ID & Name | Project Role | Primary Responsibilities | Tools & Techniques Used | Specific Deliverables | Completion |
| :---: | :--- | :--- | :--- | :--- | :---: |
| **M1 - Nguyễn Hoàng Vinh (Leader)** | Project Manager & JUnit 5 Lead | • Overall project management & GitHub repository (`main`)<br>• Design 7 interconnected methods in `EgaAutocareBillingService`<br>• Write JUnit 5 `@ParameterizedTest` with 7 `.csv` files & achieve 100% JaCoCo Coverage | Java 17, Maven 3.9, JUnit 5.10.2, JaCoCo 0.8.12, BVA & EP, White-Box Testing | • `EgaAutocareBillingService.java`<br>• `EgaAutocareBillingServiceTest.java`<br>• 7 `.csv` files (39 test cases)<br>• `TestCase_Lab1_JUnit5.xls` | **100%** (20% workload) |
| **M2 - Đinh Thành Trung** | Node.js & React Testing Lead | • Implement 3 React Components (`ServiceBookingButton`, `MembershipGreetingBanner`, `AutoServiceCard`) & `discountService`<br>• Write Jest tests reading 2 `.csv` files, generate 3 Snapshots & achieve 100% Jest Coverage | Node.js, React 18.2, Jest 29.7, React Testing Library 14.2, Snapshot & CSV Testing | • `src/components/*.jsx`, `discountService.js`, `csvLoader.js`<br>• 5 `*.test.js` suites (41 tests + 3 Snapshots)<br>• `TestCase_Lab2_NodeJS.xls` | **100%** (20% workload) |
| **M3 - Trần Công Tiến** | Selenium Automation Engineer (Part 1) | • Configure `WebDriver` & `WebDriverWait`<br>• Automate first 4 E2E scenarios (`@Order(1)` → `@Order(4)`): Access Website, Valid Login, Invalid Login, Product Search | Java, Maven, JUnit 5, Selenium WebDriver 4.25.0, CSS Selectors, JavascriptExecutor | • `EgaAutocareTest.java` (`test01` → `test04`)<br>• Sheets `TC01` → `TC04` in `TestCase_Lab3_Selenium.xls` | **100%** (20% workload) |
| **M4 - Văn Thái Trung** | Selenium Automation Engineer (Part 2) | • Automate shopping cart E2E scenarios (`@Order(5)` → `@Order(6)`): Clear cart, buy 3 distinct products, parse unit prices, and verify exact cart total (`1,915,000 VND`) | Selenium WebDriver 4.25.0, DOM Price Parsing, Regular Expressions, Surefire Report | • `EgaAutocareTest.java` (`test05` → `test06`)<br>• Sheets `TC05` → `TC06` in `TestCase_Lab3_Selenium.xls` | **100%** (20% workload) |
| **M5 - Trần Thanh Nguyên** | QA Documentation & ISTQB Lead | • Author the ISTQB Software Test Plan (Sections 1 → 12)<br>• Compile the AI Application Evidence Log (10% rubric score)<br>• Standardize and cross-check all 3 Excel Test Case workbooks | ISTQB CTFL v4.0, IEEE 829, Risk-Based Testing, Prompt Engineering | • `Lab4_ISTQB_TestPlan.docx`<br>• `Lab4_AI_Application_Evidence.docx`<br>• Review `TestCase_Lab1/2/3.xls` | **100%** (20% workload) |

#### 10.2. Project Schedule & Milestone Tracking

| Milestone | Phase & Detailed Activities | Assigned Members | Timeline | Exit Criteria | Status & Progress |
| :---: | :--- | :---: | :---: | :--- | :---: |
| **Milestone 1** | **Project Initialization, Rubric Analysis & Architecture Design**<br>• Initialize GitHub repository, 4 Lab directory structure, and whitelist `.gitignore`<br>• Analyze `LabTesting.txt` & `barem lab.xlsx` grading criteria<br>• Define specifications for 7 Java methods (Lab 1), 4 Node.js modules (Lab 2), 6 Selenium flows (Lab 3) & ISTQB outline (Lab 4) | **M1 - Nguyễn Hoàng Vinh<br>M2 - Đinh Thành Trung<br>M3 - Trần Công Tiến<br>M4 - Văn Thái Trung<br>M5 - Trần Thanh Nguyên** | `28-Oct-2026` – `29-Oct-2026` | • Maven (`pom.xml`) and Node (`package.json`) builds verified<br>• All 5 members aligned on input/output specifications | **Completed (100%)** |
| **Milestone 2** | **Core Source Code & Test Script Implementation**<br>• **M1 (Nguyễn Hoàng Vinh)**: Implement 7 methods in `EgaAutocareBillingService` + 7 CSV files<br>• **M2 (Đinh Thành Trung)**: Implement Button, String Banner, Card, `discountService` + 2 CSV files<br>• **M3 (Trần Công Tiến) & M4 (Văn Thái Trung)**: Implement 6 Selenium E2E tests on `ega-autocare.mysapo.net`<br>• **M5 (Trần Thanh Nguyên)**: Draft Sections 1 → 9 of `Lab4_ISTQB_TestPlan.docx` & record AI Prompts | **M1 - Nguyễn Hoàng Vinh<br>M2 - Đinh Thành Trung<br>M3 - Trần Công Tiến<br>M4 - Văn Thái Trung<br>M5 - Trần Thanh Nguyên** | `30-Oct-2026` – `01-Nov-2026` | • All business logic and test suites compile cleanly<br>• Initial functional test runs pass | **Completed (100%)** |
| **Milestone 3** | **100% Coverage Optimization & End-to-End Integration**<br>• **M1 (Nguyễn Hoàng Vinh)**: Run JaCoCo achieving `100% Instructions (225/225)` & `100% Branches (46/46)`<br>• **M2 (Đinh Thành Trung)**: Run `jest --coverage` achieving `100% Coverage` & generate 3 Snapshots<br>• **M3 (Trần Công Tiến) & M4 (Văn Thái Trung)**: Merge `@Order(1)` → `@Order(6)` in `EgaAutocareTest.java`, verifying `1,915,000 VND` cart total<br>• **M5 (Trần Thanh Nguyên)**: Complete Sections 10–12 (Risks) & `Lab4_AI_Application_Evidence.docx` | **M1 - Nguyễn Hoàng Vinh<br>M2 - Đinh Thành Trung<br>M3 - Trần Công Tiến<br>M4 - Văn Thái Trung<br>M5 - Trần Thanh Nguyên** | `02-Nov-2026` | • Lab 1: `39/39 Pass` (100% JaCoCo)<br>• Lab 2: `41/41 Pass`, `3/3 Snapshots` (100% Jest)<br>• Lab 3: `6/6 Pass` on live Chrome | **Completed (100%)** |
| **Milestone 4** | **Documentation Synchronization, Cross-Review & Final Release**<br>• **M1 → M4**: Populate full English test specifications in `TestCase_Lab1_JUnit5.xls`, `TestCase_Lab2_NodeJS.xls`, `TestCase_Lab3_Selenium.xls`<br>• **M5 (Trần Thanh Nguyên) & M1 (Nguyễn Hoàng Vinh)**: Perform final QA cross-check between Word/Excel docs and code, push final release to `main` | **M1 - Nguyễn Hoàng Vinh<br>M2 - Đinh Thành Trung<br>M3 - Trần Công Tiến<br>M4 - Văn Thái Trung<br>M5 - Trần Thanh Nguyên** | `03-Nov-2026` | • All 4 Lab directories finalized on `main` branch<br>• 100% ready for final presentation & defense | **Completed (100%)** |

---

### 11. Environmental Needs & Tools
- **Operating System**: Windows 11 (64-bit).
- **Lab 1 & Lab 3 Environment**: Java Development Kit (JDK 17 / JDK 21), Apache Maven `3.9.9`, Apache NetBeans IDE, JUnit Jupiter `5.10.2` / `5.6.0`, JaCoCo `0.8.12`, Selenium WebDriver `4.25.0`, Google Chrome browser.
- **Lab 2 Environment**: Node.js `v24.x`, npm `11.x`, React `18.2.0`, Jest `29.7.0`, `@testing-library/react` `14.2.1`, Babel.
- **Version Control System**: Git & GitHub (`https://github.com/ViktorWilson7777/SWT-Group-Labs`).

---

### 12. Risks & Contingencies (Risk-Based Testing Management)

#### 12.1. Product & Technical Risks

| Risk ID | Technical / Business Risk Description | Likelihood | Impact | Risk Level | Mitigation Strategy & Contingency Plan |
| :---: | :--- | :---: | :---: | :---: | :--- |
| **R-01** | Compound boolean conditions (`\|\|`, `&&`) or unreachable defensive branches causing **Branch Coverage** to fall below 100% in Lab 1 & Lab 2 | Medium | Very High (Lose 10% Coverage score) | **High** | • **Mitigation**: Inspect line-by-line branch highlights in JaCoCo (`0.8.12`) and Jest HTML reports.<br>• **Contingency**: Eliminate redundant checks, align voucher tier thresholds, and add boundary CSV rows until `0 missed branches` is achieved. |
| **R-02** | Empty cells in `.csv` files parsed as empty strings `""` or whitespace `"   "` instead of `null`, causing unexpected `if-else` branch execution | High | High | **High** | • **Mitigation**: Standardize `csvLoader.js` and enforce `str && str.trim() !== ""` checks across components and services.<br>• **Contingency**: Add dedicated unit tests (`csvLoader.test.js`) covering blank lines, `#` comments, and whitespace values. |
| **R-03** | Generic CSS selectors (`button[type='submit']`, `.price`) on Sapo Web (`ega-autocare.mysapo.net`) matching the header search button or hidden price spans | Very High | Very High (Fails Lab 3) | **Critical** | • **Mitigation**: Scope CSS selectors strictly to parent containers (`#customer_login button.btn-login`, `span.price.text-h4`, `.cart-total .price`).<br>• **Contingency**: Use `JavascriptExecutor` clicks and extract `textContent` with explicit `WebDriverWait`. |
| **R-04** | Residual shopping cart items stored in browser cookies/session distorting the total price in `test05` and `test06` | Medium | High | **High** | • **Mitigation**: Explicitly navigate to `driver.get(BASE_URL + "/cart/clear")` at the start of `test05_BuyAtLeast3Products()` to reset the cart to `0 VND`.<br>• **Contingency**: Dynamically accumulate `expectedCartTotal` across the 3 added products for exact comparison in `test06`. |
| **R-05** | Live storefront `ega-autocare.mysapo.net` latency, product stock-outs, or Chrome browser version mismatches on grading machines | Low | Very High | **Medium** | • **Mitigation**: Use **Selenium 4.25.0** with automatic *Selenium Manager* driver resolution and a 15-second `WebDriverWait`.<br>• **Contingency**: Maintain backup in-stock product slugs and support `-Dheadless=true` execution. |

#### 12.2. Project & Team Coordination Risks

| Risk ID | Project Management Risk Description | Risk Level | Practical Resolution |
| :---: | :--- | :---: | :--- |
| **R-06** | Git merge conflicts or accidental commits of local scratch files/reference documents outside the 4 Lab folders | Medium | Enforce a **Whitelist `.gitignore` (`/*`)** at the repository root so only the 4 official folders (`Lab1_JUnit5/`, `Lab2_NodeJS/`, `Lab3_Selenium_EGA_Autocare/`, `Lab4_ISTQB_TestPlan/`) are tracked on `main`. |
| **R-07** | Discrepancies between automated test code (JUnit/Jest/Selenium) and documentation (`TestCaseTemplate.xls`, `Lab4_ISTQB_TestPlan.docx`) | High | **M5 (Trần Thanh Nguyên)** and **M1 (Nguyễn Hoàng Vinh)** perform a full cross-check of all Test Case IDs (`UT_L1_...`, `UT_L2_...`, `ST_L3_...`) and the exact cart total (`1,915,000 VND`) after all suites pass 100%. |

---

## AI APPENDIX: AI APPLICATION EVIDENCE & PROMPT ENGINEERING LOG (10% RUBRIC SCORE - LAB 4)

In accordance with `Note iii. AI tools may be used for implementation` in [LabTesting.txt](file:///c:/Users/DELL/Downloads/lab%20SWT/LabTesting.txt) and the **`AI (0.1 = 10% score)`** grading criterion in Sheet `Lab4` of `barem lab.xlsx`, our team applied a controlled **Human-in-the-Loop AI-Assisted Software Testing** workflow throughout the Software Testing Life Cycle (STLC).

### Table 1 — AI Application Matrix Across STLC Phases

| STLC Phase | AI Application Area | Role of AI Assistant | Human-in-the-Loop Verification (Team Members) |
| :--- | :--- | :--- | :--- |
| **1. Requirements & Rubric Analysis** | Analyzing `LabTesting.txt` & `barem lab.xlsx` | Parsed grading weights and identified hidden rubric criteria across Excel sheets | **M1 (Nguyễn Hoàng Vinh)** & **M5 (Trần Thanh Nguyên)** verified every column in `barem lab.xlsx` and finalized the 5-member task allocation |
| **2. Test Design & CSV Generation** | Lab 1 (JUnit 5) & Lab 2 (Node.js/Jest) | Suggested BVA/EP test vectors and detected unreachable code branches | **M1 (Nguyễn Hoàng Vinh)** & **M2 (Đinh Thành Trung)** executed JaCoCo (`0.8.12`) and Jest Coverage to confirm `100% Branch Coverage` |
| **3. E2E Web Test Automation** | Lab 3 (Selenium WebDriver) | Analyzed DOM structure on `ega-autocare.mysapo.net` and scaffolded `@Order(1)` → `@Order(6)` methods | **M3 (Trần Công Tiến)** & **M4 (Văn Thái Trung)** debugged live Chrome execution, fixed CSS selector collisions, and verified the `1,915,000 VND` cart total |
| **4. ISTQB Documentation & QA** | Lab 4 (`Lab4_ISTQB_TestPlan.docx`) | Structured the 12 sections following IEEE 829 / ISTQB CTFL v4.0 templates | **M5 (Trần Thanh Nguyên)** cross-referenced all Test Case IDs, coverage metrics, and file paths against the live repository |

---

### Table 2 — Detailed Log of 6 AI Prompts Used & Human Verification Results

#### Prompt 1 — Grading Rubric Analysis & 5-Member Workload Allocation
- **Prompt Used**:
  > *"Based on the requirements in `LabTesting.txt` (4 Labs: 1. JUnit5, 2. NodeJS, 3. Selenium web testing, 4. ISTQB Test Plan) and the grading rubric `barem lab.xlsx`, analyze the scoring criteria for each Lab and create a balanced task allocation plan for our 5 team members (M1 - Nguyễn Hoàng Vinh, M2 - Đinh Thành Trung, M3 - Trần Công Tiến, M4 - Văn Thái Trung, M5 - Trần Thanh Nguyên) to maximize our score."*
- **AI Output & Human Verification**:
  - AI highlighted that **Sheet `Lab2` (NodeJS)** in `barem lab.xlsx` grades **20% CSV usage** and **10% Code Coverage** (even though not explicitly stated in the text prompt), and **Sheet `Lab4`** grades **10% AI usage**. Our team immediately integrated CSV data-driven testing and 100% Jest coverage into Lab 2 to secure the full 100% score.

#### Prompt 2 — Designing 7 Interconnected Java Methods & CSV Datasets for 100% JaCoCo Coverage (Lab 1 - M1: Nguyễn Hoàng Vinh)
- **Prompt Used**:
  > *"Design the Java class `EgaAutocareBillingService` for the EGA Autocare Automotive Parts & Maintenance domain with 7 interconnected methods (where Method 7 chains Methods 1 through 6), each containing at least one `if-else` conditional statement and using primitive/String parameters compatible with JUnit 5 `@CsvFileSource`. Generate 7 `.csv` test files achieving 100% Instruction Coverage and 100% Branch Coverage in JaCoCo."*
- **Human Verification & Refinement (M1 - Nguyễn Hoàng Vinh)**:
  - Executed `mvn clean test` with `jacoco-maven-plugin 0.8.12`: Verified **39/39 test cases Pass**, `0 missed instructions (225/225 = 100%)`, and `0 missed branches (46/46 = 100%)`.

#### Prompt 3 — Logic Review & Eliminating Unreachable Branches in Node.js/Jest (Lab 2 - M2: Đinh Thành Trung)
- **Prompt Used**:
  > *"Review the design of our 4 Node.js & React modules (`ServiceBookingButton`, `MembershipGreetingBanner`, `AutoServiceCard`, `calculateServiceInvoice`) for Lab 2. Act as a QA Lead to identify any business logic flaws, CSV empty-string edge cases, or unreachable branches that would prevent achieving 100% Branch Coverage in Jest."*
- **Human Verification & Refinement (M2 - Đinh Thành Trung)**:
  - Identified and fixed 3 critical issues prior to finalizing the code:
    1. Adjusted voucher minimum order thresholds (`EGAFREE >= 300k` → `50k` off, `EGASUPER >= 500k` → `100k` off, `EGAMEGA >= 1,000,000 VND` → `300k` off) so that a `1,000,000 VND` order with `VIP (20%) + FirstTime (5%) + EGAMEGA (300k) = 550k > 500k` triggers the 50% maximum discount cap branch (`isCapped = true`).
    2. Consolidated `customerTier` validation into a single `if - else if - else` chain to eliminate dead branches.
    3. Added `.trim() !== ''` checks when reading `customerName` and `customAnnouncement` from CSV files.

#### Prompt 4 — Live DOM Inspection & Fixing Selenium CSS Selectors on `ega-autocare.mysapo.net` (Lab 3 - M3: Trần Công Tiến & M4: Văn Thái Trung)
- **Prompt Used**:
  > *"Inspect the live HTML DOM structure of `/account/login`, product detail pages, and `/cart` on `https://ega-autocare.mysapo.net/`. Write 6 ordered JUnit 5 Selenium test methods (`@Order(1)` to `@Order(6)`) covering: Website Access, Valid Login, Invalid Login (unregistered account), Product Search, Buying at least 3 distinct products, and Verifying the exact shopping cart total."*
- **Human Verification & Refinement (M3 - Trần Công Tiến & M4 - Văn Thái Trung)**:
  - During live Chrome execution, the team discovered that generic selector `button[type='submit']` clicked the header search button instead of the login button, and the first `.price` element on product pages was hidden inside an inactive variant node.
  - The team refined the selectors to `#customer_login button.btn-login` and `span.price.text-h4`, selected 3 distinct in-stock products (`/michelin-m2566-bom-lop` `1,180,000 VND` + `/areon-tinh-dau-treo-xe` `285,000 VND` + `/autoguard-bat-phu-xe` `450,000 VND`), and verified via `assertEquals(expectedTotal, actualTotal)` that the cart total equals **`1,915,000 VND`** (`6/6 tests Pass`).

#### Prompt 5 — Authoring the International Standard ISTQB / IEEE 829 Software Test Plan (Lab 4 - M5: Trần Thanh Nguyên)
- **Prompt Used**:
  > *"Based on the verified specifications from Lab 1 (`EgaAutocareBillingService` - 39 CSV test cases), Lab 2 (`Lab2_NodeJS` - 41 Jest test cases & 3 Snapshots), and Lab 3 (`EgaAutocareTest` - 6 Selenium E2E scenarios on `ega-autocare.mysapo.net`), draft a comprehensive ISTQB Software Test Plan covering all IEEE 829 sections (Test Plan Identifier, Introduction, Test Items, Features to be Tested, Features NOT to be Tested, Approach/Strategy, Pass/Fail Criteria, Suspension/Resumption Criteria, Test Deliverables, Staffing & Schedule, Environment, Risks) in English."*
- **Human Verification & Refinement (M5 - Trần Thanh Nguyên)**:
  - Cross-checked all Test Case IDs (`UT_L1_01` → `07`, `UT_L2_...`, `ST_L3_001` → `006`), coverage numbers, member names, and deliverable paths to ensure 100% alignment with the repository.

#### Prompt 6 — Risk-Based Testing Matrix & Contingency Planning per ISTQB CTFL v4.0 (Lab 4 - M5: Trần Thanh Nguyên)
- **Prompt Used**:
  > *"Following the Risk-Based Testing methodology in ISTQB CTFL v4.0, analyze the technical/product risks and project management risks across our 3 automated testing suites (JUnit 5 CSV, Node.js Jest Coverage, Selenium E2E on Sapo Web), evaluate Likelihood and Impact, and propose concrete Mitigation Strategies and Contingency Plans."*
- **Human Verification & Refinement (M5 - Trần Thanh Nguyên)**:
  - Standardized **7 risk items (`R-01` → `R-07`)** in Section 12 of the Test Plan, directly reflecting real engineering challenges encountered and resolved during test execution (such as resetting `/cart/clear`, handling CSV whitespace, and scoping Sapo CSS selectors).

---

### Table 3 — Quality Control & AI Governance Principles in Software Testing

1. **Zero Blind-Trust Verification**: Every test script, CSV dataset, and CSS selector suggested by AI was compiled and executed against real testing engines (`mvn clean test`, `npx jest --coverage`, and live Google Chrome).
2. **Data Privacy & Safe Execution**: Only mock datasets and a dedicated test account (`linhcampochia@gmail.com`) were used, stopping prior to checkout submission on the live storefront.
3. **Bidirectional Traceability**: 100% of features in the Test Plan (`FT-01` → `FT-12`) trace directly to automated `@Test` methods and Excel Test Case sheets (`TestCase_Lab1/2/3.xls`).
