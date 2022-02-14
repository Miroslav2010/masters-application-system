import React, {FC} from "react";
import AppBar from '@mui/material/AppBar';
import {Box, Button, Container, Grid, Link, Toolbar, Typography} from "@mui/material";
import logo from './images/logo.png';
import {createStyles, makeStyles} from "@mui/styles";
import Authentication from "./components/auth/Authentication";
import {useNavigate} from "react-router-dom";
import masterService from "./service/masterService";
import personService from "./service/personService";

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

    // const isLoggedIn = () => {
    //     return personService.getLoggedInUser();
    // }

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
            <footer style={{position: 'absolute',
                bottom: '0',
                width: '99.3%',
                height: '2rem'}}>
                <Box
                    px={{ xs: 3, sm: 10 }}
                    py={{ xs: 3, sm: 5 }}
                    bgcolor="text.secondary"
                    color="white"
                >
                    <Container maxWidth="lg">
                        <Box textAlign="center">
                            Finki UKIM MK &reg; {new Date().getFullYear()}
                        </Box>
                    </Container>
                </Box>
            </footer>
        </React.Fragment>
    );
}