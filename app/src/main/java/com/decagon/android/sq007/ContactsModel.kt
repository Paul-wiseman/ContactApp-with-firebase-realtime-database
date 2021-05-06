package com.decagon.android.sq007

class ContactsModel {

    // variables for our user name
    // and contact number.
    lateinit var userName: String
    lateinit var contactNumber: String

    // constructor
    constructor(userName: String, contactNumber: String) {
        this.userName = userName
        this.contactNumber = contactNumber
    }
}
