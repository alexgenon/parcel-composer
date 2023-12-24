package be.aufildemescoutures.address_book

import be.aufildemescoutures.parcel_composer.address_book.Address
import be.aufildemescoutures.parcel_composer.address_book.AddressBookService
import be.aufildemescoutures.parcel_composer.user.UserId
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber

object AddressServiceHelper {
    fun newAddress(addressBookService: AddressBookService, userId: UserId, address: Address)=
        addressBookService
            .newAddress(userId,address)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .item
}
