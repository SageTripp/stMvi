package com.st.stmvi.store

import com.st.stmvi.state.UiEvent
import com.st.stmvi.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface UiStore<S : UiState, E : UiEvent> {
    val stateFlow: StateFlow<S>
    val eventFlow: SharedFlow<E>
}

interface Executor<S : UiState, E : UiEvent> : UiStore<S, E> {
    fun updateState(action: S.() -> S)
    fun sendEvent(event: E)
}

class RealUiStore<S : UiState, E : UiEvent>(
    initializerState: S,
    private val scope: CoroutineScope,
) : Executor<S, E> {

    private val _stateFlow = MutableStateFlow(initializerState)
    override val stateFlow: StateFlow<S> = _stateFlow.asStateFlow()
    private val _eventFlow = MutableSharedFlow<E>()
    override val eventFlow: SharedFlow<E> = _eventFlow.asSharedFlow()
    override fun updateState(action: S.() -> S) {
        _stateFlow.update(action)
    }

    override fun sendEvent(event: E) {
        scope.launch { _eventFlow.emit(event) }
    }
}

