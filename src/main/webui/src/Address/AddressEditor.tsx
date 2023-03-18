import React, {useState} from 'react';
import {Button, Stack, TextField} from "@mui/material";
import {Address, AddressBuilder} from "./Address";
import Grid from "@mui/material/Unstable_Grid2";

export interface AddressEditorProps {
    address: AddressBuilder;
    buttonText: String;
    buttonAction: (address:AddressBuilder) => void;
}

export function AddressEditor({address, buttonText,buttonAction}: AddressEditorProps) {
    const [updatedAddress, setUpdatedAddress] = useState<AddressBuilder>(address)
    const updateAddress = (fieldUpdater: (a:AddressBuilder) => void) => {
        let newAddress:Address= JSON.parse(JSON.stringify(updatedAddress)) ;
        let newBuilder = new AddressBuilder(newAddress);
        fieldUpdater(newBuilder);
        setUpdatedAddress(newBuilder);
    }

    return (
        <Stack spacing={1}>
            <Grid container spacing={1}>
                <Grid xs={6}> <TextField fullWidth={true} id="firstName"
                                         value={updatedAddress.firstName}
                                         label="Prénom"
                                         onChange={ e => {
                                            updateAddress(a => {
                                                a.firstName = e.target.value;
                                            })
                                         }}
                /></Grid>
                <Grid xs={6}> <TextField fullWidth={true} id="lastName"
                                         value={updatedAddress.lastName}
                                         label="Nom"
                                         onChange={ e => {
                                             updateAddress(a => {
                                                 a.lastName = e.target.value;
                                             })
                                         }}
                /> </Grid>
            </Grid>
            <Grid container spacing={1}>
                <Grid xs={8}> <TextField fullWidth={true} id="street"
                                          value={updatedAddress.street}
                                          label="Rue"
                                          onChange={ e => {
                                              updateAddress(a => {
                                                  a.street = e.target.value;
                                              })
                                          }}
                /> </Grid>
                <Grid xs={2}> <TextField fullWidth={true} id="streetNb"
                                         value={updatedAddress.streetNb}
                                         label="Numéro"
                                         onChange={ e => {
                                             updateAddress(a => {
                                                 a.streetNb = e.target.value;
                                             })
                                         }}
                /> </Grid>
                <Grid xs={2}> <TextField fullWidth={true} id="postBoxLetter"
                                         value={updatedAddress.postboxLetter}
                                         label="Boîte"
                                         onChange={ e => {
                                             updateAddress(a => {
                                                 a.postboxLetter = e.target.value;
                                             })
                                         }}
                /> </Grid>
            </Grid>
            <Grid container spacing={1}>
                <Grid xs={2}> <TextField fullWidth={true} id="postCode"
                                         value={updatedAddress.postcode}
                                         label="Code postal"
                                         onChange={ e => {
                                             updateAddress(a => {
                                                 a.postcode = Number.parseInt(e.target.value);
                                             })
                                         }}
                /> </Grid>
                <Grid xs={10}> <TextField fullWidth={true} id="city"
                                          value={updatedAddress.city}
                                          label="Ville"
                                          onChange={ e => {
                                              updateAddress(a => {
                                                  a.city = e.target.value;
                                              })
                                          }}
                /> </Grid>
            </Grid>
            <Grid container spacing={1}>
                <Grid xs={12}> <TextField fullWidth={true} id="email"
                                          value={updatedAddress.email}
                                          label="email"
                                          onChange={ e => {
                                              updateAddress(a => {
                                                  a.email = e.target.value;
                                              })
                                          }}
                /></Grid>
            </Grid>
            <Grid container spacing={1}>
                <Button variant="outlined"
                        onClick={_ => buttonAction(updatedAddress)}
                >
                    {buttonText}
                </Button>
            </Grid>
        </Stack>
    );
}

