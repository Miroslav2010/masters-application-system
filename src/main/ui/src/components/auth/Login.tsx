import React, {Props} from "react";
import {PageLayout} from "../../PageLayout";
import {Button, Grid, TextField} from "@mui/material";
import authService from "../../service/authService";

interface IProps{
}

interface IState {
    username: string;
    password: string;
    login: string;
}

class Login extends React.Component<IProps,IState>{

    constructor(props:IProps) {
        super(props);
        this.state={
            username: "",
            password: "",
            login: ""
        }
    }

    setUsername(event: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) {
        this.setState({username: event.target.value})
    }

    setPassword(event: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) {
        this.setState({
            password: event.target.value
        })
    }

    handleLogin(){
        authService.login(this.state.username,this.state.password,()=>{this.setState({login:"SUCCESS"})})
    }

    render(){
        return (
            <PageLayout>
                <Grid
                    container
                    direction="column"
                    justifyContent="center"
                    alignItems="center"
                    marginTop={1}
                    rowSpacing={1}>
                    <Grid item xs={4}>
                        <TextField id="username" name="username" label="Full Name" variant="outlined" onChange={(event)=>{this.setUsername(event)}}/>
                    </Grid>
                    <Grid item xs={4}>
                        <TextField id="password" name="password" label="Password" variant="outlined" onChange={(event)=>{this.setPassword(event)}}/>
                    </Grid>
                    <Grid item xs={4}>
                        <Button variant="contained" onClick={()=>{this.handleLogin()}}>Login</Button>
                    </Grid>
                </Grid>

            </PageLayout>
        )
    }

}

export default Login
