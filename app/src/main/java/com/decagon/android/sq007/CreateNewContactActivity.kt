package com.decagon.android.sq007

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wiseman.paul.myapplication.R

open class CreateNewContactActivity : AppCompatActivity() {
    // creating a new variable for our edit text and button.
    lateinit var nameEdt: EditText // creating a new variable for our edit text and button.
    lateinit var phoneEdt: EditText // creating a new variable for our edit text and button.
    lateinit var emailEdt: EditText
    lateinit var addContactEdt: Button
    lateinit var ContactsRVAdapter: ContactsRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_contact)
        // on below line we are initializing our variables.
        nameEdt = findViewById(R.id.idEdtName)
        phoneEdt = findViewById(R.id.idEdtPhoneNumber)
        emailEdt = findViewById(R.id.idEdtEmail)
        addContactEdt = findViewById(R.id.idBtnAddContact)

        // on below line we are adding on click listener for our button.
        addContactEdt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                // on below line we are getting text from our edit text.
                val name = nameEdt.getText().toString()
                val phone = phoneEdt.getText().toString()
                val email = emailEdt.getText().toString()

                // on below line we are making a text validation.
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone)) {
                    Toast.makeText(
                        this@CreateNewContactActivity,
                        "Please enter the data in all fields. ",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // calling a method to add contact.
                    addContact(name, email, phone)
                }
            }
        })
    }

    private fun addContact(
        name: String,
        email: String,
        phone: String
    ) {
        // in this method we are calling an intent and passing data to that
        // intent for adding a new contact.
        val contactIntent = Intent(ContactsContract.Intents.Insert.ACTION)
        contactIntent.type = ContactsContract.RawContacts.CONTENT_TYPE
        contactIntent
            .putExtra(ContactsContract.Intents.Insert.NAME, name)
            .putExtra(ContactsContract.Intents.Insert.PHONE, phone)
            .putExtra(ContactsContract.Intents.Insert.EMAIL, email)
        startActivityForResult(contactIntent, 1)
        var contact = ContactsModel(name, phone)
        ContactsRVAdapter.contactsModelArrayList.add(contact)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // in on activity result method.
        if (requestCode == 1) {
            // we are checking if the request code is 1
            if (resultCode == Activity.RESULT_OK) {
                // if the result is ok we are displaying a toast message.
                Toast.makeText(this, "Contact has been added.", Toast.LENGTH_SHORT).show()
                var i = Intent(
                    this@CreateNewContactActivity, MainActivity::class.java
                )
                startActivity(i)
            }
            // else we are displaying a message as contact addition has cancelled.
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                    this, "Cancelled Added Contact",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
