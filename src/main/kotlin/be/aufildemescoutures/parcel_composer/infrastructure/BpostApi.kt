package be.aufildemescoutures.parcel_composer.infrastructure

import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("bpost")
class BpostApi {
    @ConfigProperty(name="parcel-composer.bpost.default-sender-header")
    lateinit var senderHeader: String

    @GET
    @Path("sender_header")
    fun returnSenderHeader():String = senderHeader
}
