package pl.patrykzygo.videospace.ui.movies_list

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.others.Paths


@BindingAdapter("moviePoster")
fun bindMoviePoster(imageView: ImageView, posterPath: String?) {
    if (posterPath?.isNotEmpty() == true) {
        Glide.with(imageView)
            .load(Paths.POSTERS_BASE_URL + posterPath)
            .error(R.drawable.ic_baseline_no_photography)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

}

@BindingAdapter("averageVote")
fun bindAverageVote(textView: TextView, voteAverage: Double?) {
    textView.text = getAverageVoteString(voteAverage)
}

fun getAverageVoteString(voteAverage: Double?): String {
    val voteValue = "%.${2}f".format(voteAverage).toDouble()
    return "Average vote: $voteValue"
}

@BindingAdapter("movieVoteCount")
fun bindVoteCount(textView: TextView, voteCount: Int?) {
    textView.text = getVoteCountString(voteCount)
}

fun getVoteCountString(voteCount: Int?): String{
    return "Vote count: $voteCount"
}

