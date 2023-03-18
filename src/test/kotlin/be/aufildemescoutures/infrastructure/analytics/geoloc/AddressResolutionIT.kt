package be.aufildemescoutures.infrastructure.analytics.geoloc

import be.aufildemescoutures.address_book.DummyAddresses
import be.aufildemescoutures.parcel_composer.address_book.Address
import be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc.AddressOnMap
import be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc.AddressResolutionService
import be.aufildemescoutures.parcel_composer.infrastructure.analytics.geoloc.OpenStreetMapResolver
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.*
import org.mockito.Mockito.`when`
import java.io.InputStream
import javax.inject.Inject


@QuarkusTest
class AddressResolutionIT {
    @InjectMock
    @RestClient
    lateinit var openStreetMapResolver: OpenStreetMapResolver

    @Inject
    lateinit var addressResolutionService: AddressResolutionService

    @OptIn(ExperimentalSerializationApi::class)
    @BeforeEach
    fun setup(){
        val correctResponse:InputStream? = Thread.currentThread().getContextClassLoader().getResourceAsStream("OpenStreetMapResponse.json")
        val correctResponseJsonObject = Json.decodeFromStream<JsonElement>(correctResponse!!)
        `when` (openStreetMapResolver.search("jsonv2",1,"rue des remparts - 6600 Bastogne"))
            .thenReturn(Uni.createFrom().item(correctResponseJsonObject))
        `when` (openStreetMapResolver.search("jsonv2",1,"fakeAddress"))
            .thenReturn(Uni.createFrom().item(Json.parseToJsonElement("[]")))
    }

    @Test
    @DisplayName("Gets a correct address")
    fun checkCorrectAddressResolution(){
        val address = DummyAddresses.dummyBelgianAddress()
        val result = addressResolutionService.getAddress(address)
        val subscriber:UniAssertSubscriber<AddressOnMap> = result
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())

        val resultAddressOnMap = subscriber.assertCompleted().item
        assertAll("Correctly get lat and lon",
            { Assertions.assertEquals( resultAddressOnMap?.lat, 50.0057556) },
            { Assertions.assertEquals( resultAddressOnMap?.lon,5.7204901 ) }
        )
    }
}
