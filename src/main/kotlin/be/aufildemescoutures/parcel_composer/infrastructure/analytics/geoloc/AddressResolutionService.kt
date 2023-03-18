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

data class AddressOnMap(val address: Address, val lat: Double, val lon: Double)

@ApplicationScoped
class AddressResolutionService {
    private val LOG = Logger.getLogger(javaClass)
    @RestClient
    private lateinit var openStreetMapResolver: OpenStreetMapResolver

    fun getAddress(address: Address): Uni<AddressOnMap?> {
        val queryResult = openStreetMapResolver.search("jsonv2",1,formatAddress(address))
        return convertQueryResult(queryResult,address)
    }

    companion object {
        private fun convertQueryResult(queryResult: Uni<JsonElement>, address: Address) =
            queryResult
                .map(JsonElement::jsonArray)
                .map { json ->
                    if (json.size == 0) {
                        null
                    } else {
                        val firstResult = json.jsonArray[0].jsonObject
                        AddressOnMap(address, toDouble(firstResult, "lat"), toDouble(firstResult, "lon"))
                    }
                }

        private fun formatAddress(ad: Address) = "${ad.street} - ${ad.postcode} ${ad.city}"
        private fun toDouble(firstResult: JsonObject, key: String) =
            firstResult[key].toString().replace("\"", "").toDouble()
    }
}
