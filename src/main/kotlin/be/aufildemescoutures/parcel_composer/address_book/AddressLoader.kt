package be.aufildemescoutures.parcel_composer.address_book

import be.aufildemescoutures.parcel_composer.user.UserId
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.quarkus.security.identity.SecurityIdentity
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestForm
import java.io.File
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@kotlinx.serialization.Serializable
data class ConversionResult(val address: Address?, val conversionError: String?)

@Path("/address-loader")
class AddressLoader {
    enum class LoadingStrategy { TRUNCATE_AND_LOAD, MERGE }

    private val LOG = Logger.getLogger(javaClass)

    @Inject
    @field:Default
    lateinit var addressService: AddressBookService

    @Inject
    lateinit var securityIdentity: SecurityIdentity

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @ReactiveTransactional
    fun loadFromFile(
        @RestForm file: File,
        @RestForm("loading_strategy") @DefaultValue("TRUNCATE_AND_LOAD") loadingStrategy: LoadingStrategy,
        @RestForm("legacy_format") legacyFormat: Boolean,
    ): Uni<Pair<List<String>, AddressBook>> {
        val addressesString = file.readText();
        val conversionResults = if (!legacyFormat) {
            Json.decodeFromString<List<Address>>(addressesString)
                .map { ConversionResult(it, null) }
        } else {
            Json.parseToJsonElement(addressesString).jsonArray
                .map(JsonElement::jsonObject)
                .map { decodeLegacyFormat(it) }
        }
        val addresses:Set<Address> = conversionResults.filter { it.address != null }.map{ it.address!! }.toSet()
        val conversionErrors:List<String> = conversionResults.filter { it.conversionError != null }.map{it.conversionError!!}
        LOG.info("About to load ${addresses.size} new addresses Book using strategy $loadingStrategy")
        LOG.info("Following errors reported:\n${conversionErrors.joinToString("\n")}")

        return addressService.addAddresses(
                getUserId(),
                addresses,
                loadingStrategy == LoadingStrategy.TRUNCATE_AND_LOAD
            ).map { Pair(conversionErrors,it) }
    }


    /**
     * Tries to parse a Json export from the parcel-composer-angular application.
     * There are slight differences in the data model
     * and some data coming from the Angular app might not be "clean enough" to be accepted by Kotlin data class
     * @return: a Pair containing the correctly parsed addresses and the list of errors that occurred during parsing
     */

    private fun JsonElement?.toCleanInt() = this.toString().replace("\"", "").toInt()
    private fun JsonElement?.toCleanString() = (this ?: "").toString().replace("\"", "").trim()
    private fun decodeLegacyFormat(element: JsonObject): ConversionResult {
        try {
            val address = Address(
                businessId = element["firstName"].toCleanString() + element["lastName"].toCleanString() + element["city"].toCleanString(),
                originalString = element["originalString"].toCleanString(),
                firstName = element["firstName"].toCleanString(),
                lastName = element["lastName"].toCleanString(),
                street = element["street"].toCleanString(),
                streetNb = element["street_nb"].toCleanString(),
                postboxLetter = element["postboxLetter"].toCleanString(),
                city = element["city"].toCleanString(),
                postcode = element["postcode"]!!.toCleanInt(), // No Postcode, not a good address
                email = element["email"].toCleanString()
            )
            return ConversionResult(address, null)
        } catch (e: NumberFormatException) {
            val errorMessage = "${element["originalString"].toString()} : ${e.message}"
            LOG.error("Parsing error: $errorMessage")
            return ConversionResult(null, errorMessage)
        }
    }

    private fun getUserId() = UserId(securityIdentity.principal.name)
}
