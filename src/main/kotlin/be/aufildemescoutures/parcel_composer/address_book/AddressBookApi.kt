package be.aufildemescoutures.parcel_composer.address_book

import org.jboss.logging.Logger
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/address-book")
class AddressBookApi {
    private val LOG = Logger.getLogger(javaClass)

    @Inject
    @field:Default
    lateinit var addressService: AddressBookService

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllAddresses() = addressService.getAllAddresses()

    @POST
    fun newAddress(address: Address) = addressService.newAddress(address)

    @DELETE
    @Path("{id}")
    fun removeAddress(@PathParam("id") id:String) = addressService.removeAddress(id)
}
