package com.abhisht28.newsfeed
//
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.view.Menu
//import android.view.View
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.SearchView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.android.volley.Request
//import com.android.volley.Response
//import com.android.volley.toolbox.JsonObjectRequest
//
//class MainActivity : AppCompatActivity(), NewsListAdapter.NewsItemClicked {
//
//    private lateinit var mAdapter: NewsListAdapter
//    private lateinit var webView: WebView
//    private var newsList: ArrayList<News> = ArrayList()
//    private var isWebViewVisible: Boolean = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
//        recycler.layoutManager = LinearLayoutManager(this)
//
//        mAdapter = NewsListAdapter(this, ::onSaveButtonClicked, ::onReadButtonClicked)
//        recycler.adapter = mAdapter
//
//        webView = findViewById(R.id.webView)
//        webView.webViewClient = object : WebViewClient() {
//            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                super.onPageStarted(view, url, favicon)
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//            }
//        }
//
//        fetchData()
//    }
//
//    override fun onBackPressed() {
//        if (isWebViewVisible) {
//            webView.visibility = View.GONE
//            isWebViewVisible = false
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    private fun fetchData() {
//        val url =
//            "https://newsapi.org/v2/top-headlines?country=in&apiKey=ab04d6baa4864a8982ab424144151d69"
//        val jsonObjectRequest = object : JsonObjectRequest(
//            Request.Method.GET, url,
//            null,
//            Response.Listener { response ->
//                val newsJsonArray = response.getJSONArray("articles")
//                val newsArray = ArrayList<News>()
//                for (i in 0 until newsJsonArray.length()) {
//                    val newsJsonObject = newsJsonArray.getJSONObject(i)
//                    val news = News(
//                        newsJsonObject.getString("title"),
//                        newsJsonObject.getString("author"),
//                        newsJsonObject.getString("url"),
//                        newsJsonObject.getString("urlToImage"),
//                        newsJsonObject.getString("publishedAt"),
//                        newsJsonObject.getString("description")
//                    )
//                    newsArray.add(news)
//                }
//                newsList = newsArray
//                mAdapter.updateNews(newsArray)
//            },
//            Response.ErrorListener { error ->
//                // Handle error
//            }
//        ) {
//            override fun getHeaders(): MutableMap<String, String> {
//                val headers = HashMap<String, String>()
//                headers["User-Agent"] = "Mozilla/5.0"
//                return headers
//            }
//        }
//
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
//    }
//
//    override fun onItemClicked(item: News) {
//        webView.visibility = View.VISIBLE
//        webView.loadUrl(item.url)
//        isWebViewVisible = true
//    }
//
//    private fun onSaveButtonClicked(item: News) {
//        item.isSaved = true
//    }
//
//    override fun onReadButtonClicked(item: News) {
//        webView.visibility = View.VISIBLE
//        webView.loadUrl(item.url)
//        isWebViewVisible = true
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//
//        val searchItem = menu?.findItem(R.id.action_search)
//        val searchView = searchItem?.actionView as SearchView
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                mAdapter.filter.filter(newText)
//                return true
//            }
//        })
//        searchView.setOnCloseListener {
//            mAdapter.filter.filter("")
//            mAdapter.updateNews(newsList)
//            true
//        }
//
//        return true
//    }
//}
//
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NewsListAdapter.NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter
    private lateinit var webView: WebView
    private lateinit var savedArticleDao: SavedArticleDao
    private var newsList: ArrayList<News> = ArrayList()
    private var isWebViewVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(this)

        mAdapter = NewsListAdapter(this, ::onSaveButtonClicked, ::onReadButtonClicked)
        recycler.adapter = mAdapter

        webView = findViewById(R.id.webView)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

        // Initialize Room database
        savedArticleDao = AppDatabase.getInstance(applicationContext).savedArticleDao()

        fetchData()
    }

    override fun onBackPressed() {
        if (isWebViewVisible) {
            webView.visibility = View.GONE
            isWebViewVisible = false
        } else {
            super.onBackPressed()
        }
    }

    private fun fetchData() {
        val url =
            "https://newsapi.org/v2/top-headlines?country=in&apiKey=ab04d6baa4864a8982ab424144151d69"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url,
            null,
            Response.Listener { response ->
                val newsJsonArray = response.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage"),
                        newsJsonObject.getString("publishedAt"),
                        newsJsonObject.getString("description")
                    )
                    newsArray.add(news)
                }
                newsList = newsArray
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener { error ->
                // Handle error
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        webView.visibility = View.VISIBLE
        webView.loadUrl(item.url)
        isWebViewVisible = true
    }

    private fun onSaveButtonClicked(item: News) {
        val savedArticle = SavedArticle(
            title = item.title,
            author = item.author,
            url = item.url,
            imageUrl = item.imageUrl,
            publishedAt = item.publishedAt,
            description = item.description
        )
        insertSavedArticle(savedArticle)
        showToast("Article saved")
    }

    private fun insertSavedArticle(savedArticle: SavedArticle) {
        // Run the insertion on a background thread using a coroutine
        GlobalScope.launch(Dispatchers.IO) {
            savedArticleDao.insertSavedArticle(savedArticle)
        }
    }

    override fun onReadButtonClicked(item: News) {
        webView.visibility = View.VISIBLE
        webView.loadUrl(item.url)
        isWebViewVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter.filter.filter(newText)
                return true
            }
        })
        searchView.setOnCloseListener {
            mAdapter.filter.filter("")
            mAdapter.updateNews(newsList)
            true
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

