package com.sushil.articlex.network


import com.sushil.articlex.BuildConfig
import com.sushil.articlex.repository.ArticleXRepository
import com.sushil.articlex.utils.API_TIMEOUT
import com.sushil.articlex.utils.BASE_URL
import com.sushil.articlex.viewmodel.ArticleXViewModel
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val retrofit: Retrofit = createNetworkClient(BASE_URL)

private val articleXApi: ArticleXApi = retrofit.create(ArticleXApi::class.java)

fun createNetworkClient(BASE_URL: String) =
    retrofitClient(BASE_URL, okHttpClient())

private fun retrofitClient(baseUrl: String, httpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

private fun okHttpClient() = OkHttpClient.Builder()
    .cache(null)
    .addInterceptor(logInterceptor()).apply { timeOutForOkHttpClient(this) }
    .addInterceptor(headersInterceptor()).build()

private fun logInterceptor() = HttpLoggingInterceptor().apply {
    level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}

fun headersInterceptor() = Interceptor { chain ->
    chain.proceed(
        chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
    )
}

private fun timeOutForOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder) =
    okHttpClientBuilder.apply {
        readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
    }

val network = module {
    single { articleXApi }
}

val repository = module {
    single { ArticleXRepository(articleXApi = get()) }
}

val viewModel = module {
    viewModel { ArticleXViewModel(get()) }
}


