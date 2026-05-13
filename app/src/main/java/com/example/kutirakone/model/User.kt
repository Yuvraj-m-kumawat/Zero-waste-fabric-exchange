package com.example.kutirakone.model

data class User(
    var uid: String = "",
    var name: String = "",
    var phoneNumber: String = "",
    var location: String = "",
    var profileImageUrl: String = "",
    var userType: String = "Individual" // Individual or Business
)