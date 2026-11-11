const path = require('path');
const { loadCsvData, parseCsvLine } = require('../src/utils/csvLoader');

describe('Lab 2 - Helper Unit Test: CSV Loader (csvLoader)', () => {
  test('successfully loads discount_test_data.csv using relative path', () => {
    const data = loadCsvData('tests/fixtures/discount_test_data.csv');

    expect(Array.isArray(data)).toBe(true);
    expect(data.length).toBeGreaterThan(0);
    expect(data[0]).toHaveProperty('testId');
    expect(data[0]).toHaveProperty('customerTier');
    expect(data[0]).toHaveProperty('baseCost');
  });

  test('successfully loads greeting_test_data.csv using absolute path', () => {
    const absolutePath = path.resolve(__dirname, 'fixtures/greeting_test_data.csv');
    const data = loadCsvData(absolutePath);

    expect(Array.isArray(data)).toBe(true);
    expect(data.length).toBeGreaterThan(0);
    expect(data[0]).toHaveProperty('testId');
    expect(data[0]).toHaveProperty('customerName');
    expect(data[0]).toHaveProperty('tier');
  });

  test('returns empty array when CSV file is empty or contains only comments', () => {
    const emptyPath = path.resolve(__dirname, 'fixtures/empty_test.csv');
    const data = loadCsvData(emptyPath);
    expect(data).toEqual([]);
  });

  test('throws descriptive error if CSV file does not exist', () => {
    expect(() => loadCsvData('non_existent_folder/missing.csv')).toThrow('CSV file not found at path');
  });

  test('parseCsvLine handles RFC 4180 escaped quotes and commas inside quotes', () => {
    const tokens = parseCsvLine('EGA-01,"Gói ""VIP"" Pro, bảo dưỡng trọn gói",1500000');
    expect(tokens).toEqual(['EGA-01', 'Gói "VIP" Pro, bảo dưỡng trọn gói', '1500000']);
  });

  test('parseCsvLine gracefully handles null, undefined, or empty string', () => {
    expect(parseCsvLine('')).toEqual([]);
    expect(parseCsvLine(null)).toEqual([]);
    expect(parseCsvLine(undefined)).toEqual([]);
  });
});
