import {ApplicationScreens} from "./App";
import {AppBar, Box, Button, makeStyles, Toolbar, Typography} from "@mui/material";
import {LocalShipping} from "@mui/icons-material";
import React from "react";
import {Link} from "react-router-dom";

interface MenuBarProps {
    screens: ApplicationScreens[]
}

function MenuBar({screens}: MenuBarProps) {
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
                            mr: 2,
                            display: 'flex',
                            fontFamily: 'monospace',
                            fontWeight: 700,
                            letterSpacing: '.2rem',
                            color: 'inherit',
                            textDecoration: 'none',
                        }}
                    >
                        BPost Parcel Composer
                    </Typography>
                    <Box sx={{flexGrow: 1, display: "flex"}}>
                        {
                            screens.map(screen => {
                                return (
                                    <Link to={screen.path}>
                                        <Button sx={{
                                            textDecoration: "none",
                                            color: "white",
                                            fontSize: "20px",
                                            marginLeft: 2,
                                            "&:hover": {
                                                color: "yellow",
                                                borderBottom: "1px solid white",
                                            }
                                        }}
                                        >
                                            {screen.name}
                                        </Button>
                                    </Link>);
                            })
                        }
                    </Box>
                </Toolbar>
            </AppBar>
        </nav>
    </Box>);
}

export default MenuBar;
