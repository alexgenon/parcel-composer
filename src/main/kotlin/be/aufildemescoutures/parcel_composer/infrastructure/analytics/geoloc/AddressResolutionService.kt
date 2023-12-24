package be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc

import be.aufildemescoutures.parcel_composer.address_book.Address
import io.smallrye.mutiny.Uni
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import be.aufildemescoutures.parcel_composer.analytics.geoloc.AddressOnMap
import io.smallrye.mutiny.unchecked.Unchecked
import javax.ws.rs.NotFoundException

@ApplicationScoped
class AddressResolutionService {
    private val LOG = Logger.getLogger(javaClass)
    @RestClient
    private lateinit var openStreetMapResolver: OpenStreetMapResolver

    fun getAddress(address: Address): Uni<AddressOnMap> {
        val queryResult = openStreetMapResolver.search("jsonv2",1,address.toMapQuery())
        return convertQueryResult(queryResult,address)
    }

    companion object {
        private fun convertQueryResult(queryResult: Uni<JsonElement>, address: Address) =
            queryResult
                .map(JsonElement::jsonArray)
                .map (Unchecked.function { json ->
                    if (json.size == 0) {
                        throw NotFoundException("No match in OpenStreetMap")
                    } else {
                        val firstResult = json.jsonArray[0].jsonObject
                        AddressOnMap(address.businessId,
                            toDouble(firstResult, "lat"),
                            toDouble(firstResult, "lon"))
                    }
                })

        private fun Address.toMapQuery() = "${street} - ${postcode} ${city}"
        private fun toDouble(firstResult: JsonObject, key: String) =
            firstResult[key].toString().replace("\"", "").toDouble()
    }
}
