package be.aufildemescoutures.address_book

import be.aufildemescoutures.parcel_composer.address_book.Address
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.lessThan
import org.jboss.logging.Logger
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.time.Instant
import javax.ws.rs.core.MediaType

@QuarkusTest
@TestSecurity(user = "testUser")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AddressBookAPITest {

    private val LOG = Logger.getLogger(javaClass)
    @Order(1)
    @Test
    fun testEmptyAddresses(){
        When {
            get ("/api/address-book")
        } Then {
            log().all()
            statusCode(200)
            body("size()",`is`(0))
        }
    }

    fun addAddress(address: Address){
        val addressJson = Json.encodeToJsonElement(address)
            // Scenario is to call the API endpoint for a new address and creationTimestamp is set by the
            // backend.
            .jsonObject.minus("creationTimestamp")
        LOG.debug("Will submit the following address: $addressJson")
        Given{
            contentType(MediaType.APPLICATION_JSON)
            // RestAssured uses Gson or Jackson but does not support kotlinx-serialization.
            // Have to explicitly serialize to avoid mismatch
            body(Json.encodeToString(addressJson))
        } When{
            post("/api/address-book")
        } Then {
            statusCode(201)
            body("firstName",`is`(address.firstName))
        }
    }

    @Test
    @Order(2)
    fun testAddAddress(){
        val ronald = DummyAddresses.dummyAddress()
        addAddress(ronald)
        val now = Instant.now().toEpochMilli()
        When {
            get ("/api/address-book")
        } Then {
            log().all()
            statusCode(200)
            body("size()",`is`(1))
            body("[0].firstName",`is`(ronald.firstName))
            body("[0].creationTimestamp", allOf(greaterThanOrEqualTo(now-1000), lessThan(now+1000)))
        }
    }
}
