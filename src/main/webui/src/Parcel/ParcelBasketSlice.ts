import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {Address} from "../Address/Address";
import {RootState} from "../app/store";

interface parcelBasketState {
    basket: Address[]
}

const initialState: parcelBasketState = {
    basket: []
}

export const parcelBasketSlice = createSlice({
    name: 'parcelBasket',
    initialState,
    reducers: {
        addParcel: (state, action: PayloadAction<Address>) => {
            state.basket.push(action.payload)
        },
        removeParcel: (state, action: PayloadAction<Address>) => {
            state.basket = state.basket.filter ((element) => {
                return element !== action.payload
            })
        }
    }
})

export const {addParcel,removeParcel} = parcelBasketSlice.actions
export const selectBasket = (state: RootState) => state.parcelBasket.basket

export default parcelBasketSlice.reducer
