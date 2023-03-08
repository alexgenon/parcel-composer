import {parcelComposerApi} from "../app/parcelComposerApi";

// noinspection TypeScriptValidateTypes
const addressLoaderApi = parcelComposerApi.injectEndpoints({
    endpoints: (builder) => ({
        loadAddresses: builder.mutation({
            query(body){
                return {
                    url : 'address-loader',
                    method: 'POST',
                    body
                }
            },
            invalidatesTags: [{ type: 'Address', id: 'LIST' }],
        })
    }),
})

export const { useLoadAddressesMutation } = addressLoaderApi
