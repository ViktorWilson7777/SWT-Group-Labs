const path = require('path');
const { loadCsvData } = require('../src/utils/csvLoader');

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

  test('throws descriptive error if CSV file does not exist', () => {
    expect(() => loadCsvData('non_existent_folder/missing.csv')).toThrow('CSV file not found at path');
  });
});
