package be.aufildemescoutures.address_book

import org.jboss.logging.Logger
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/address-book")
class AddressBookApi {
    private val LOG = Logger.getLogger(javaClass)

    @Inject
    @field:Default
    lateinit var addressService: AddressBookService

    @GET
    @Path("addresses")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllAddresses() = addressService.getAllAddresses()
}
