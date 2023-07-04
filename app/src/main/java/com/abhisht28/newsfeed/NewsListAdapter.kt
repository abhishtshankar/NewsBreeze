package com.abhisht28.newsfeed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsListAdapter(
    private val listener: NewsItemClicked,
    private val saveButtonClickListener: (News) -> Unit,
    private val readButtonClickListener: (News) -> Unit
) : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>(), Filterable {

    private var newsList: ArrayList<News> = ArrayList()
    private var filteredNewsList: ArrayList<News> = ArrayList(newsList)


    init {
        filteredNewsList = newsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredNewsList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = filteredNewsList[position]

        holder.bind(currentItem)

    }

    fun updateNews(updatedNews: ArrayList<News>) {
        newsList = updatedNews
        filteredNewsList = newsList
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val authorView: TextView = itemView.findViewById(R.id.author)
        private val descriptionView: TextView = itemView.findViewById(R.id.description)
        private val publishedAtView: TextView = itemView.findViewById(R.id.publishedAt)
        private val imageView: ImageView = itemView.findViewById(R.id.image)
        private val saveButton: Button = itemView.findViewById(R.id.saveButton)
        private val readButton: Button = itemView.findViewById(R.id.readButton)

        fun bind(newsItem: News) {
            titleView.text = newsItem.title
            authorView.text = newsItem.author
            descriptionView.text = newsItem.description
            publishedAtView.text = newsItem.publishedAt

            Glide.with(itemView).load(newsItem.imageUrl).into(imageView)

            saveButton.setOnClickListener {
                saveButtonClickListener.invoke(newsItem)
            }

            readButton.setOnClickListener {
                readButtonClickListener.invoke(newsItem)
            }

            itemView.setOnClickListener {
                listener.onItemClicked(newsItem)
            }
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.toLowerCase()
                val filteredList = ArrayList<News>()

                if (query.isNullOrBlank()) {
                    filteredList.addAll(newsList)
                } else {
                    for (news in newsList) {
                        if (news.title.toLowerCase().contains(query)) {
                            filteredList.add(news)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredNewsList = results?.values as? ArrayList<News> ?: ArrayList()
                notifyDataSetChanged()
            }
        }
    }

    interface NewsItemClicked {
        fun onItemClicked(item: News)
        fun onReadButtonClicked(item: News)
    }
}
