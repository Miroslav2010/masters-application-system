import React from "react";
import {PageLayout} from "../../PageLayout";

class Login extends React.Component{
    Login(){
        this.state = {
            data: "data",

        }
    }
    /*onSubmitEvent(event){
        event.preventDefault()
        authRepository.login(event.target.elements.username.value,event.target.elements.password.value)
    }*/

    render(){
        return (
            <PageLayout>
                <div>
                    <form>
                        <label>Username</label>
                        <input name="username" type="text"/>
                        <br/>
                        <label>Password</label>
                        <input name="password" type="text"/>
                        <br/>
                        <button type="submit">Login</button>
                    </form>
                </div>
            </PageLayout>
        )
    }

}

export default Login
