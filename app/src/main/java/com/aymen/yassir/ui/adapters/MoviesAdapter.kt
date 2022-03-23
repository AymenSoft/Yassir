package com.aymen.yassir.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aymen.yassir.R
import com.aymen.yassir.databinding.ItemMoviesBinding
import com.aymen.yassir.models.MoviesItem
import com.aymen.yassir.utils.SMALL_POSTER_URL
import com.bumptech.glide.Glide

/**
 * show movies list as a recycler view
 * @author Aymen Masmoudi
 * */
@SuppressLint("NotifyDataSetChanged")
class MoviesAdapter(listener: ClickListener) : RecyclerView.Adapter<MoviesAdapter.MoviesHolder>(), Filterable {

    private lateinit var context: Context
    private var defList: ArrayList<MoviesItem>
    private var movies: ArrayList<MoviesItem>
    private val listener: ClickListener

    init {
        this.defList = ArrayList()
        this.movies = ArrayList()
        this.listener = listener
    }

    //set movies list and notify adapter to refresh
    fun setMovies(list: ArrayList<MoviesItem>){
        this.defList = ArrayList(list)
        this.movies = defList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.MoviesHolder {
        val binding = ItemMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        this.context = parent.context
        return MoviesHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesAdapter.MoviesHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(position)
        }
    }

    override fun getItemCount() = movies.size

    inner class MoviesHolder(private val binding: ItemMoviesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            val movie = movies[position]

            binding.tvName.text = movie.title
            binding.tvRelease.text = String.format(context.resources.getString(R.string.release), movie.releaseDate)
            binding.tvVote.text = String.format(context.resources.getString(R.string.vote), movie.voteAverage)

            val poster = "$SMALL_POSTER_URL${movie.posterPath}"

            Glide.with(context)
                .load(poster)
                .placeholder(R.drawable.ic_no_poster)
                .into(binding.imgPoster)

        }
    }

    //detect item click
    interface ClickListener {
        fun onItemClickListener(position: Int)
    }

    //filter movies list by movie name
    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = ArrayList<MoviesItem>()
                if (constraint!!.isNotEmpty()){
                    for (movie in defList){
                        if (movie.title.lowercase().contains(constraint.toString().lowercase())){
                            result.add(movie)
                        }
                    }
                } else {
                    result.addAll(defList)
                }
                val filterResult = FilterResults()
                filterResult.values = result
                filterResult.count = result.size
                return filterResult
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                movies = results!!.values as ArrayList<MoviesItem>
                notifyDataSetChanged()
            }
        }
    }

}