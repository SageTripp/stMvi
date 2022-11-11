package com.st.stmvi.viewmodel

import androidx.lifecycle.ViewModel
import com.st.stmvi.state.UiEvent
import com.st.stmvi.state.UiState
import com.st.stmvi.store.Executor
import com.st.stmvi.store.UiStore
import com.st.stmvi.store.uiStore

abstract class MviViewModel<S : UiState, E : UiEvent>(initializerState: S) : ViewModel() {
    protected val uiExecutor by uiStore<S, E>(initializerState)
    val uiStore: UiStore<S, E> = uiExecutor

    protected fun updateState(action: S.() -> S) = uiExecutor.updateState(action)
    protected inline fun withState(crossinline action: Executor<S, E>.(S) -> Unit) =
        uiExecutor.withState(action)


    protected fun sendEvent(event: E) = uiExecutor.sendEvent(event)


    protected inline fun <S : UiState, E : UiEvent> Executor<S, E>.withState(
        action: Executor<S, E>.(S) -> Unit
    ) {
        action(state)
    }

    protected val <S : UiState> UiStore<S, *>.state get() = stateFlow.value
}