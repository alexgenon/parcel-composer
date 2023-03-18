package be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc

import be.aufildemescoutures.parcel_composer.address_book.Address
import io.smallrye.mutiny.Uni
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam


/*
Example for https://nominatim.openstreetmap.org/search.php?format=jsonv2&addressdetails=1&q=rue%20des%20remparts%20-%206600%20Bastogne see src/test/resources/OpenStreetMapResponse.json

 */
@RegisterRestClient
interface OpenStreetMapResolver {
    @Path("search.php")
    @GET
    fun search(@QueryParam("format") format:String,
               @QueryParam("addressdetails") addressDetails: Int,
               @QueryParam("q") address:String): Uni<JsonElement>

    companion object {

    }
}

