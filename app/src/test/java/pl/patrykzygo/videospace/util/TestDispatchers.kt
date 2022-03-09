package pl.patrykzygo.videospace.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import pl.patrykzygo.videospace.ui.DispatchersProvider

@ExperimentalCoroutinesApi
class TestDispatchers(private val dispatcher: TestDispatcher): DispatchersProvider{

    override val main: CoroutineDispatcher
        get() = dispatcher
    override val io: CoroutineDispatcher
        get() = dispatcher
    override val default: CoroutineDispatcher
        get() = dispatcher
    override val unconfined: CoroutineDispatcher
        get() = dispatcher
}