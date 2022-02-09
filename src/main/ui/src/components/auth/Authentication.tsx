import React, {Props} from "react";
import {Button} from "@mui/material";
import {User} from "../../domain/User";
import {useNavigate} from "react-router-dom";
import authService from "../../service/authService";


const Authentication = () => {

    let navigate = useNavigate();
    const onRegister = () => {
        let path = `/register`;
        navigate(path);
    }
    const onLogin = () => {
        let path = `/login`;
        navigate(path);
    }

    const onLogout = () => {
        authService.logout()
        let path = `/`;
        navigate(path);
    }

    let userString = localStorage.getItem("user")
    if (userString != null) {
        try {
            let user: User = JSON.parse(userString)
            return (
                <>
                    <Button color="inherit">{user.fullName}</Button>
                    <Button color="inherit" onClick={onLogout}>Logout</Button>
                </>
            )
        } catch (err: any) {
            localStorage.clear()
        }
    }
    return (
        <>
            <Button color="inherit" onClick={onRegister}>Register</Button>
            <Button color="inherit" onClick={onLogin}>Login</Button>
        </>
    )
}

export default Authentication
