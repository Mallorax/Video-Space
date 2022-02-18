package pl.patrykzygo.videospace.ui.movies_list

import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
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

@BindingAdapter("movieVoteCount")
fun bindVoteCount(textView: TextView, voteCount: Int?) {
    textView.text = getVoteCountString(voteCount)
}

@BindingAdapter("movieDescription")
fun bindMovieDescription(textView: TextView, description: String?){
    if (description != null){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
        textView.text = description
    }
}

fun getAverageVoteString(voteAverage: Double?): String {
    val voteValue = "%.${2}f".format(voteAverage).toDouble()
    return voteValue.toString()
}



fun getVoteCountString(voteCount: Int?): String{
    return voteCount.toString()
}

