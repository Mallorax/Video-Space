package pl.patrykzygo.videospace.ui.view_holders

import androidx.recyclerview.widget.RecyclerView
import pl.patrykzygo.videospace.data.app.SimpleMovie
import pl.patrykzygo.videospace.databinding.MovieGalleryItemBinding

class GalleryItemViewHolder(val binding: MovieGalleryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: SimpleMovie) {
        binding.movie = movie
        binding.executePendingBindings()
    }
}