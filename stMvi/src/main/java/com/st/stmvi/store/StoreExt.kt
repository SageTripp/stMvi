package com.st.stmvi.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st.stmvi.state.UiEvent
import com.st.stmvi.state.UiState
import kotlinx.coroutines.CoroutineScope

class UiStoreLazy<S : UiState, E : UiEvent>(
    initializerState: S,
    scope: CoroutineScope,
) : Lazy<RealUiStore<S, E>> {
    private val store: RealUiStore<S, E>? = null

    override val value: RealUiStore<S, E> = store ?: RealUiStore(initializerState, scope)
    override fun isInitialized() = store != null
}

fun <S : UiState, E : UiEvent> ViewModel.uiStore(
    initializerState: S,
    scope: CoroutineScope = viewModelScope
): UiStoreLazy<S, E> = UiStoreLazy(initializerState, scope)