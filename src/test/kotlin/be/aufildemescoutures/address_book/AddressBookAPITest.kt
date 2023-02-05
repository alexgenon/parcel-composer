package be.aufildemescoutures.address_book

import be.aufildemescoutures.parcel_composer.address_book.Address
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.RestAssured.post
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.Test
import javax.ws.rs.core.MediaType

@QuarkusTest
@TestSecurity(user = "testUser")
class AddressBookAPITest {
    val dummyAddress = Address("123","Ronald McDonald\\n Fast food Street","Ronald","McDonald","Fast food street","12","","Junk Food",156,"ro@mcdo.com")
    val dummyAddressUser2 = Address("456","SomeRandomText","Burger","King","Fast food street","34","","Junk Food",156,"burger@king.com")
    @Test
    fun testEmptyAddresses(){
        Given{
            auth().preemptive().basic("alex", "genon")
        }
        When {
            get ("/api/address-book")
        } Then {
            statusCode(200)
            body("size()",`is`(0))
        }
    }

    @Test
    fun testAddAddress(){
        Given{
            contentType(MediaType.APPLICATION_JSON)
            body(dummyAddress)
        } When{
            post("/api/address-book")
        } Then {
            statusCode(204)
        }
        When {
            get ("/api/address-book")
        } Then {
            statusCode(200)
            body("size()",`is`(1))
            body("[0].firstName",`is`("Ronald"))
        }
    }
}
