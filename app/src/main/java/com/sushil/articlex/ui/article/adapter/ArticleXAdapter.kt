package com.sushil.articlex.ui.article.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sushil.articlex.R
import com.sushil.articlex.model.ArticleXMediaModel
import com.sushil.articlex.model.ArticleXModel
import com.sushil.articlex.model.ArticleXUserModel
import com.sushil.articlex.utils.articleDateTime
import com.sushil.articlex.utils.getFormatedValue
import kotlinx.android.synthetic.main.inflater_recyclerview_articlex_item.view.*


class ArticleXAdapter(articleXModel: ArrayList<ArticleXModel>) :
    RecyclerView.Adapter<ArticleXAdapter.ArticleViewHolder>() {

    var articleXModelList: ArrayList<ArticleXModel> = arrayListOf()
        set(value) {
            articleXModelList.addAll(value)
            notifyDataSetChanged()
        }

    init {
        this.articleXModelList = articleXModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.inflater_recyclerview_articlex_item, parent, false)
        )


    override fun getItemCount(): Int = articleXModelList.size


    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articleXModelList[position])
    }

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(articleXModel: ArticleXModel) {
            val articleXUserModel: ArticleXUserModel? =
                if (articleXModel.user.isNotEmpty()) articleXModel.user[0] else null
            val articleXMediaModel: ArticleXMediaModel? =
                if (articleXModel.media.isNotEmpty()) articleXModel.media[0] else null

            val requestOptionAvatar = RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .skipMemoryCache(true)
                .centerCrop()
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(itemView.context)
                .load(articleXUserModel?.avatar)
                .apply(requestOptionAvatar)
                .into(itemView.image_view)

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_no_image)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            if (articleXMediaModel?.image.isNullOrBlank())
                itemView.imageViewArticle.visibility = GONE
            else {
                itemView.imageViewArticle.visibility = VISIBLE
                Glide.with(itemView.context)
                    .load(articleXMediaModel?.image)
                    .apply(requestOption)
                    .into(itemView.imageViewArticle)
            }
            itemView.textViewUserName.text =
                "${articleXUserModel?.name} ${articleXUserModel?.lastname}"
            itemView.textViewUserDesignation.text = articleXUserModel?.designation
            itemView.textViewDateTime.text =
                articleDateTime(itemView.context, articleXModel.createdAt)

            if (articleXMediaModel?.title.isNullOrBlank())
                itemView.textViewTitle.visibility = GONE
            else {
                itemView.textViewTitle.visibility = VISIBLE
                itemView.textViewTitle.text = articleXMediaModel?.title
            }
            itemView.textViewLikes.text = articleXModel.likes.getFormatedValue(
                itemView.context,
                itemView.context.getString(R.string.likes)
            )
            itemView.textViewComments.text = articleXModel.comments.getFormatedValue(
                itemView.context,
                itemView.context.getString(R.string.comments)
            )
            if (articleXModel.content.isBlank())
                itemView.textViewContent.visibility = GONE
            else {
                itemView.textViewContent.visibility = VISIBLE
                itemView.textViewContent.text = articleXModel.content
            }
            if (articleXMediaModel?.url.isNullOrBlank())
                itemView.textViewUrl.visibility = GONE
            else {
                itemView.textViewUrl.visibility = VISIBLE
                itemView.textViewUrl.text = articleXMediaModel?.url
            }

        }

    }

}