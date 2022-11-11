//package com.st.stmvi.ext
//
//import androidx.lifecycle.*
//import com.st.stmvi.state.UiEvent
//import com.st.stmvi.state.UiState
//import com.st.stmvi.store.Executor
//import com.st.stmvi.store.UiStore
//import com.st.stmvi.viewmodel.MviViewModel
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//
////
//context(LifecycleOwner)
//fun <S : UiState> Flow<S>.collectState(
//    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
//    action: suspend (S) -> Unit
//) {
//    flowWithLifecycle(lifecycle, minActiveState).onEach(action).launchIn(lifecycleScope)
//}
//
//context(LifecycleOwner)
//fun <S : UiState> UiStore<S, *>.collectState(
//    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
//    action: suspend (S) -> Unit
//) {
//    stateFlow.collectState(minActiveState, action)
//}
//
//context(ViewModel) inline fun <S : UiState, E : UiEvent> Executor<S, E>.withState(
//    action: Executor<S, E>.(S) -> Unit
//) {
//    action(state)
//}
//
//context(ViewModel) val <S : UiState> UiStore<S, *>.state get() = stateFlow.value
//context(LifecycleOwner) val <S : UiState> UiStore<S, *>.currentState get() = stateFlow.value
//
//
//context(LifecycleOwner)
//fun <S : UiState> MviViewModel<S, *>.collectState(
//    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
//    action: suspend (S) -> Unit
//) {
//    uiStore.collectState(minActiveState, action)
//}
