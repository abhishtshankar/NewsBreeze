package com.abhisht28.newsfeed

data class News(
    val title: String,
    val author: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: String,
    val description: String,
    var isSaved: Boolean = false

)
