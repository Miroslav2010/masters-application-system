import React from 'react';
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import {Home} from "./Home";
import {ThemeProvider} from "@mui/material";
import {muiTheme} from "./Theme";
import ValidationPage from "./components/ValidationPage";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import MasterTopic from "./components/master/MasterTopic";
import MasterWrap from "./components/master-list/MasterWrap";
import MasterDetailsWrap from "./components/master-details/MasterDetailsWrap";
import SingleDocumentUpload from "./components/document-upload/SingleDocumentUploadPage";

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
                    <Route path={"/masters"} element={<MasterWrap />}/>
                    <Route path={"/master/:id/details"} element={<MasterDetailsWrap />}/>
                </Routes>
            </Router>
        </ThemeProvider>
    );
}

export default App;
