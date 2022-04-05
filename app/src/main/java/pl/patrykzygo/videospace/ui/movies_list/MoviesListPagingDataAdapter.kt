package pl.patrykzygo.videospace.ui.movies_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import pl.patrykzygo.videospace.data.app.SimpleMovie
import pl.patrykzygo.videospace.databinding.MovieListItemBindingImpl
import pl.patrykzygo.videospace.ui.view_holders.MovieListItemViewHolder

class MoviesListPagingDataAdapter(private val onMovieClickListener: OnMovieClickListener) :
    PagingDataAdapter<SimpleMovie, MovieListItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holderListItem: MovieListItemViewHolder, position: Int) {
        val movie = getItem(position)
        val binding = holderListItem.binding
        binding.root.setOnClickListener {
            onMovieClickListener.onMovieClick(movie, it)
        }
        movie?.let {
            holderListItem.bind(movie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieListItemBindingImpl.inflate(inflater, parent, false)
        return MovieListItemViewHolder(binding)
    }


    companion object DiffCallback : DiffUtil.ItemCallback<SimpleMovie>() {
        override fun areItemsTheSame(oldItem: SimpleMovie, newItem: SimpleMovie): Boolean {
            return oldItem.movieId == newItem.movieId
        }

        override fun areContentsTheSame(oldItem: SimpleMovie, newItem: SimpleMovie): Boolean {
            return oldItem == newItem
        }
    }

    class OnMovieClickListener(val clickListener: (movie: SimpleMovie?, view: View) -> Unit) {
        fun onMovieClick(movie: SimpleMovie?, view: View) = clickListener(movie, view)
    }
}