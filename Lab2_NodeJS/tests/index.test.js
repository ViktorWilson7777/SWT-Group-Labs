const indexExports = require('../src/index');

describe('Lab 2 - Entry Point Index Export Test (src/index.js)', () => {
  test('correctly exports core services and utilities', () => {
    expect(typeof indexExports.calculateServiceInvoice).toBe('function');
    expect(typeof indexExports.loadCsvData).toBe('function');
    expect(typeof indexExports.parseCsvLine).toBe('function');
  });
});

