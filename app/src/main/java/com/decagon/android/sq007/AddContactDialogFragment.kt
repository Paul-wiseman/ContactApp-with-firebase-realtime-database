package com.decagon.android.sq007

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wiseman.paul.myapplication.R
import com.wiseman.paul.myapplication.databinding.FragmentAddContactDialogBinding

// this class is responsible for the pop up dialog fragment to add contacts to contact list and firebase
class AddContactDialogFragment : DialogFragment() {
    lateinit var contactsModelArrayList: ArrayList<ContactsModel>
    /*using view binding*/
    private var _binding: FragmentAddContactDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContactViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*the style and theme setting for the add contact dialog fragment*/
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)

        /*creating an object of the viewmodel class so that we can ass the add function on the contact viewmodel so that
        on clicking of the save button the information is sent to fire base data base*/
        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contactsModelArrayList = arrayListOf()
        viewModel.result.observe(
            viewLifecycleOwner,
            Observer {
                val message = if (it == null) {
                    getString(R.string.add_contact)
                } else {
                    getString(R.string.error, it.message)
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                dismiss()
            }
        )
        /*the save button on the add contact pop dialog, used to save contact also send data to firebase data base*/
        binding.buttonAdd.setOnClickListener {
            /*getting the information passed in by the user*/
            val fullName = binding.editTextFullName.text.toString().trim()
            val contactNumber = binding.editTextContact.text.toString().trim()

            /*validating the input from the user*/
            if (fullName.isEmpty()) {
                binding.editTextFullName.error = "This field is required"
                return@setOnClickListener
            }
            if (contactNumber.isEmpty()) {
                binding.editTextContact.error = "This field is required"
                return@setOnClickListener
            }
        /*creating an object of the contact class and passing in the input from the user and sending it to add function
        * in the viewmodel class to be sent to the data base*/
            val contact = ContactsModel()
            contact.contactPhoneNumber = contactNumber
            contact.contactName = fullName

            viewModel.addContact(contact)
            contactsModelArrayList.add(contact)
            // sending object with the input from the user to the add function in the viewmode class
//            viewModel.result.observe(viewLifecycleOwner, Observer {
//                if (it == null) dismiss() else {
//                    Snackbar.make(requireView(),it.toString(),Snackbar.LENGTH_LONG).show()
//                }
//            })

            dismiss()
        }
    }
}
