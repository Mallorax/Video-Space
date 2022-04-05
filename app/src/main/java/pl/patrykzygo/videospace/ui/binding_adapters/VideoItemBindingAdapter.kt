package pl.patrykzygo.videospace.ui.binding_adapters

import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.constants.MovieStatus
import pl.patrykzygo.videospace.constants.Paths
import pl.patrykzygo.videospace.data.app.SimpleMovie


@BindingAdapter("moviePoster")
fun bindMoviePoster(imageView: ImageView, posterPath: String?) {
    if (posterPath?.isNotEmpty() == true) {
        val path = Paths.POSTERS_BASE_URL + posterPath
        Glide.with(imageView)
            .load(path)
            .error(R.drawable.ic_baseline_no_photography)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}


@BindingAdapter("averageVote")
fun bindAverageVote(textView: TextView, voteAverage: Double?) {
    textView.text = getAverageVoteString(voteAverage)
}

@BindingAdapter("movieVoteCount")
fun bindVoteCount(textView: TextView, voteCount: Int?) {
    textView.text = getVoteCountString(voteCount)
}

@BindingAdapter("movieDescription")
fun bindMovieDescription(textView: TextView, description: String?) {
    if (description != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            textView.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
        textView.text = description
    }
}

@BindingAdapter("releaseDate")
fun bindReleaseDate(textView: TextView, releaseDate: String) {
    val text = "Release date: $releaseDate"
    textView.text = text
}

@BindingAdapter("userScore")
fun bindUserScore(textView: TextView, movie: SimpleMovie) {
    if (movie.status != MovieStatus.COMPLETED) {
        textView.visibility = View.INVISIBLE
    } else {
        textView.visibility = View.VISIBLE
        val text = "${movie.userScore}/10"
        textView.text = text
    }
}

@BindingAdapter("toggleScoreLabel")
fun bindToggleScoreLabel(textView: TextView, movie: SimpleMovie) {
    if (movie.status != MovieStatus.COMPLETED) {
        textView.visibility = View.INVISIBLE
    } else {
        textView.visibility = View.VISIBLE
    }
}

fun getAverageVoteString(voteAverage: Double?): String {
    val voteValue = "%.${2}f".format(voteAverage).toDouble()
    return voteValue.toString()
}

fun getVoteCountString(voteCount: Int?): String {
    return voteCount.toString()
}

