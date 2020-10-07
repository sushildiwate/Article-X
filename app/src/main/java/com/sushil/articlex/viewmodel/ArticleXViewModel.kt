package com.sushil.articlex.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sushil.articlex.model.ArticleXModel
import com.sushil.articlex.repository.ArticleXRepository
import com.sushil.articlex.utils.MAX_ARTICLE_LIMIT
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class ArticleXViewModel(private val articleXRepository: ArticleXRepository) : ViewModel() {
    val responseLiveData = MutableLiveData<ArrayList<ArticleXModel>>()
    val progressBar = MutableLiveData<Int>()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    private lateinit var subscription: Disposable

    fun getArticles(pageNumber: Int,context: Context) {
        subscription = articleXRepository.getArticlesCount().subscribe(
            { count ->
                getArticlesList(pageNumber, count,context)
            },
            {
                getArticlesList(pageNumber, 0,context)
            }
        )
    }

    private fun getArticlesList(pageNumber: Int, dbCount: Int,context: Context) {
        if (dbCount >= pageNumber) {
            getArticleListFromDataBase(pageNumber,context)
        } else {
            subscription =
                articleXRepository.getArticlesFromAPI(pageNumber, MAX_ARTICLE_LIMIT).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { progressBar.value = 0 }
                    .doOnTerminate { progressBar.value = 1 }
                    .subscribe(
                        { result ->
                            insertArticlesInDataBase(pageNumber, result,context)
                        },
                        { error ->
                            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                        }
                    )
        }
    }

    fun getArticleListFromDataBase(pageNumber: Int,context: Context) {
        subscription = articleXRepository.getArticlesByPageNumber(pageNumber)
            .doOnSubscribe { progressBar.value = 0 }
            .doOnTerminate { progressBar.value = 1 }
            .subscribe({ articleList: ArrayList<ArticleXModel> ->
                responseLiveData.value = articleList
            }, { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            })
    }

    private fun insertArticlesInDataBase(
        pageNumber: Int,
        articleList: ArrayList<ArticleXModel>,context: Context
    ) {
        subscription = articleXRepository.insertInDataBase(pageNumber, articleList)
            .doOnSubscribe { progressBar.value = 0 }
            .doOnTerminate { progressBar.value = 1 }
            .subscribe({ _ ->
                responseLiveData.value = articleList
            }, { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            })
    }
}