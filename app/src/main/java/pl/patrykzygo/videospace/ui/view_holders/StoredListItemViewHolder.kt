package pl.patrykzygo.videospace.ui.view_holders

import androidx.recyclerview.widget.RecyclerView
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.databinding.StoredListMovieItemBinding

class StoredListItemViewHolder(val binding: StoredListMovieItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.movie = movie
        binding.executePendingBindings()
    }
}