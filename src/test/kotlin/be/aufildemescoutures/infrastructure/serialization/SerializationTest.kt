package be.aufildemescoutures.infrastructure.serialization

import be.aufildemescoutures.address_book.DummyAddresses
import be.aufildemescoutures.parcel_composer.address_book.Address
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.jboss.logging.Logger
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class AddressSerializationTest {

    private val LOG = Logger.getLogger(javaClass)
    @Test
    fun instantCorrectlySerialized() {
        val dummyAddress = DummyAddresses.dummyAddress()
        val serDeserAddress = Json.decodeFromString<Address>(Json.encodeToString(dummyAddress))

        assertAll("Serialization then Deserialization did not altered the object", {
            assertEquals(dummyAddress.originalString,serDeserAddress.originalString)
            assertEquals(dummyAddress.creationTimestamp?.toEpochMilli(),serDeserAddress.creationTimestamp?.toEpochMilli())
            assertEquals(dummyAddress.postcode,serDeserAddress.postcode)
            assertEquals(dummyAddress.businessId,serDeserAddress.businessId)
        }
        )
    }

    @Test
    fun acceptAddressWithoutInstant() {
        val dummyAddressJson = Json.encodeToJsonElement(DummyAddresses.dummyAddress2())
            .jsonObject.minus("creationTimestamp")
        LOG.debug("Object that will be serialized $dummyAddressJson")
        val dummyAddress = Json.decodeFromString<Address>(Json.encodeToString(dummyAddressJson))
        assertEquals(DummyAddresses.dummyAddress2().businessId,dummyAddress.businessId)
    }

}
