package com.example.firebase_danhba

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddContactActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var buttonSave: MaterialButton
    private lateinit var firebaseDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        // Khởi tạo Firebase
        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("contacts")

        // Khởi tạo các view
        editTextName = findViewById(R.id.editTextName)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextAddress = findViewById(R.id.editTextAddress)
        buttonSave = findViewById(R.id.buttonSave)

        // Lắng nghe sự kiện click vào nút Lưu
        buttonSave.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val address = editTextAddress.text.toString().trim()

            // Kiểm tra đầu vào
            if (validateInput(name, phone, email, address)) {
                // Nếu đầu vào hợp lệ, gọi hàm lưu liên hệ vào Firebase
                saveContact(name, phone, email, address)
            }
        }
    }



    // Hàm kiểm tra đầu vào
    private fun validateInput(name: String, phone: String, email: String, address: String): Boolean {
        // Kiểm tra tên không được để trống
        if (name.isEmpty()) {
            Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show()
            return false
        }

        // Kiểm tra số điện thoại không được để trống và có định dạng đúng
        if (phone.isEmpty()) {
            Toast.makeText(this, "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show()
            return false
        } else if (!phone.matches("^[0-9]+$".toRegex())) {
            Toast.makeText(this, "Số điện thoại chỉ chứa chữ số", Toast.LENGTH_SHORT).show()
            return false
        }

        // Kiểm tra email không được để trống và có định dạng đúng
        if (email.isEmpty()) {
            Toast.makeText(this, "Email không được để trống", Toast.LENGTH_SHORT).show()
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
            return false
        }

        // Kiểm tra địa chỉ không được để trống
        if (address.isEmpty()) {
            Toast.makeText(this, "Địa chỉ không được để trống", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    // Hàm lưu liên hệ vào Firebase
    private fun saveContact(name: String, phone: String, email: String, address: String) {
        // Kiểm tra nếu bất kỳ tham số nào là null, thay thế bằng giá trị mặc định hoặc giá trị an toàn
        val safeName = name ?: ""
        val safePhone = phone ?: ""
        val safeEmail = email ?: ""
        val safeAddress = address ?: ""

        // Tạo đối tượng liên hệ mới
        val contactId = FirebaseDatabase.getInstance().reference.push().key // Tạo ID ngẫu nhiên
        val contact = contactId?.let { Contact( safeName, safePhone, safeEmail, safeAddress,it) }

        // Lưu vào Firebase Realtime Database
        if (contactId != null) {
            FirebaseDatabase.getInstance().getReference("contacts")
                .child(contactId)
                .setValue(contact)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Liên hệ đã được thêm", Toast.LENGTH_SHORT).show()
                        finish() // Quay lại MainActivity
                    } else {
                        Toast.makeText(this, "Có lỗi khi lưu liên hệ", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}

