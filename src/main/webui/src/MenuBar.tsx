import {ApplicationScreens} from "./App";
import {AppBar, Box, Button, Toolbar, Typography} from "@mui/material";
import {LocalShipping} from "@mui/icons-material";
import React from "react";
import {Link} from "react-router-dom";

interface MenuBarProps {
    screens: ApplicationScreens[]
    loggedInUser?: string
    loginCallback: () => void
    logoutCallback: () => void
}

function MenuBar({screens,loggedInUser,loginCallback,logoutCallback}: MenuBarProps) {
    const menuBarStyle = {
        textDecoration: "none",
        underline: "none",
        color: "white",
        fontSize: "20px",
        marginLeft: 2,
        "&:hover": {
            color: "yellow",
            borderBottom: "1px solid white",
        }
    }
    return (<Box sx={{flexGrow: 1}}>
        <nav>
            <AppBar position="static" component="nav">
                <Toolbar>
                    <LocalShipping/>
                    <Typography
                        variant="h5"
                        noWrap
                        component="a"
                        href=""
                        sx={{
                            mr: 1,
                            display: 'flex',
                            fontFamily: 'monospace',
                            fontWeight: 500,
                            letterSpacing: '.2rem',
                            color: 'inherit',
                            textDecoration: 'none',
                        }}
                    >
                        Parcel Composer
                    </Typography>
                    <Box sx={{flexGrow: 1, display: "flex"}}>
                        {
                            screens.map(screen => {
                                return (
                                    <Link to={screen.path} key={screen.name}>
                                        <Button sx={menuBarStyle}
                                        >
                                            {screen.name}
                                        </Button>
                                    </Link>);
                            })
                        }
                    </Box>
                    <Box><Button sx={menuBarStyle} onClick={loggedInUser?logoutCallback:loginCallback}> {loggedInUser??"Click to login"}</Button></Box>
                </Toolbar>
            </AppBar>
        </nav>
    </Box>);
}

export default MenuBar;
