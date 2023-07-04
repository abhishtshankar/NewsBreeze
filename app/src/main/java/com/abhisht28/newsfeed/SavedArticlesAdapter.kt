package com.abhisht28.newsfeed
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//class SavedArticlesAdapter : RecyclerView.Adapter<SavedArticlesAdapter.ArticleViewHolder>() {
//
//    private val articles: MutableList<Article> = mutableListOf()
//
//    fun updateArticles(newArticles: List<Article>) {
//        articles.clear()
//        articles.addAll(newArticles)
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_saved_article, parent, false)
//        return ArticleViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
//        val article = articles[position]
//        holder.bind(article)
//    }
//
//    override fun getItemCount(): Int {
//        return articles.size
//    }
//
//    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
//        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
//
//        fun bind(article: Article) {
//            titleTextView.text = article.title
//            authorTextView.text = article.author
//        }
//    }
//}
