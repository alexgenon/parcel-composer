import {useGetAllAddressesQuery} from "../Address/AddressApi";
import {Address} from "../Address/Address";
import AddressList from "./AddressList";
import AddressLoader from "./AddressLoader";
import {Divider} from "@mui/material";

function AddressBook() {

    const {data, error, isLoading} = useGetAllAddressesQuery();
    if (isLoading) {
        return (<div>"Loading addresses"</div>);
    } else if (data) {
        let addresses: Address[] = data as Address[];
        return (
            <>
                <AddressLoader/>
                <Divider/>
                <AddressList addresses={addresses}/>
            </>
        );
    } else {
        return (<div> {"Error" + JSON.stringify(error)}</div>);
    }
}

export default AddressBook;
