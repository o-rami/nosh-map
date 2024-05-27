/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx,html}",
    "./node_modules/tw-elements/dist/js/**/*.js"
  ],
  theme: {
    colors: {
      primary: '#ea580c',
      orange: '#fb923c',
      slate: '#cbd5e1',
      stone: '#d6d3d1',
      lime: '#65a30d',
      black: 'rgb(0 0 0)',
      amber: '#F59E0B',
      white: '#FAFAFA',
      green: '#BEF264', 
    },
    screens: {
      'sm': '640px',
      'md': '768px',
      'lg': '1024px',
      'xl': '1280px',
      '2xl': '1536px',
    },
    extend: {
      keyframes: {
        animate: {
          '0%,10%,100%':{
            width: '0%'
          },
          '70%,80%,90%': {
            width: '100%'
          },
        },
      }, 
      animation: {
        animate: 'animate 6s linear infinite',
      },
      spacing: {
        sm: '8px',
        md: '12px',
        lg: '16px',
        xl: '24px',
      },
      borderWidth: {
        'r': '4px',
      }
    }, 
  },
  plugins: [require('tailwindcss')],
  darkMode: "class",
};
