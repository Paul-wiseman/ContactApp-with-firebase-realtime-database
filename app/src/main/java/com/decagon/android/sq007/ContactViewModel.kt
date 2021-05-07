package com.decagon.android.sq007

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
/*this viewmodel class acts as a controller or the middle way between the app and firebase
* and also were we host the functions to interact with database such as add, delete or update contact in the database*/
class ContactViewModel : ViewModel() {

    private val dbcontacts = FirebaseDatabase.getInstance().getReference(CONTACTS_NODE) // creating an object of the database connection
    /*a list of Exceptions to check if the data has been submitted to the database or not
    * if the data has been submitted to the data base we set the list to null
    * if not we print that error message in a toast message*/
    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    /*this list is used for observing if a contact was added to our database*/
    private val _contact = MutableLiveData<ContactsModel>()
    val contact: LiveData<ContactsModel> get() = _contact

    /*the function to add contact to fire base*/
    fun  addContact(contact: ContactsModel) {
        contact.id = dbcontacts.push().key // generating a key for the contact object

        /* to save the contact in the firebase we use the below method getting the id and passing in the contact
        * object which firebase will then compared the fields and submit to the database
        * the id is also like an indicator of the path to were the data is store
        * oncompletelistener is also use so as to know if the process is successful
        * */
        dbcontacts.child(contact.id!!).setValue(contact).addOnCompleteListener {
            Log.d("ViewModel", "$contact")
            if (it.isSuccessful) {
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }
    /*childeventlistener to observe our mutable live data*/
    private val childEventListener = object : ChildEventListener {

        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            /*getting a snapshot of the contact from the data base, i.e creating an object of the contact stored on the data base */
            val contact = snapshot.getValue(ContactsModel::class.java)
            contact?.id = snapshot.key // getting the id of the contact
            _contact.value = contact!! // putting the value into a contact object
            Log.d("Childeventlistener", "$contact")

        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val contact = snapshot.getValue(ContactsModel::class.java)
            contact?.id = snapshot.key
            _contact.value = contact!!

        }
        override fun onChildRemoved(snapshot: DataSnapshot) {
            val contact = snapshot.getValue(ContactsModel::class.java)
            contact?.id = snapshot.key
            contact?.isDeleted = true
            _contact.value = contact!!
        }
        override fun onCancelled(error: DatabaseError) {
        }
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }
    }

        /* the function is used for getting live update from the database
        * this function will be called in our contact fragment to check if a new contact has been added to our database if so
        * then display it*/
    fun getRealtimeUpdate() {
        /*we pass in the childEventListener object to addChildEventListener to observe the database*/
        dbcontacts.addChildEventListener(childEventListener)
    }
    /*this function updates the mutable live data*/
    fun updateContact(contact: ContactsModel) {
        dbcontacts.child(contact.id!!).setValue(contact)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }
    /*a function to delete contact*/
    fun deleteContact(contact: ContactsModel) {
        dbcontacts.child(contact.id!!).setValue(null)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }
        /*it is also a good practice to override on clear to remove the event listener once the
        * the fragment is cleared*/
    override fun onCleared() {
        super.onCleared()
        dbcontacts.removeEventListener(childEventListener)
    }



}
