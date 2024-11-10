package com.example.firebase_danhba

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private val context: Context,
    private val contactList: MutableList<Contact>,
    private val onContactClickListener: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val phoneTextView: TextView = itemView.findViewById(R.id.textViewPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.nameTextView.text = contact.name
        holder.phoneTextView.text = contact.phone

        // Handle click event for each contact
        holder.itemView.setOnClickListener {
            onContactClickListener(contact)
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    // Update the list of contacts
    fun updateContactList(newList: List<Contact>) {
        contactList.clear()
        contactList.addAll(newList)
        notifyDataSetChanged()
    }
}


