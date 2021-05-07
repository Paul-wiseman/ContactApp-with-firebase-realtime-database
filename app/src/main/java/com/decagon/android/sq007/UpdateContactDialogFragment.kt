package com.decagon.android.sq007

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.wiseman.paul.myapplication.databinding.FragmentUpdateContactDialogBinding


// this is responsible for the dialog fragment to updating data to firebase
class UpdateContactDialogFragment(private val contact: ContactsModel) : DialogFragment() {
    private var _binding: FragmentUpdateContactDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ContactViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateContactDialogBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*we are taking the save contact info which was passed into this class as an object
        and setting it to as the editTextFullName and editTextContact*/
        binding.editTextFullName.setText(contact.contactName)
        binding.editTextContact.setText(contact.contactPhoneNumber)
        /*on the click of the update button the contact info is updated with the name input*/
        binding.buttonUpdate.setOnClickListener {
            /*on clicking the update button the input retrieved and converted to a string and saved as fullname and contactNumber*/
            val fullName = binding.editTextFullName.text.toString().trim()
            val contactNumber = binding.editTextContact.text.toString().trim()

            /*we validate the user input*/
            if (fullName.isEmpty()) {
                binding.editTextFullName.error = "This field is required"
                return@setOnClickListener
            }
            if (contactNumber.isEmpty()) {
                binding.editTextContact.error = "This field is required"
                return@setOnClickListener
            }

        /*updating the object of the contact class and passing in the input from the user and sending it to add function
        * in the viewmodel class to be sent to the data base*/
            contact.contactName = fullName
            contact.contactPhoneNumber = contactNumber
            viewModel.updateContact(contact)
            dismiss()
            Toast.makeText(context, "Contact has been updated", Toast.LENGTH_SHORT).show()
        }
    }
}
