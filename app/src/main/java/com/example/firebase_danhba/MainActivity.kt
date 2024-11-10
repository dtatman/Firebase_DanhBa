package com.example.firebase_danhba

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var allContacts: MutableList<Contact>
    private lateinit var contactList: MutableList<Contact>
    private lateinit var firebaseDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firebase reference
        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("contacts")

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        allContacts= mutableListOf()
        contactList = mutableListOf()
        contactAdapter = ContactAdapter(this, contactList) { contact ->
            // Handle click to view/edit contact details
            val intent = Intent(this, ContactDetailActivity::class.java)
            intent.putExtra("CONTACT", contact)
            intent.putExtra("CONTACT_ID", contact.id)  // Pass Firebase ID for editing
            startActivity(intent)
        }
        recyclerView.adapter = contactAdapter

        // Fetch contacts from Firebase
        fetchContactsFromFirebase()

        // Setup SearchView
        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                allContacts
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContacts(newText)
                return true
            }
        })

        // Floating Action Button for adding new contact
        val fabAddContact: FloatingActionButton = findViewById(R.id.fabAddContact)
        fabAddContact.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchContactsFromFirebase() {
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                contactList.clear()
                for (contactSnapshot in snapshot.children) {
                    val contact = contactSnapshot.getValue(Contact::class.java)
                    contact?.let {
                        contact.id = contactSnapshot.key ?: ""
                        allContacts.add(it)
                        contactList.add(it)
                    }
                }
                contactAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error loading contacts", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterContacts(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            allContacts // Nếu không có từ khóa, hiển thị tất cả
        } else {
            contactList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.phone.contains(query, ignoreCase = true) ||
                        it.email.contains(query, ignoreCase = true) ||
                        it.address.contains(query, ignoreCase = true)
            }
        }
        contactAdapter.updateContactList(filteredList)
    }
}



