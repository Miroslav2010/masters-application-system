import React from "react";
import authRepository from "../../service/authService";
import roleRepository from "../../service/roleService";
import Select from '@mui/material/Select';

class register extends React.Component {

    onSubmitEvent(event) {
        event.preventDefault()
        authRepository.login(event.target.elements.username.value, event.target.elements.password.value)
    }

    render() {
        return (
            <div>
                <form onSubmit={this.onSubmitEvent}>
                    <label>Username</label>
                    <input name="username" type="text"/>
                    <br/>
                    <label>Password</label>
                    <input name="password" type="text"/>
                    <br/>
                    <input name="index" type="text"/>
                    <br/>
                    <select name="role">
                    </select>
                    <button type="submit">Login</button>
                </form>
            </div>
        )
    }

}