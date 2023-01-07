import {configureStore} from '@reduxjs/toolkit'
import parcelBasketReducer from "../Parcel/ParcelBasketSlice";
import {addressApi} from "../Address/AddressApi";

export const store = configureStore({
   reducer: {
       parcelBasket: parcelBasketReducer,
       [addressApi.reducerPath]: addressApi.reducer
   },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat(addressApi.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
