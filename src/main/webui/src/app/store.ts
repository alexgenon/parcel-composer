import {configureStore} from '@reduxjs/toolkit'
import parcelBasketReducer from "../Parcel/ParcelBasketSlice";
import adressBookReducer from "../Address/AdressBookSlice";

export const store = configureStore({
   reducer: {
       parcelBasket: parcelBasketReducer,
       addressBook: adressBookReducer
   }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
