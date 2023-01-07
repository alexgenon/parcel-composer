import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'
import {Address} from "./Address";

export const addressApi = createApi({
    reducerPath: 'addressApi',
    keepUnusedDataFor: 0,
    tagTypes:['Address'],
    baseQuery: fetchBaseQuery({ baseUrl: 'http://localhost:8080/api/' }),
    endpoints: (builder) => ({
        getAllAddresses: builder.query<Address[],void>({
            query: () => 'address-book',
            providesTags: (result) =>
                result ?
                    [
                        ...result.map(({id}) => ({type: 'Address', id} as const)),
                        {type: 'Address', id: 'LIST'}
                    ]
                :
                    [{ type: 'Address', id: 'LIST' }],
        }),
        addNewAddress: builder.mutation<Address,Partial<Address>>({
            query(body){
                return {
                    url : 'address-book',
                    method: 'POST',
                    body
                }
            },
            invalidatesTags: [{ type: 'Address', id: 'LIST' }],
        }),
        deleteAddress: builder.mutation<string,string>({
            query(id){
                return {
                    url : `address-book/${id}`,
                    method: 'DELETE'
                }
            },
            invalidatesTags: [{ type: 'Address', id: 'LIST' }],
        }),
    }),
})

export const { useGetAllAddressesQuery,useAddNewAddressMutation, useDeleteAddressMutation } = addressApi
