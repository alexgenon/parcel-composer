package be.aufildemescoutures.infrastructure.analytics.geoloc

import be.aufildemescoutures.address_book.DummyAddresses
import be.aufildemescoutures.parcel_composer.address_book.Address
import be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc.AddressOnMap
import be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc.AddressResolutionService
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import org.jboss.logging.Logger
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.io.InputStream


class AddressResolutionTest {
    private val LOG = Logger.getLogger(javaClass)

    /**
     * This is a fast way to test the parsing of OpenStreetMap parsing logic but it requires reflection
     * Not nice but the only way to test private methods
     * An alternative is to use Mocking for the OpenStreetMapResolver using @InjectMock @see AddressResolutionIT
     * But it is slower to run (is an integration test)
     */
    private fun callConvertQueryResult( json: JsonElement,  address: Address):AddressOnMap?{
        val parseMethod = AddressResolutionService.Companion.javaClass.getDeclaredMethod("convertQueryResult",Uni::class.java,Address::class.java)
        parseMethod.isAccessible=true
        val parseParameters = arrayOfNulls<Any>(2)
        parseParameters[0] = Uni.createFrom().item(json)
        parseParameters[1] = address
        val result = parseMethod.invoke(AddressResolutionService.Companion,*parseParameters) as Uni<AddressOnMap>
        val subscriber:UniAssertSubscriber<AddressOnMap> = result
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
        return subscriber.assertCompleted().item
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    @DisplayName("Correctly parses a traditional belgian address")
    fun checkCorrectAddressResolution(){
        val correctResponse:InputStream? = Thread.currentThread().getContextClassLoader().getResourceAsStream("OpenStreetMapResponse.json")
        val correctResponseJsonObject = Json.decodeFromStream<JsonElement>(correctResponse!!)
        val resultAddressOnMap = callConvertQueryResult(correctResponseJsonObject,DummyAddresses.dummyBelgianAddress())
        assertAll("Correctly get lat and lon",
            { assertEquals( resultAddressOnMap?.lat, 50.0057556) },
            { assertEquals( resultAddressOnMap?.lon,5.7204901 ) }
        )
    }

    @Test
    @DisplayName("Correctly manages when there is no result")
    fun checkNoResult(){
        val emptyResponse = Json.parseToJsonElement("[]")
        val result = callConvertQueryResult(emptyResponse,DummyAddresses.dummyBelgianAddress())
        assertNull(result)
    }

}
