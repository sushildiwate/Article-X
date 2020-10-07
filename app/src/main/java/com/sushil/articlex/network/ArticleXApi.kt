package com.sushil.articlex.network

import com.sushil.articlex.model.ArticleXModel
import com.sushil.articlex.utils.BLOG
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleXApi {

    @GET(BLOG)
    fun getArticles(
        @Query("page") pageNumber: Int,
        @Query("limit") maxArticleLimit: Int
    ): Observable<ArrayList<ArticleXModel>>
}