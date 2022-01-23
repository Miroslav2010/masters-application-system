import React from 'react';
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Home} from "./Home";
import {ThemeProvider} from "@mui/material";
import {muiTheme} from "./Theme";

function App() {
  return (
      <ThemeProvider theme={muiTheme}>
        <Router>
          <Routes>
            <Route path={"/"} element={<Home />}/>
          </Routes>
        </Router>
      </ThemeProvider>
  );
}

export default App;
