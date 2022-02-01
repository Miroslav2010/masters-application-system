import React from 'react';
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Home} from "./Home";
import {ThemeProvider} from "@mui/material";
import {muiTheme} from "./Theme";
import ValidationPage from "./components/ValidationPage";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import MasterTopic from "./components/master/MasterTopic";

function App() {
    return (
        <ThemeProvider theme={muiTheme}>
            <Router>
                <Routes>
                    <Route path={"/"} element={<Home/>}/>
                    <Route path={"/login"} element={<Login/>}/>
                    <Route path={"/register"} element={<Register/>}/>
                    <Route path={"/masterTopic"} element={<MasterTopic/>}/>
                    <Route path={"/validation/:id"} element={<ValidationPage />}/>
                </Routes>
            </Router>
        </ThemeProvider>
    );
}

export default App;
