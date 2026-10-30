const fs = require('fs');
const path = require('path');

/**
 * Parses a simple CSV file into an array of objects based on header row.
 * Handles quoted strings with commas and trims whitespace.
 *
 * @param {string} relativeFilePath - Path relative to project root or absolute path
 * @returns {Array<Object>} Array of row objects
 */
function loadCsvData(relativeFilePath) {
  const absolutePath = path.isAbsolute(relativeFilePath)
    ? relativeFilePath
    : path.resolve(__dirname, '../../', relativeFilePath);

  if (!fs.existsSync(absolutePath)) {
    throw new Error(`CSV file not found at path: ${absolutePath}`);
  }

  const content = fs.readFileSync(absolutePath, 'utf-8');
  const lines = content
    .split(/\r?\n/)
    .map(line => line.trim())
    .filter(line => line.length > 0 && !line.startsWith('#'));

  if (lines.length < 2) {
    return [];
  }

  const headers = parseCsvLine(lines[0]);
  const rows = [];

  for (let i = 1; i < lines.length; i++) {
    const values = parseCsvLine(lines[i]);
    const rowObj = {};
    headers.forEach((header, index) => {
      rowObj[header] = values[index] !== undefined ? values[index] : '';
    });
    rows.push(rowObj);
  }

  return rows;
}

/**
 * Splits a single CSV line into tokens, respecting double-quoted values.
 *
 * @param {string} line
 * @returns {string[]}
 */
function parseCsvLine(line) {
  const result = [];
  let current = '';
  let inQuotes = false;

  for (let i = 0; i < line.length; i++) {
    const char = line[i];
    if (char === '"') {
      inQuotes = !inQuotes;
    } else if (char === ',' && !inQuotes) {
      result.push(current.trim());
      current = '';
    } else {
      current += char;
    }
  }
  result.push(current.trim());
  return result;
}

module.exports = {
  loadCsvData,
  parseCsvLine
};
