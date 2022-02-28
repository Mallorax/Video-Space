package pl.patrykzygo.videospace.repository.delegate

interface MovieCalcKeyPositionDelegate {

    fun nextKey(currKey: Int, lastKey: Int): Int?
    fun previousKey(currKey: Int): Int?
}