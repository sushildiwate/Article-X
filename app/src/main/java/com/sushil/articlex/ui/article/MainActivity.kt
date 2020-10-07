package com.sushil.articlex.ui.article

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sushil.articlex.ApplicationActivity
import com.sushil.articlex.R
import com.sushil.articlex.ui.article.adapter.ArticleXAdapter
import com.sushil.articlex.viewmodel.ArticleXViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : ApplicationActivity() {
    private var articlesAdapter: ArticleXAdapter = ArticleXAdapter(arrayListOf())
    var isLoading: Boolean = false
    var pageNumber: Int = 1
    private val mArticleXViewModel: ArticleXViewModel by viewModel()
    private var pageEmpty = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        mArticleXViewModel.getArticles(pageNumber, this)
        subscribeObserver()
    }

    private fun initUI() {
        //Custom Name set to action bar
        setCustomTitle(getString(R.string.app_name))

        //Pagination achieved on scroll state changed. It can be done better by custom scroll view. Custom scroll will not call the function even if the list is scrolled upwards
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articlesAdapter
            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val itemCount = this@apply.layoutManager?.itemCount ?: 0
                    val lastItemInList =
                        (this@apply.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (!isLoading && !pageEmpty && itemCount == lastItemInList + 1
                    ) {
                        mArticleXViewModel.getArticles(pageNumber, this@MainActivity) //calling Get article function present in model
                    } else if (itemCount == lastItemInList + 1 && newState == 1) //End of list
                        Toast.makeText(
                            this@MainActivity,
                            "You're All Caught Up",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            })
        }
    }

    //To get the data fetched through API/database and pass it to the adapter.
    private fun subscribeObserver() {
        mArticleXViewModel.responseLiveData.observe(this, Observer { articlesList ->
            if (articlesList.isNotEmpty()) {
                articlesAdapter.articleXModelList = articlesList
                pageEmpty = false
                pageNumber++
            } else {
                pageEmpty = true
            }
        })

        //Hide/show the progress bar
        mArticleXViewModel.progressBar.observe(this, Observer { it ->
            if (it == 0) {
                progressBar.visibility = View.VISIBLE
                this.isLoading = true
            } else {
                progressBar.visibility = View.GONE
                this.isLoading = false
            }
        })
    }

    private fun setCustomTitle(title: String?) {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val textView = TextView(this)
        textView.text = title
        textView.textSize = 20f
        textView.setTypeface(null, Typeface.BOLD)
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.gravity = Gravity.CENTER
        textView.setTextColor(resources.getColor(R.color.black))
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.customView = textView
    }
}