import {Address} from "../Address/Address";
import React from "react";
import {Button, Card, CardActions, CardContent, Typography} from "@mui/material";
import {DeleteOutlined} from "@mui/icons-material";
import Grid from "@mui/material/Unstable_Grid2";

export interface BasketProps {
    basket: Address[];
    removeAction: (address: Address) => void;
}

function basketToCards(basket: Address[], removeAction: (address: Address) => void) {
    return basket.map(address => {
        return (
            <Grid xs={4} md={3} lg={2} key={address.id}>
                <Card>
                    <CardContent>
                        <Typography component="h3"> {address.firstName} - {address.lastName}</Typography>
                        <Typography variant={"subtitle2"}>  {address.city} </Typography>
                    </CardContent>
                    <CardActions>
                        <Button variant="outlined"
                                startIcon={<DeleteOutlined/>}
                                onClick={e => removeAction(address)}>
                            Supprimer
                        </Button>
                    </CardActions>
                </Card>
            </Grid>
        )
    });
}

function Basket({basket, removeAction}: BasketProps) {
    return (<>
        <Typography variant="h2">Panier ({basket.length} éléments)</Typography>
        <Grid container spacing={2}>
            {basketToCards(basket, removeAction)}
        </Grid>
    </>);
}

export default Basket;
