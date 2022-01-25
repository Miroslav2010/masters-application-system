import {grey} from "@mui/material/colors";
import {createTheme} from "@mui/material/styles";

const palette = {
    primary: {
        main: '#000'
    },
    secondary: {
        main: grey[800]
    },
    lightGrey: {
        main: grey[400]
    }
};

export const muiTheme = createTheme({
   palette,
   components: {
       MuiFormLabel: {
           styleOverrides: {
               root: {
                   userSelect: 'none'
               }
           }
       },
       MuiFormControlLabel: {
           styleOverrides: {
               root: {
                   userSelect: 'none'
               }
           }
       }
   }
});