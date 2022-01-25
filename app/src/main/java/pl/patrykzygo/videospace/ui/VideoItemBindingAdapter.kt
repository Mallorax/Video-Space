package pl.patrykzygo.videospace.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("popularity")
fun bindPopularity(textView: TextView, popularity: Double) {
    textView.text = "Popularity: $popularity"
}