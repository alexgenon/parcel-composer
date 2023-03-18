import React, {useState} from 'react';
import {
    Button,
    Dialog, DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import Grid from '@mui/material/Unstable_Grid2'
import {Address, AddressBuilder} from "../Address/Address";
import {AddressEditor} from "../Address/AddressEditor";
import {useAppDispatch, useAppSelector} from "../app/hook";
import {addParcel, removeParcel, resetBasket, selectBasket} from "./ParcelBasketSlice";
import Basket from "./Basket";
import SearchResults from "./SearchResults";
import {useGetAllAddressesQuery, useAddNewAddressMutation, useDeleteAddressMutation} from "../Address/AddressApi";
import {ToBpostCSV} from "../Adapters/ToBpostCSV";
import {useGetSenderHeaderQuery} from "../Adapters/BpostApi";


function ParcelComposer() {
    const [inputStringAddress, setInputStringAddress] = useState("");
    const [candidateAddresses, setCandidateAddresses] = useState<Address[]>([]);
    const [newAddress, setNewAddress] = useState<AddressBuilder | undefined>(undefined);
    const [addressToRemove, setAddressToRemove] = useState<Address | undefined>(undefined);
    const parcelBasket: Address[] = useAppSelector(selectBasket);
    const {data: addressBookData, error: fetchAddressBookError} = useGetAllAddressesQuery();
    let addressBook: Address[] = addressBookData?(addressBookData as Address[]):[];
    const [addNewAddress] = useAddNewAddressMutation();
    const [deleteAddress] = useDeleteAddressMutation();
    const {data: senderHeader} = useGetSenderHeaderQuery();
    const dispatch = useAppDispatch();

    if(fetchAddressBookError){
        console.error(JSON.stringify(fetchAddressBookError));
    }

    function findCandidateAddresses(searchString: string) {
        if (searchString.length >= 2) {
            let firstLine = searchString.split(/\r\n|\r|\n/)[0].trim().toLowerCase();
            setCandidateAddresses(addressBook.filter(a => (a.firstName + a.lastName).toLowerCase().search(firstLine) >= 0));
        } else {
            setCandidateAddresses([]);
        }
    }

    const updateInputSearchString = ((searchString: string) => {
        setInputStringAddress(searchString);
        if (newAddress) {
            setNewAddress(undefined);
        }
        findCandidateAddresses(searchString);
    })

    const removeFromBasketAction = ((address: Address) => {
        dispatch(removeParcel(address));
    })

    const commitBasket = (() => {
        let exportBlob = ToBpostCSV.exportToBPostCSV(parcelBasket,senderHeader);
        initiateDownload(exportBlob);
        dispatch(resetBasket());
    });

    return (<>
            <Grid container alignItems="flex-start" spacing={2} padding={3}>
                <Grid xs={12}><Typography variant="h2">Adresse</Typography></Grid>
                <Grid xs={4}>
                    <Stack spacing={1}>
                        <TextField variant="standard"
                                   multiline
                                   fullWidth={true}
                                   rows={5}
                                   value={inputStringAddress}
                                   onChange={e => updateInputSearchString(e.target.value)}
                                   label="Addresse complète"/>
                        <Button variant="outlined"
                                onClick={_ => setNewAddress(AddressBuilder.fromString(inputStringAddress))}
                        >
                            Extraire Addresse
                        </Button>
                    </Stack>
                </Grid>
                <Grid xs={8}>
                    {
                        newAddress ? <AddressEditor address={newAddress}
                                                    buttonText="Sauver et ajouter"
                                                    buttonAction={(a:AddressBuilder) => {
                                                        setInputStringAddress('');
                                                        let finalAddress = a.build();
                                                        dispatch(addParcel(finalAddress));
                                                        addNewAddress(finalAddress);
                                                    }}
                            />
                            : <SearchResults candidateAddresses={candidateAddresses}
                                             addressClicked={address => {
                                                 updateInputSearchString('');
                                                 dispatch(addParcel(address));
                                             }}
                                             removeAddress={address => {
                                                 setAddressToRemove(address);
                                             }}
                            />
                    }
                </Grid>
                <Grid xs={12}>
                    <Basket
                        basket={parcelBasket}
                        removeAction={removeFromBasketAction}
                    />
                </Grid>
                <Grid xs={4}>
                    <Button variant="outlined"
                            onClick={commitBasket}
                    >
                        Finaliser les colis
                    </Button>
                </Grid>
            </Grid>
            <ConfirmRemovalDialog
                address={addressToRemove}
                processResponse={confirmed => {
                    if (confirmed) {
                        let finalAddress = addressToRemove as Address
                        deleteAddress(finalAddress.businessId);
                        setCandidateAddresses(candidateAddresses.filter(it => it.businessId !== addressToRemove?.businessId))
                    }
                    setAddressToRemove(undefined)
                }}
            />
        </>
    )
}

interface ConfirmRemovalDialogProps {
    address: Address | undefined
    processResponse: (removalConfirmed: boolean) => void
}

function ConfirmRemovalDialog({address, processResponse}: ConfirmRemovalDialogProps) {
    let display: boolean = (address !== undefined);
    const handleCancel = () => {
        processResponse(false);
    };
    const handleConfirm = () => {
        processResponse(true);
    }

    return (
        <Dialog open={display}
                onClose={handleCancel}
                aria-describedby="removal"
                aria-labelledby="confirm-address-removal-title"
        >
            <DialogTitle id="confirm-address-removal-title">Confirmer Suppression</DialogTitle>
            <DialogContent>
                <DialogContentText id="confirm-address-removal">
                    Supprimer l'adresse {address?.firstName} {address?.lastName}
                </DialogContentText>
                <DialogActions>
                    <Button onClick={handleCancel}>Annuler</Button>
                    <Button onClick={handleConfirm}>Confirmer</Button>
                </DialogActions>
            </DialogContent>
        </Dialog>)
        ;
}

export default ParcelComposer

/*
    Source of inspiration: https://www.geeksforgeeks.org/how-to-download-pdf-file-in-reactjs/
 */
function initiateDownload(exportBlob: Blob){
    const fileURL = URL.createObjectURL(exportBlob);
    let alink = document.createElement('a');
    let n=new Date();
    let fileSuffix = `${n.getFullYear()}_${to2chars(n.getMonth())}_${to2chars(n.getDay())}_${to2chars(n.getHours())}_${to2chars(n.getMinutes())}`
    alink.href = fileURL;
    alink.download = `colis_bpost_${fileSuffix}.csv`;
    alink.click();
    alink.remove();
    URL.revokeObjectURL(fileURL);
}

function to2chars(num: number):string {
    return num.toString().padStart(2,'0');
}
