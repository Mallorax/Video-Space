package pl.patrykzygo.videospace.ui.movies_gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.databinding.MovieGalleryItemBindingImpl
import pl.patrykzygo.videospace.ui.view_holders.GalleryItemViewHolder

class MoviesGalleryRecyclerAdapter(private val onMovieClickListener: OnMovieClickListener) :
    PagingDataAdapter<Movie, GalleryItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) {
        val movie = getItem(position)
        val binding = holder.binding
        binding.imageViewMore.setOnClickListener {
            onMovieClickListener.onMovieClick(movie, it)
        }
        if (movie != null) {
            holder.bind(movie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieGalleryItemBindingImpl.inflate(inflater, parent, false)
        return GalleryItemViewHolder(binding)
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