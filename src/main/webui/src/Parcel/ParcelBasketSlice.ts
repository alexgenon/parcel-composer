import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {Address} from "../Address/Address";
import {RootState} from "../app/store";

interface ParcelBasketState {
    basket: Address[] // Have to use an Array instead of a Set to benefit from functions like map, filter, ... 🤷‍♀️
}

const initialState: ParcelBasketState = {
    basket: []
}

export const parcelBasketSlice = createSlice({
    name: 'parcelBasket',
    initialState,
    reducers: {
        addParcel: (state:ParcelBasketState, action: PayloadAction<Address>) => {
            state.basket.push(action.payload);
        },
        removeParcel: (state:ParcelBasketState, action: PayloadAction<Address>) => {
            state.basket = state.basket.filter((it) => {return (it.id !== action.payload.id)});
        },
        resetBasket:(state:ParcelBasketState) => {
            state.basket = [];
        }
    }
})

export const {addParcel,removeParcel,resetBasket} = parcelBasketSlice.actions
export const selectBasket = (state: RootState) => state.parcelBasket.basket

export default parcelBasketSlice.reducer
