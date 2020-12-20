const defaultTheme = require('tailwindcss/defaultTheme')
module.exports = {
  future: {
    removeDeprecatedGapUtilities: true,
  },
  theme: {
  },
  variants: {
      extend: {
        backgroundColor: ['active'],
      }
    }
};