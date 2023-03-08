import {Address} from "../Address/Address";
import {Typography} from "@mui/material";

interface AddressListProps {
    addresses: Address[]
}

function AddressList({addresses}: AddressListProps) {
    return (
        <>
            <Typography variant="h2">Carnet d'addresses</Typography>
            <ul>
                {addresses.map(address => {
                    return (<li>{address.lastName} - {address.firstName} ({address.city})</li>);
                })}
            </ul>
        </>
    );
}

export default AddressList;
