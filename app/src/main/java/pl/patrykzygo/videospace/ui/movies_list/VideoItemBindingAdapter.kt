package pl.patrykzygo.videospace.ui.movies_list

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.others.Constants


@BindingAdapter("moviePoster")
fun bindMoviePoster(imageView: ImageView, posterPath: String) {
    if (posterPath.isNotEmpty()){
        Glide.with(imageView)
            .load(Constants.POSTERS_BASE_URL + posterPath)
            .error(R.drawable.ic_baseline_no_photography_24)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

}