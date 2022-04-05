package pl.patrykzygo.videospace.ui.stored_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.app.SimpleMovie
import pl.patrykzygo.videospace.databinding.StoredListMovieItemBinding
import pl.patrykzygo.videospace.ui.view_holders.StoredListItemViewHolder

class StoredListAdapter(private val onMovieClickListener: OnMovieClickListener) :
    ListAdapter<SimpleMovie, StoredListItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holderListItem: StoredListItemViewHolder, position: Int) {
        val movie = getItem(position)
        val binding = holderListItem.binding
        binding.root.setOnClickListener {
            onMovieClickListener.onMovieClick(movie, it)
        }
        movie?.let {
            holderListItem.bind(movie)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoredListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StoredListMovieItemBinding.inflate(inflater, parent, false)
        return StoredListItemViewHolder(binding)
    }


    companion object DiffCallback : DiffUtil.ItemCallback<SimpleMovie>() {
        override fun areItemsTheSame(oldItem: SimpleMovie, newItem: SimpleMovie): Boolean {
            return oldItem.movieId == newItem.movieId
        }

        override fun areContentsTheSame(oldItem: SimpleMovie, newItem: SimpleMovie): Boolean {
            return oldItem.title == newItem.title
        }
    }

    class OnMovieClickListener(val clickListener: (movie: SimpleMovie?, view: View) -> Unit) {
        fun onMovieClick(movie: SimpleMovie?, view: View) = clickListener(movie, view)
    }
}