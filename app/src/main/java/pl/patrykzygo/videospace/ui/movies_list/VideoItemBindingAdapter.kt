package pl.patrykzygo.videospace.ui.movies_list

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.others.Constants


@BindingAdapter("moviePoster")
fun bindMoviePoster(imageView: ImageView, posterPath: String) {
    if (posterPath.isNotEmpty()) {
        Glide.with(imageView)
            .load(Constants.POSTERS_BASE_URL + posterPath)
            .error(R.drawable.ic_baseline_no_photography)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

}

@BindingAdapter("averageVote")
fun bindAverageVote(textView: TextView, voteAverage: Double) {
    val voteValue = "%.${2}f".format(voteAverage).toDouble()
    val averageVote = "Average vote: $voteValue"
    textView.text = averageVote
}

@BindingAdapter("movieVoteCount")
fun bindVoteCount(textView: TextView, voteCount: Int) {
    val votes = "Vote count: $voteCount"
    textView.text = votes
}
