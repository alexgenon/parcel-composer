import {configureStore} from '@reduxjs/toolkit'
import parcelBasketReducer from "../Parcel/ParcelBasketSlice";
import {parcelComposerApi} from "./parcelComposerApi";

export const store = configureStore({
   reducer: {
       parcelBasket: parcelBasketReducer,
       [parcelComposerApi.reducerPath]: parcelComposerApi.reducer
   },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat([parcelComposerApi.middleware]),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
