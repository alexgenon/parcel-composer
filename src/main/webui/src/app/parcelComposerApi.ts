import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'
import {getAuthorizationHeader} from "./oidc";

// noinspection TypeScriptValidateTypes
export const parcelComposerApi = createApi({
    reducerPath: 'parcelComposer',
    tagTypes: ['Address', 'bpost'],
    baseQuery: fetchBaseQuery({
        baseUrl: '/api/',
        prepareHeaders: (headers) => {
            const token = getAuthorizationHeader();
            // If we have a token set in state, let's assume that we should be passing it.
            if (token) {
                headers.set('authorization',token);
            }
            return headers
        }
    }),
    endpoints: () => ({})
})
