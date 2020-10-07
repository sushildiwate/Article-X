package com.sushil.articlex

import android.app.Application
import com.sushil.articlex.database.ArticleXDataBase
import com.sushil.articlex.network.network
import com.sushil.articlex.network.repository
import com.sushil.articlex.network.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class ArticleXApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            androidContext(this@ArticleXApplication)
            modules(listOf(network, repository, viewModel))
        }
    }

    fun getRoomDAO(): ArticleXDataBase {
        return ArticleXDataBase.getDatabase(this)
    }

    companion object {
        @get:Synchronized
        var instance: ArticleXApplication? = null
            private set
    }
}