package be.aufildemescoutures.parcel_composer.address_book

import be.aufildemescoutures.parcel_composer.user.UserId
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.SecurityIdentity
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger
import java.net.URI
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/address-book")
@Authenticated
class AddressBookApi {

    @Inject
    lateinit var securityIdentity: SecurityIdentity

    @Inject
    @field:Default
    lateinit var addressService: AddressBookService

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllAddresses(): Uni<List<Address>> = addressService.getAllAddresses(getUserId())

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAddress(@PathParam("id") businessId: String) = addressService.getAddress(getUserId(), businessId)

    @POST
    @ReactiveTransactional
    fun newAddress(address: Address) = addressService
        .newAddress(getUserId(), address)
        .map { ad ->
            Response.status(Response.Status.CREATED)
                .entity(ad)
                .build()
        }

    @DELETE
    @Path("{id}")
    @ReactiveTransactional
    fun removeAddress(@PathParam("id") businessId: String) = addressService
        .removeAddress(getUserId(), businessId)

    private fun getUserId() = UserId(securityIdentity.principal.name)
}
