package com.decagon.android.sq007

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.wiseman.paul.myapplication.R

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    // creating variables for our array list, recycler view progress bar and adapter.
    lateinit var contactsModelArrayList: ArrayList<ContactsModel>
    lateinit var contactRV: RecyclerView
    lateinit var contactsRVAdapter: ContactsRVAdapter
    lateinit var loadingPB: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // on below line we are initializing our variables.
        contactsModelArrayList = ArrayList()
        contactRV = findViewById(R.id.idRVContacts)
        var addNewContactFAB: FloatingActionButton = findViewById(R.id.idFABadd)
        loadingPB = findViewById(R.id.idPBLoading)

        // calling method to prepare our recycler view.
        prepareContactRV()

        // calling a method to request permissions.
        requestPermissions()

        // adding on click listener for our fab.
        // adding on click listener for our fab.
        addNewContactFAB.setOnClickListener { // opening a new activity on below line.
            val i = Intent(this@MainActivity, CreateNewContactActivity::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // in this on create options menu we are calling
        // a menu inflater and inflating our menu file.
        var inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        // on below line we are getting our menu item as search view item
        var searchViewItem = menu.findItem(R.id.app_bar_search)
        // on below line we are creating a variable for our search view.
        var searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        // on below line we are setting on query text listener for our search view.
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }
    // Override onQueryTextSubmit method
    // which is call
    // when submitquery is searched

    override fun onQueryTextSubmit(query: String?): Boolean {
        // on query submit we are clearing the focus for our search view.
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // on changing the text in our search view we are calling
        // a filter method to filter our array list.
        if (newText != null) {
            filter(newText.toLowerCase())
        }
        return false
    }
    fun filter(text: String) {
        // in this method we are filtering our array list.
        // on below line we are creating a new filtered array list.
        var filteredlist = ArrayList<ContactsModel>()
        // on below line we are running a loop for checking if the item is present in array list.
        for (item: ContactsModel in contactsModelArrayList) {
            if (item.userName.toLowerCase().contains(text.toLowerCase())) {
                // on below line we are adding item to our filtered array list.
                filteredlist.add(item)
            }
        }
        // on below line we are checking if the filtered list is empty or not.
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Contact Found", Toast.LENGTH_SHORT).show()
        } else {
            // passing this filtered list to our adapter with filter list method.
            contactsRVAdapter.filterList(filteredlist)
        }
    }

    private fun prepareContactRV() {
        // in this method we are preparing our recycler view with adapter.
        contactsRVAdapter = ContactsRVAdapter(this, contactsModelArrayList)
        // on below line we are setting layout manager.
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        contactRV.layoutManager = layoutManager
        // on below line we are setting adapter to our recycler view.
        contactRV.adapter = contactsRVAdapter
    }
    private fun requestPermissions() {
        // below line is use to request
        // permission in the current activity.
        Dexter.withActivity(this)
            // below line is use to request the number of
            // permissions which are required in our app.
            .withPermissions(
                Manifest.permission.READ_CONTACTS,
                // below is the list of permissions
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS, Manifest.permission.WRITE_CONTACTS
            )
            // after adding permissions we are
            // calling an with listener method.
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    // this method is called when all permissions are granted
                    if (p0 != null) {
                        if (p0.areAllPermissionsGranted()) {
                            // do you work now
                            getContacts()
                            Toast.makeText(this@MainActivity, "All the permissions are granted..", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // check for permanent denial of any permission
                    if (p0 != null) {
                        if (p0.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently,
                            // we will show user a dialog message.
                            showSettingsDialog()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    // this method is called when user grants some
                    // permission and denies some of them.
                    if (p1 != null) {
                        p1.continuePermissionRequest()
                    }
                }
            }).withErrorListener(object : PermissionRequestErrorListener {
                override fun onError(p0: DexterError?) {
                    // we are displaying a toast message for error message.
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()
                }
            })
            // below line is use to run the permissions
            // on same thread and to check the permissions
            .onSameThread().check()
    }

    // below is the shoe setting dialog
    // method which is use to display a
    // dialogue message.
    private fun showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

        // below line is the title
        // for our alert dialog.
        builder.setTitle("Need Permissions")

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton(
            "GOTO SETTINGS",
            DialogInterface.OnClickListener { dialog, which -> // this method is called on click on positive
                // button and on clicking shit button we
                // are redirecting our user from our app to the
                // settings page of our app.
                dialog.cancel()
                // below is the intent from which we
                // are redirecting our user.
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, 101)
            }
        )
        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> // this method is called when
                // user click on negative button.
                dialog.cancel()
            }
        )
        // below line is used
        // to display our dialog
        builder.show()
    }

    private fun getContacts() {
        // this method is use to read contact from users device.
        // on below line we are creating a string variables for
        // our contact id and display name.
        var contactId = ""
        var displayName = ""
        // on below line we are calling our content resolver for getting contacts
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        // on blow line we are checking the count for our cursor.
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                // if the count is greater than 0 then we are running a loop to move our cursor to next.
                while (cursor.moveToNext()) {
                    // on below line we are getting the phone number.
                    val hasPhoneNumber: Int =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                            .toInt()
                    if (hasPhoneNumber > 0) {
                        // we are checking if the has phone number is > 0
                        // on below line we are getting our contact id and user name for that contact
                        if (cursor != null) {
                            contactId =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        }
                        if (cursor != null) {
                            displayName =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        }
                        // on below line we are calling a content resolver and making a query
                        val phoneCursor: Cursor? = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(contactId),
                            null
                        )
                        // on below line we are moving our cursor to next position.
                        if (phoneCursor != null) {
                            if (phoneCursor.moveToNext()) {
                                // on below line we are getting the phone number for our users and then adding the name along with phone number in array list.
                                val phoneNumber: String =
                                    phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                contactsModelArrayList.add(ContactsModel(displayName, phoneNumber))
                            }
                        }
                        // on below line we are closing our phone cursor.
                        if (phoneCursor != null) {
                            phoneCursor.close()
                        }
                    }
                }
            }
        }
        // on below line we are closing our cursor.
        if (cursor != null) {
            cursor.close()
        }
        // on below line we are hiding our progress bar and notifying our adapter class.
        loadingPB.visibility = View.GONE
        contactsRVAdapter.notifyDataSetChanged()
    }
}
