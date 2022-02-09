import React, {FC} from "react";
import AppBar from '@mui/material/AppBar';
import {Button, Container, Toolbar, Typography} from "@mui/material";
import logo from './images/logo.png';
import {createStyles, makeStyles} from "@mui/styles";
import Authentication from "./components/auth/Authentication";
import {useNavigate} from "react-router-dom";

const useStyles = makeStyles(() =>
    createStyles({
        root: {
            flexGrow: 1
        },
        title: {
            flexGrow: 1
        },
        logo: {
            maxWidth: 250,
            marginRight: '10px'
        }
    })
);

export const PageLayout: FC = ({children}) => {
    const classes = useStyles();
    const navigate = useNavigate();

    return (
        <React.Fragment>
            <AppBar position="static">
                <Container>
                    <Toolbar>
                        <Button color="inherit" sx={{marginRight: '30px'}}>
                            <img src={logo} alt="Kitty Katty!" className={classes.logo} onClick={() => navigate("/")}/>
                        </Button>

                        <Typography variant="h6" className={classes.title}>
                            <Button color="inherit" onClick={() => navigate("/masters")}>Магистерски трудови</Button>
                        </Typography>

                        <Authentication/>
                    </Toolbar>
                </Container>
            </AppBar>
            <Container>
                {children}
            </Container>
        </React.Fragment>
    );
}