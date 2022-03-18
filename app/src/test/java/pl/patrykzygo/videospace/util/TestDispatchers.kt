package pl.patrykzygo.videospace.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider

@ExperimentalCoroutinesApi
class TestDispatchers(private val dispatcher: TestDispatcher) : DispatchersProvider {

    override val main: CoroutineDispatcher
        get() = dispatcher
    override val io: CoroutineDispatcher
        get() = dispatcher
    override val default: CoroutineDispatcher
        get() = dispatcher
    override val unconfined: CoroutineDispatcher
        get() = dispatcher
}