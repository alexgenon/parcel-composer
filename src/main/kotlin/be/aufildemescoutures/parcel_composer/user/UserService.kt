package be.aufildemescoutures.parcel_composer.user

import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class UserService {
    fun getUserFromId(id:UserId) = User(id)
}
