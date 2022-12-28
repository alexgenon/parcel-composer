import {createSlice, PayloadAction} from '@reduxjs/toolkit'
import {Address, someAddresses} from './Address'
import {RootState} from "../app/store";

interface AddressBookState {
    addressBook: Address[] // Have to use an Array instead of a Set to benefit from functions like map, filter, ... 🤷‍♀️
}

const initialState:AddressBookState = {
        //addressBook: []
    addressBook: someAddresses
}

export const addressBookSlice = createSlice ({
    name: 'addressBook',
    initialState,
    reducers: {
        addAddress: (state:AddressBookState, action:PayloadAction<Address>) =>{
            state.addressBook.push(action.payload);
        },
        removeAddress: (state:AddressBookState, action:PayloadAction<Address>) => {
            state.addressBook = state.addressBook.filter((it) => {return (it.id !==action.payload.id)} );
        }
    }
})

export const {addAddress,removeAddress} = addressBookSlice.actions;
export const selectAddressBook = (state:RootState) => state.addressBook.addressBook;

export default addressBookSlice.reducer
