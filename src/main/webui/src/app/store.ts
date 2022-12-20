import {configureStore} from '@reduxjs/toolkit'
import parcelBasketReducer from "../Parcel/ParcelBasketSlice";

export const store = configureStore({
   reducer: {
       parcelBasket: parcelBasketReducer
   }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
