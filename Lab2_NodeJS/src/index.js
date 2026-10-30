const { calculateServiceInvoice } = require('./services/discountService');
const { loadCsvData, parseCsvLine } = require('./utils/csvLoader');

module.exports = {
  calculateServiceInvoice,
  loadCsvData,
  parseCsvLine
};
