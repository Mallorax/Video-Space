package pl.patrykzygo.videospace

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider

@ExperimentalCoroutinesApi
class TestDispatcherProvider : DispatchersProvider {

    override val main: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val io: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val default: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val unconfined: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
}