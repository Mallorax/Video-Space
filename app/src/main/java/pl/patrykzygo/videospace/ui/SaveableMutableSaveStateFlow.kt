package pl.patrykzygo.videospace.ui

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SaveableMutableSaveStateFlow<T>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String,
    defaultValue: T
) {
    private val _state: MutableStateFlow<T> =
        MutableStateFlow(
            savedStateHandle.get<T>(key) ?: defaultValue)

    var value: T
        get() = _state.value
        set(value) {
            _state.value = value
            savedStateHandle.set(key, value)
        }

    fun asStateFlow(): StateFlow<T> = _state
}