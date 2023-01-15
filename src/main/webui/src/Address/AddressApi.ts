import {Address} from "./Address";
import {parcelComposerApi} from "../app/parcelComposerApi";

// noinspection TypeScriptValidateTypes
const addressApi = parcelComposerApi.injectEndpoints({
    endpoints: (builder) => ({
        getAllAddresses: builder.query<Address[],void>({
            query: () => 'address-book',
            providesTags: (result) => {
                let resultSafe =(result?result:[]);
                return [{ type: 'Address', id: 'LIST' }, ...resultSafe.map(({id}) => ({type: 'Address', id} as const))]
            }
        }),
        // TODO: Setup optimistic update to cope with no network https://redux-toolkit.js.org/rtk-query/usage/manual-cache-updates
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
