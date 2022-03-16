package pl.patrykzygo.videospace.ui.stored_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.databinding.MovieListItemBindingImpl
import pl.patrykzygo.videospace.ui.view_holders.MovieListItemViewHolder

class StoredListAdapter(private val onMovieClickListener: OnMovieClickListener) :
    ListAdapter<Movie, MovieListItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holderListItem: MovieListItemViewHolder, position: Int) {
        val movie = getItem(position)
        val binding = holderListItem.binding
        binding.root.setOnClickListener {
            onMovieClickListener.onMovieClick(movie, it)
        }
        if (movie != null) {
            holderListItem.bind(movie)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieListItemBindingImpl.inflate(inflater, parent, false)
        return MovieListItemViewHolder(binding)
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