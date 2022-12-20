import {createSlice, PayloadAction} from '@reduxjs/toolkit'
import { Address } from './Address'

interface AddressBookState {
    addressBook: Address[] // Have to use an Array instead of a Set to benefit from functions like map, filter, ... 🤷‍♀️
}

const initialState:AddressBookState = {
        addressBook: []
}

export const addressBookSlice = createSlice ({
    name: 'addressBook',
    initialState,
    reducers: {
        newAddress: (state:AddressBookState, action:PayloadAction<Address>) =>{
            state.addressBook.push(action.payload);
        },
        removeAddress: (state:AddressBookState, action:PayloadAction<Address>) => {
            state.addressBook.filter((it) => {return (it !==action.payload)} );
        },
        updateAddress: (state: AddressBookState, action: PayloadAction<Address>) => {
            state.addressBook = state.addressBook.filter((it)=> {return it.id() !== action.payload.id()})
            state.addressBook.push(action.payload)
        }
    }
})