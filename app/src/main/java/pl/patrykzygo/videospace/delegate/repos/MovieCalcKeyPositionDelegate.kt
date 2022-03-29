package pl.patrykzygo.videospace.delegate.repos

interface MovieCalcKeyPositionDelegate {

    fun nextKey(currKey: Int, lastKey: Int): Int?
    fun previousKey(currKey: Int): Int?
}