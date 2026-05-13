package com.example.kutirakone.model

data class Scrap(
    var id: String = "",
    var material: String = "",
    var price: String = "",
    var unit: String = "kg",
    var distance: String = "",
    var userName: String = "",
    var imageUrl: String = "",
    var description: String = "",
    var condition: String = "",
    var size: String = "",
    var timestamp: Long = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)