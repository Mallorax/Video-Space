package pl.patrykzygo.videospace.repository.delegate

class MovieCalcNextKeyDelegateImpl : MovieCalcKeyPositionDelegate {

    override fun nextKey(currKey: Int, lastKey: Int): Int? {
        return if (currKey == lastKey) {
            null
        } else {
            currKey + 1
        }
    }

    override fun previousKey(currKey: Int): Int? {
        return if (currKey == 1) {
            null
        } else {
            currKey - 1
        }
    }
}