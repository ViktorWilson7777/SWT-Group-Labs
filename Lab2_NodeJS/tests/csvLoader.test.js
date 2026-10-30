const path = require('path');
const fs = require('fs');
const { loadCsvData, parseCsvLine } = require('../src/utils/csvLoader');

describe('Lab 2 - Helper Unit Test: CSV Loader (csvLoader)', () => {
  const tempCsvPath = path.resolve(__dirname, 'fixtures/temp_test.csv');

  afterEach(() => {
    if (fs.existsSync(tempCsvPath)) {
      fs.unlinkSync(tempCsvPath);
    }
  });

  test('successfully loads CSV file with valid headers and data', () => {
    const csvPath = path.resolve(__dirname, 'fixtures/discount_test_data.csv');
    const data = loadCsvData(csvPath);

    expect(Array.isArray(data)).toBe(true);
    expect(data.length).toBeGreaterThan(0);
    expect(data[0]).toHaveProperty('testId');
    expect(data[0]).toHaveProperty('customerTier');
  });

  test('throws descriptive error if CSV file does not exist', () => {
    expect(() => loadCsvData('non_existent_folder/missing.csv')).toThrow('CSV file not found at path');
  });

  test('returns empty array if CSV has less than 2 valid rows', () => {
    fs.writeFileSync(tempCsvPath, 'header1,header2\n');
    const result = loadCsvData(tempCsvPath);
    expect(result).toEqual([]);
  });

  test('parses lines with comments, empty lines, and double quotes', () => {
    const content = `
# This is a comment
name,description,amount
"Item, A","Description with, comma",100
Item B,Simple description,200
Item C
`;
    fs.writeFileSync(tempCsvPath, content);
    const result = loadCsvData(tempCsvPath);

    expect(result).toHaveLength(3);
    expect(result[0].name).toBe('Item, A');
    expect(result[0].description).toBe('Description with, comma');
    expect(result[0].amount).toBe('100');

    expect(result[1].name).toBe('Item B');
    expect(result[1].amount).toBe('200');

    expect(result[2].name).toBe('Item C');
    expect(result[2].description).toBe('');
    expect(result[2].amount).toBe('');
  });

  test('parseCsvLine properly trims values and handles quotes', () => {
    const line = ' "Alpha" , Beta , "Gamma, Delta" ';
    const tokens = parseCsvLine(line);
    expect(tokens).toEqual(['Alpha', 'Beta', 'Gamma, Delta']);
  });
});
