package com.example.firebase_danhba

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseManager {
    private val database = FirebaseDatabase.getInstance().reference

    fun getContacts(onDataReceived: (List<Contact>) -> Unit) {
        database.child("contacts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val contacts = mutableListOf<Contact>()
                for (data in snapshot.children) {
                    val contact = data.getValue(Contact::class.java)
                    contact?.let { contacts.add(it) }
                }
                onDataReceived(contacts)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }
        })
    }

    fun addContact(contact: Contact) {
        val newContactRef = database.child("contacts").push()
        newContactRef.setValue(contact)
    }

    fun updateContact(contactId: String, contact: Contact) {
        database.child("contacts").child(contactId).setValue(contact)
    }

    fun deleteContact(contactId: String) {
        database.child("contacts").child(contactId).removeValue()
    }
}
