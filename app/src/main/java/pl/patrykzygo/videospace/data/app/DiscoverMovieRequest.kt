package pl.patrykzygo.videospace.data.app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.lang.StringBuilder

@Parcelize
class DiscoverMovieRequest(
    val includedGenres: String? = null,
    val excludedGenres: String? = null,
    val minScore: Int? = null,
    val minVoteCount: Int? = null,
): Parcelable {

}