package com.st.stmvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st.stmvi.state.AsyncTask
import com.st.stmvi.state.LoadState
import com.st.stmvi.state.UiEvent
import com.st.stmvi.state.UiState
import com.st.stmvi.store.Executor
import com.st.stmvi.store.UiStore
import com.st.stmvi.store.uiStore
import kotlinx.coroutines.launch

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


    protected fun AsyncTask.error(msg: String, e: Throwable? = null) {
        loadState.value = LoadState.Error(msg, e)
    }

    protected fun AsyncTask.idle() {
        loadState.value = LoadState.Idle
    }

    protected fun AsyncTask.loading() {
        loadState.value = LoadState.Loading
    }

    protected fun AsyncTask.finish() {
        loadState.value = LoadState.Finish
    }

    protected fun AsyncTask.exec(
        errorMsg: (e: Throwable) -> String = { e -> e.message.orEmpty().ifEmpty { "执行异常" } },
        executor: suspend (S) -> Unit,
    ) {
        viewModelScope.launch {
            loading()
            try {
                executor(uiStore.state)
                finish()
            } catch (e: Exception) {
                error(errorMsg(e), e)
            }
        }
    }
}