import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'
import {Address} from "./Address";

export const addressApi = createApi({
    reducerPath: 'addressApi',
    keepUnusedDataFor: 0,
    baseQuery: fetchBaseQuery({ baseUrl: 'http://localhost:8080/api/' }),
    endpoints: (builder) => ({
        getAllAddresses: builder.query<Address[],string>({
            query: (_) => `address-book/addresses`,
        }),
    }),
})

export const { useGetAllAddressesQuery } = addressApi
