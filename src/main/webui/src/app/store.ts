import {configureStore} from '@reduxjs/toolkit'
import parcelBasketReducer from "../Parcel/ParcelBasketSlice";
import {addressApi} from "../Address/AddressApi";
import {bpostApi} from "../Adapters/BpostApi";

export const store = configureStore({
   reducer: {
       parcelBasket: parcelBasketReducer,
       [addressApi.reducerPath]: addressApi.reducer,
       [bpostApi.reducerPath]: bpostApi.reducer
   },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat([addressApi.middleware,bpostApi.middleware]),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
