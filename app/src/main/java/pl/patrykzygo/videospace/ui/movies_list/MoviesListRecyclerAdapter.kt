package pl.patrykzygo.videospace.ui.movies_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.databinding.MovieItemBinding
import pl.patrykzygo.videospace.databinding.MovieItemBindingImpl

class MoviesListRecyclerAdapter(private val onMovieClickListener: OnMovieClickListener) :
    PagingDataAdapter<Movie, MoviesListRecyclerAdapter.MovieItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val movie = getItem(position)
        val binding = holder.binding
        binding.imageViewMore.setOnClickListener {
            onMovieClickListener.onMovieClick(movie, it)
        }
        if (movie != null) {
            holder.bind(movie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBindingImpl.inflate(inflater, parent, false)
        return MovieItemViewHolder(binding)
    }

    class MovieItemViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movie = movie
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.originalTitle == newItem.originalTitle
        }
    }

    class OnMovieClickListener(val clickListener: (movie: Movie?, view: View) -> Unit) {
        fun onMovieClick(movie: Movie?, view: View) = clickListener(movie, view)
    }
}