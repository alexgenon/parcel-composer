import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";

// noinspection TypeScriptValidateTypes
export const bpostApi = createApi({
    reducerPath: 'bpostApi',
    keepUnusedDataFor: 0,
    baseQuery: fetchBaseQuery({baseUrl: "http://localhost:8080/api"}),
    endpoints: (builder) =>({
      getSenderHeader: builder.query<string,void>({
          query: () => 'bpost/sender_header'
      })
    })
})

export const {useGetSenderHeaderQuery} = bpostApi
