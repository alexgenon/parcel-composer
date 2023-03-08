package be.aufildemescoutures.parcel_composer.address_book

import be.aufildemescoutures.parcel_composer.user.UserId
import io.quarkus.security.identity.SecurityIdentity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestForm
import java.io.File
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@kotlinx.serialization.Serializable
data class LoadingResult(val addressesAdded:Set<Address>, val errors: List<String>)

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
    fun loadFromLocalFile(
        @RestForm file:File,
        @RestForm("loading_strategy") @DefaultValue("TRUNCATE_AND_LOAD") loadingStrategy: LoadingStrategy,
        @RestForm("legacy_format") legacyFormat: Boolean,
    ): LoadingResult {
        val addressesString = file.readText();
        val loadingResult = if (!legacyFormat) {
            LoadingResult(Json.decodeFromString(addressesString), emptyList())
        } else {
            decodeLegacyFormat(addressesString)
        }
        LOG.info("About to load ${loadingResult.addressesAdded.size} using strategy $loadingStrategy")
        when (loadingStrategy) {
            LoadingStrategy.TRUNCATE_AND_LOAD -> addressService.resetAddressBook(getUserId(),loadingResult.addressesAdded)
            LoadingStrategy.MERGE -> {
                addressService.addAddresses(getUserId(),loadingResult.addressesAdded)
            }
        }
        return loadingResult

    }


    /**
     * Tries to parse a Json export from the parcel-composer-angular application.
     * There are slight differences in the data model
     * and some data coming from the Angular app might not be "clean enough" to be accepted by Kotlin data class
     * @return: a Pair containing the correctly parsed addresses and the list of errors that occurred during parsing
     */

    private fun JsonElement?.toCleanInt() = this.toString().replace("\"", "").toInt()
    private fun JsonElement?.toCleanString() = (this?:"").toString().replace("\"", "").trim()
    private fun decodeLegacyFormat(addressesString: String): LoadingResult {
        val jsonElements = Json.parseToJsonElement(addressesString).jsonArray
        var errorList= emptyList<String>()

        /* There's a bit of "Ninja/Kungfu functional code here: the goal is to try to parse each address from input array
        We have a JsonArray where some addresses can be correctly parsed and should added to the book
        Then for the elements that cannot be parsed, we should add the error message to the errorList variable, and they
        should not be in the book.
        I thought first about using map and returning null
        I would end with a Set<Address?> and then I would filter for non-null
        But after some Googling, I found some example using fold and I wanted to give it a try
        (https://www.reddit.com/r/Kotlin/comments/oxqic5/skipping_an_item_in_a_map/)
         */
        val parsedAddresses:Set<Address> = jsonElements
            .map(JsonElement::jsonObject)
            .fold(emptySet()) { targetSet, element ->
                try {
                    targetSet.plus(Address(
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
                    ))
                } catch (e: NumberFormatException) {
                    val errorMessage = "${element["originalString"].toString()} : ${e.message}"
                    errorList = errorList.plus(errorMessage)
                    LOG.error("Parsing error: $errorMessage")
                    targetSet
                }
            }
        return LoadingResult(parsedAddresses,errorList)
    }

    private fun getUserId() = UserId(securityIdentity.principal.name)
}
