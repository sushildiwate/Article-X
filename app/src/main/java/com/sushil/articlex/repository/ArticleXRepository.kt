package com.sushil.articlex.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sushil.articlex.ArticleXApplication
import com.sushil.articlex.database.Article
import com.sushil.articlex.model.ArticleXModel
import com.sushil.articlex.network.ArticleXApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class ArticleXRepository(private val articleXApi: ArticleXApi) {

    private val articlexDao = ArticleXApplication.instance!!.getRoomDAO().articleDao()

    fun getArticlesFromAPI(pageNumber: Int, maxArticleLimit: Int): Observable<ArrayList<ArticleXModel>> {
        return articleXApi.getArticles(pageNumber, maxArticleLimit)
    }
    fun insertInDataBase(
        pageNumber: Int,
        articlesDataList: ArrayList<ArticleXModel>
    ): Observable<Unit> {
        return Observable.fromCallable {
            articlexDao.insert(
                Article(
                    pageNumber, Gson().toJson(articlesDataList)
                )
            )
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getArticlesCount(): Observable<Int> {
        return Observable.fromCallable { articlexDao.getCount() }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getArticlesByPageNumber(pageNumber: Int): Observable<ArrayList<ArticleXModel>> {
        return Observable.fromCallable {
            articlexDao.getArticleListByPageNumber(pageNumber)
        }.map { articleString: String ->
            parseArticle(articleString)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun parseArticle(dataString: String): ArrayList<ArticleXModel> {
        val listType = object : TypeToken<ArrayList<ArticleXModel>>() {}.type
        return Gson().fromJson(dataString, listType)
    }
}