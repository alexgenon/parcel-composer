import React, {useState} from 'react';
import {Button, Stack, TextField} from "@mui/material";
import Grid from '@mui/material/Unstable_Grid2'
import {Address} from "../Address/Address";
import {AddressEditor} from "../Address/AddressEditor";
import {useAppDispatch, useAppSelector} from "../app/hook";
import {addParcel} from "./ParcelBasketSlice";

interface SearchResultsProps {
    candidateAddresses: Address[]
    addressClicked: (address: Address) => void
}

function SearchResults({candidateAddresses, addressClicked}: SearchResultsProps) {
    if (candidateAddresses.length > 0) {
        return (
            <ul>
                {candidateAddresses.map(a => <li key={a.lastName}>{a.firstName} - {a.lastName}</li>)}
            </ul>
        )
    } else {
        return (<div><em>Pas d'addresse trouvée</em></div>);
    }
}

function getDummyAddresses(searchString: string): Address[] {
    if (searchString) {
        return Array.from(Array(3).keys())
            .map(i => {
                let a = new Address()
                a.firstName = searchString;
                a.lastName = i.toString();
                return a;
            });
    } else return [];
}

function extractAddress(fullAddress: string) {
    return Address.fromString(fullAddress);
}


function ParcelComposer() {
    const [inputAddress, setInputAddress] = useState("");
    const [candidateAddresses, setCandidateAddresses] = useState<Address[]>([]);
    const [newAddress, setNewAddress] = useState<Address | undefined>(undefined);
    const parcelBasket = useAppSelector(state => state.parcelBasket.basket);
    const dispatch = useAppDispatch();

    const updateInputAddress = ((searchString: string) => {
        setInputAddress(searchString);
        if (newAddress)
            setNewAddress(undefined);
        setCandidateAddresses(getDummyAddresses(searchString));
    })

    return (
        <Grid container alignItems="flex-start" spacing={2} padding={3}>
            <Grid xs={4}>
                <Stack spacing={1}>
                    <TextField variant="standard"
                               multiline
                               fullWidth={true}
                               rows={5}
                               value={inputAddress}
                               onChange={e => updateInputAddress(e.target.value)}
                               label="Addresse complète"/>
                    <Button variant="outlined"
                            onClick={_ => setNewAddress(extractAddress(inputAddress))}
                    >
                        Extraire Addresse
                    </Button>
                </Stack>
            </Grid>
            <Grid xs={8}>
                {
                    newAddress ? <AddressEditor address={newAddress}
                                                buttonText="Sauver et ajouter"
                                                buttonAction={a => dispatch(addParcel(a))}
                        />
                        : <SearchResults candidateAddresses={candidateAddresses}
                                         addressClicked={a => console.log("TODO")}
                        />
                }
            </Grid>
            <Grid xs={12}>
                <ul>
                    {
                        parcelBasket.map(it => <li>{it.firstName}</li>)
                    }
                </ul>
            </Grid>
        </Grid>
    )
}

export default ParcelComposer
