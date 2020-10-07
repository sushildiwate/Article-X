package com.sushil.articlex.model

data class ArticleXModel(
    var id: String,
    var createdAt: String,
    var content: String,
    var comments: Int,
    var likes: Int,
    var media: List<ArticleXMediaModel> = mutableListOf(),
    var user: List<ArticleXUserModel> = mutableListOf()
)
