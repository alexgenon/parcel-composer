import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'

// noinspection TypeScriptValidateTypes
export const parcelComposerApi = createApi({
    reducerPath: 'parcelComposer',
    tagTypes: ['Address', 'bpost'],
    baseQuery: fetchBaseQuery({baseUrl: 'http://localhost:8080/api/'}),
    endpoints: () => ({})
})
