package com.decagon.android.sq007

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.decagon.android.sq007.ContactDetailActivity
import com.decagon.android.sq007.ContactsModel
import com.wiseman.paul.myapplication.R

class ContactsRVAdapter
constructor( // creating variables for context and array list.
    var context: Context,
    var contactsModelArrayList: ArrayList<ContactsModel>
) : RecyclerView.Adapter<ContactsRVAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contactIV: ImageView = itemView.findViewById(R.id.idIVContact)
        var contactTV: TextView = itemView.findViewById(R.id.idTVContactName)

//        fun ViewHolder (itemView: View){
//            super.itemView
//            contactIV = itemView.findViewById(R.id.idIVContact)
//            contactTV = itemView.findViewById(R.id.idTVContactName)
//        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        // passing our layout file for displaying our card item
        val itemView = LayoutInflater.from(context).inflate(R.layout.contacts_rv_item, parent, false)
        return MyViewHolder(itemView)
//        return ViewHolder(LayoutInflater.from(context).inflate(android.R.layout.contacts_rv_item, parent, false))
    }
    // below method is use for filtering data in our array list
    fun filterList(filterlist: ArrayList<ContactsModel>) {
        // on below line we are passing filtered
        // array list in our original array list
        contactsModelArrayList = filterlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // getting data from array list in our modal.
        var model = contactsModelArrayList[position]
        // on below line we are setting data to our text view.
        holder.contactTV.setText(model.userName)
        var generator: ColorGenerator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        var color = generator.randomColor
        // below text drawable is a circular.
        var drawable2 = TextDrawable.builder().beginConfig()
            .width(100)
            .height(100)
            .endConfig()
            // as we are building a circular drawable
            // we are calling a build round method.
            // in that method we are passing our text and color.
            .buildRound(model.userName.substring(0, 1), color)
        // setting image to our image view on below line.
        holder.contactIV.setImageDrawable(drawable2)
        holder.itemView.setOnClickListener(
            View.OnClickListener() {

                // on below line we are opening a new activity and passing data to it.
                var intent = Intent(context, ContactDetailActivity::class.java)
                intent.putExtra("name", model.userName)
                intent.putExtra("contact", model.contactNumber)
                // on below line we are starting a new activity,
                context.startActivity(intent)
            }
        )
    }
    override fun getItemCount(): Int {
        return contactsModelArrayList.size
    }
}
