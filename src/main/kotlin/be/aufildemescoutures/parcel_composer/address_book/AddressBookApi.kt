package be.aufildemescoutures.parcel_composer.address_book

import be.aufildemescoutures.parcel_composer.user.UserId
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.SecurityIdentity
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
@Authenticated
class AddressBookApi {
    private val LOG = Logger.getLogger(javaClass)

    @Inject
    lateinit var securityIdentity: SecurityIdentity

    @Inject
    @field:Default
    lateinit var addressService: AddressBookService

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllAddresses():Set<Address> = addressService.getAllAddresses(getUserId())

    @POST
    fun newAddress(address: Address) = addressService.newAddress(getUserId(),address)

    @DELETE
    @Path("{id}")
    fun removeAddress(@PathParam("id") id:String) = addressService.removeAddress(getUserId(),id)

    private fun getUserId() = UserId(securityIdentity.principal.name)
}
