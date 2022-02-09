import React from "react";
import {PageLayout} from "../../PageLayout";
import {
    Button,
    FormControl, Grid, InputLabel,
    ListItemText,
    MenuItem, OutlinedInput,
    Select,
    SelectChangeEvent, TextField
} from "@mui/material";
import personService from "../../service/personService";
import authService from "../../service/authService";

interface IProps{
}

interface IState {
    username: string;
    password: string;
    index: string;
    role: string;
    allRoles: string[];
    register: string
}

const MenuProps = {
    PaperProps: {
        style: {
            width: 250,
        },
    },
};

class Register extends React.Component<IProps,IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {
            username: "",
            password: "",
            index: "",
            role: "",
            allRoles: [],
            register: ""
        }
    }

    componentDidMount() {
        personService.fetchRoles((data) => {
            this.setState({
                allRoles: data
            })
        })
    }

    componentDidUpdate(prevProps: Readonly<IProps>, prevState: Readonly<IState>, snapshot?: any) {
        if(this.state.register==="SUCCESS"){
            return
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

    setIndex(event: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) {
        this.setState({
            index: event.target.value
        })
    }

    setRoles(event: SelectChangeEvent<string>) {
        const {
            target: {value},
        } = event;
        this.setState({
            role: value
        })
    }

    handleRegister() {
        authService.register(this.state.username, this.state.password, [this.state.role], this.state.index,()=>{this.setState({register:"SUCCESS"})})
    }

    render() {
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
                        <TextField id="username" label="Full Name" variant="outlined" onChange={(value) => {
                            this.setUsername(value)
                        }}/>
                    </Grid>
                    <Grid item xs={4}>
                        <TextField id="password" label="Password" variant="outlined" onChange={(value) => {
                            this.setPassword(value)
                        }}/>
                    </Grid>
                    <Grid item xs={4}>
                        <TextField id="index" label="Index" variant="outlined" onChange={(value) => {
                            this.setIndex(value)
                        }}/>
                    </Grid>
                    <Grid item xs={4}>
                        <FormControl sx={{ m: 1, width: 200 }}>
                            <InputLabel id="roleLabel">Role</InputLabel>
                            <Select id="role" labelId="roleLabel" onChange={(value) => {this.setRoles(value)}} value={this.state.role} label="Role" input={<OutlinedInput label="Role"  />} MenuProps={MenuProps}>
                                {
                                    this.state.allRoles.map((role: string) => {
                                        return (
                                            <MenuItem key={role} value={role}>
                                                <ListItemText primary={role}/>
                                            </MenuItem>
                                        )
                                    })
                                }
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid item xs={4}>
                        <Button onClick={() => {
                            this.handleRegister()
                        }} variant="contained">Register</Button>
                    </Grid>
                </Grid>

            </PageLayout>
        )
    }
}

export default Register