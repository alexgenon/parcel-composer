package be.aufildemescoutures.address_book

import be.aufildemescoutures.parcel_composer.address_book.Address

object DummyAddresses {
    fun dummyAddress() = Address("123","Ronald McDonald\\n Fast food Street","Ronald","McDonald","Fast food street","12","","Junk Food",156,"ro@mcdo.com")
    fun dummyAddress2() = Address("456","SomeRandomText","Burger","King","Fast food street","34","","Junk Food",156,"burger@king.com")
    fun dummyBastogneAddress() = Address("789","SomeRandomTextBastogne","Ben","Genlut","rue des remparts", "1", "", "Bastogne", 6600, null)
    fun dummyBertogneAddress() = Address("101112","SomeRandomTextBertogne","JM","CoFran","rue grande", "1", "", "Bertogne", 6687, null)
    fun dummyFauvillersAddress() = Address("131415","SomeRandomTextFauvillers","Lasconi","Manstil","place communale", "1", "", "Fauvillers", 6637, null)
}
