package com.decagon.android.sq007

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.wiseman.paul.myapplication.R

class ContactDetailActivity : AppCompatActivity() {

    // creating variables for our image view and text view and string. .
    lateinit var contactName: String // creating variables for our image view and text view and string. .
    lateinit var contactNumber: String
    lateinit var contactTV: TextView
    lateinit var nameTV: TextView
    lateinit var contactIV: ImageView
    lateinit var callIV: ImageView
    lateinit var messageIV: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        // on below line we are getting data which
        // we passed in our adapter class with intent.
        contactName = getIntent().getStringExtra("name").toString()
        contactNumber = getIntent().getStringExtra("contact").toString()

        // initializing our views.
        nameTV = findViewById(R.id.idTVName)
        contactIV = findViewById(R.id.idIVContact)
        contactTV = findViewById(R.id.idTVPhone)
        nameTV.setText(contactName)
        contactTV.setText(contactNumber)
        callIV = findViewById(R.id.idIVCall)
        messageIV = findViewById(R.id.idIVMessage)

        // on below line adding click listener for our calling image view.
        // on below line adding click listener for our calling image view.
        callIV.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // calling a method to make a call.
                makeCall(contactNumber)
            }
        })

        messageIV.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                sendMessage(contactNumber)
            }

        })

    }

    private fun sendMessage(contactNumber: String) {
        // in this method we are calling an intent to send sms.
        // on below line we are passing our contact number.
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$contactNumber"))
        intent.putExtra("sms_body", "Enter your message")
        startActivity(intent)
    }

    private fun makeCall(contactNumber: String) {
        // this method is called for making a call.
        // on below line we are calling an intent to make a call.
        val callIntent = Intent(Intent.ACTION_CALL)
        // on below line we are setting data to it.
        callIntent.data = Uri.parse("tel:$contactNumber")
        // on below line we are checking if the calling permissions are granted not.
        if (ActivityCompat.checkSelfPermission(
                this@ContactDetailActivity,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // at last we are starting activity.
        startActivity(callIntent)
    }

    private fun sendSMS(contactNumber: String) {
        // this method is called for making a call.
        // on below line we are calling an intent to make a call.
        val callIntent = Intent(Intent.ACTION_SEND)
        // on below line we are setting data to it.
        callIntent.data = Uri.parse("tel:$contactNumber")
        // on below line we are checking if the calling permissions are granted not.
        if (ActivityCompat.checkSelfPermission(
                this@ContactDetailActivity,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // at last we are starting activity.
        startActivity(callIntent)
    }
}
