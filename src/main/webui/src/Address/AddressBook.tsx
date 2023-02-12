import {useGetAllAddressesQuery} from "./AddressApi";
import {Address} from "./Address";

function AddressBook() {

    const {data, error, isLoading} = useGetAllAddressesQuery();
    if (isLoading) {
        return (<div>"Loading addresses"</div>);
    } else if (data) {
        let addresses: Address[] = data as Address[];
        return (<ul>
            {addresses.map(address =>
                <li key={address.businessId}>
                    {address.firstName}
                </li>)}
        </ul>);
    } else {
        return (<div> {"Error" + JSON.stringify(error)}</div>);
    }
}

export default AddressBook;
