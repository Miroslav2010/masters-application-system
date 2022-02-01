import React from 'react';
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Home} from "./Home";
import {ThemeProvider} from "@mui/material";
import {muiTheme} from "./Theme";
import ValidationPage from "./components/ValidationPage";

function App() {
  return (
      <ThemeProvider theme={muiTheme}>
        <Router>
          <Routes>
            <Route path={"/"} element={<Home />}/>
              <Route path={"/validation/:id"} element={<ValidationPage />}/>
          </Routes>
        </Router>
      </ThemeProvider>
  );
}

export default App;
