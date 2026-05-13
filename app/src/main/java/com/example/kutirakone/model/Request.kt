package com.example.kutirakone.model

data class Request(
    var id: String = "",
    var scrapId: String = "",
    var requesterId: String = "",
    var ownerId: String = "",
    var type: String = "Buy", // Buy or Swap
    var status: String = "Pending", // Pending, Accepted, Completed
    var message: String = "",
    var timestamp: Long = 0
)