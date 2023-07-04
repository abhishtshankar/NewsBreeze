package com.abhisht28.newsfeed

import androidx.room.*

@Entity(tableName = "saved_articles")
data class SavedArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: String,
    val description: String
)

@Dao
interface SavedArticleDao {
    @Query("SELECT * FROM saved_articles")
    suspend fun getAllSavedArticles(): List<SavedArticle>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSavedArticle(savedArticle: SavedArticle)
}
