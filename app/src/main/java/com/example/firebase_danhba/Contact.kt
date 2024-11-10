package com.example.firebase_danhba

import java.io.Serializable

data class Contact(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    var id: String=""
) : Serializable