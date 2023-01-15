import {parcelComposerApi} from "../app/parcelComposerApi";

// noinspection TypeScriptValidateTypes
const bpostApi = parcelComposerApi.injectEndpoints({
    endpoints: (builder) =>({
      getSenderHeader: builder.query<string,void>({
          query: () => 'bpost/sender_header'
      })
    })
})

export const {useGetSenderHeaderQuery} = bpostApi
