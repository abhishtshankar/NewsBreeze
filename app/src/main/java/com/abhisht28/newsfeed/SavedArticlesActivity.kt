////import android.view.LayoutInflater
////import android.view.View
////import android.view.ViewGroup
////import android.widget.TextView
////import androidx.recyclerview.widget.RecyclerView
////import com.abhisht28.newsfeed.Article
////import com.abhisht28.newsfeed.R
////
////class SavedArticlesAdapter : RecyclerView.Adapter<SavedArticlesAdapter.ViewHolder>() {
////
////    private var articles: List<Article> = emptyList()
////
////    fun updateArticles(newArticles: List<Article>) {
////        articles = newArticles
////        notifyDataSetChanged()
////    }
////
////    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
////        val view = LayoutInflater.from(parent.context)
////            .inflate(R.layout.item_saved_article, parent, false)
////        return ViewHolder(view)
////    }
////
////    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
////        val article = articles[position]
////        holder.titleTextView.text = article.title
////        holder.authorTextView.text = article.author
////    }
////
////    override fun getItemCount(): Int {
////        return articles.size
////    }
////
////    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
////        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
////        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
////    }
////}
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.abhisht28.newsfeed.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class SavedArticlesActivity : AppCompatActivity() {
//
//    private lateinit var adapter: SavedArticlesAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_saved_articles)
//
//        val recyclerView: RecyclerView = findViewById(R.id.savedArticlesRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = SavedArticlesAdapter()
//        recyclerView.adapter = adapter
//
//        loadSavedArticles()
//    }
//
//    private fun loadSavedArticles() {
//        GlobalScope.launch(Dispatchers.Main) {
//            val savedArticles = withContext(Dispatchers.IO) {
//                getAllSavedArticles()
//            }
//            adapter.updateArticles(savedArticles)
//        }
//    }
//
//    private suspend fun getAllSavedArticles(): List<SavedArticle> {
//        return withContext(Dispatchers.IO) {
//            AppDatabase.getInstance(this@SavedArticlesActivity)
//                .savedArticleDao()
//                .getAllSavedArticles()
//        }
//    }
//}
