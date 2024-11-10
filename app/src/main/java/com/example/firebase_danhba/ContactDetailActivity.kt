package com.example.firebase_danhba

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ContactDetailActivity : AppCompatActivity() {
    private  lateinit var id: String
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    private lateinit var firebaseDatabase: DatabaseReference
    private lateinit var contactId: String
    private lateinit var contact: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        // Initialize views
        nameEditText = findViewById(R.id.editTextName)
        phoneEditText = findViewById(R.id.editTextPhone)
        emailEditText = findViewById(R.id.editTextEmail)
        addressEditText = findViewById(R.id.editTextAddress)
        saveButton = findViewById(R.id.buttonSave)
        deleteButton = findViewById(R.id.buttonDelete)

        // Firebase reference
        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("contacts")

        // Get the contact object passed from MainActivity
        contact = intent.getSerializableExtra("CONTACT") as Contact
        contactId = intent.getStringExtra("CONTACT_ID")!!

        // Pre-fill data into the EditText fields
        id= contact.id
        nameEditText.setText(contact.name)
        phoneEditText.setText(contact.phone)
        emailEditText.setText(contact.email)
        addressEditText.setText(contact.address)

        // Save button click listener
        saveButton.setOnClickListener {
            updateContactInFirebase()
        }

        // Delete button click listener
        deleteButton.setOnClickListener {
            deleteContactFromFirebase()
        }

    }

    private fun updateContactInFirebase() {
        val updatedContact = Contact(
            nameEditText.text.toString(),
            phoneEditText.text.toString(),
            emailEditText.text.toString(),
            addressEditText.text.toString(),
            id
        )

        firebaseDatabase.child(contactId).setValue(updatedContact)
            .addOnSuccessListener {
                Toast.makeText(this, "Thông tin liên hệ đã được cập nhật", Toast.LENGTH_SHORT).show()
                finish()  // Close the activity
            }
            .addOnFailureListener {
                Toast.makeText(this, "Lỗi khi cập nhật thông tin", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteContactFromFirebase() {
        firebaseDatabase.child(contactId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Liên hệ đã bị xóa", Toast.LENGTH_SHORT).show()
                finish()  // Close the activity
            }
            .addOnFailureListener {
                Toast.makeText(this, "Lỗi khi xóa liên hệ", Toast.LENGTH_SHORT).show()
            }
    }
}
