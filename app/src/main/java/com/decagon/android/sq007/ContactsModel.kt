package com.decagon.android.sq007

import com.google.firebase.database.Exclude

data class ContactsModel(
    @get:Exclude
    var id: String? = null,
    var contactName: String? = null,
    var contactPhoneNumber: String? = null,

    /* we are checking if the contact is deleted*/
    @get:Exclude
    var isDeleted: Boolean = false // once deleted this become true and the contact is deleted from the to data base
) {

    /*we also need to check if the id is matches the id of the contact we want to delete and if so we need to generate a hascode for it*/
    override fun equals(other: Any?): Boolean {
        return if (other is ContactsModel) {
            other.id == id
        } else false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (contactName?.hashCode() ?: 0)
        result = 31 * result + (contactPhoneNumber?.hashCode() ?: 0)
        result = 31 * result + isDeleted.hashCode()
        return result
    }
}
