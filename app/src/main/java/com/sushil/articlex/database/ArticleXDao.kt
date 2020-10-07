package com.sushil.articlex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleXDao {

    @Query("SELECT Count(*) FROM article")
    fun getCount(): Int

    @Query("SELECT articles FROM article WHERE id = :pageNumber")
    fun getArticleListByPageNumber(pageNumber: Int): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article)
}