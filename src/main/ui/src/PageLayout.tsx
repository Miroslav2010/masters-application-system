import React, {FC} from "react";
import AppBar from '@mui/material/AppBar';
import {Button, Container, Toolbar, Typography} from "@mui/material";
import logo from './images/logo.png';
import {createStyles, makeStyles} from "@mui/styles";

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

    return (
        <React.Fragment>
            <AppBar position="static">
                <Container>
                    <Toolbar>
                        <img src={logo} alt="Kitty Katty!" className={classes.logo} />

                        <Typography variant="h6" className={classes.title}>
                            Ovde ke stojat menu items
                        </Typography>

                        <Button color="inherit">Login</Button>
                    </Toolbar>
                </Container>
            </AppBar>
            <Container>
                {children}
            </Container>
        </React.Fragment>
    );
}