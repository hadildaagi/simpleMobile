package com.example.simple_auth_app.data

data class Product (
    val id : Long,
    val name : String,
    val price : Double,
    val description : String,
//    val imageUrl : Int
    val imageUrl : String
)
