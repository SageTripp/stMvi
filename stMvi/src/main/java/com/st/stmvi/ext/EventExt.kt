//package com.st.stmvi.ext
//
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.lifecycleScope
//import com.st.stmvi.state.UiEvent
//import com.st.stmvi.store.Executor
//import com.st.stmvi.store.UiStore
//import com.st.stmvi.viewmodel.MviViewModel
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//
//context(LifecycleOwner)
//fun <E : UiEvent> UiStore<*, E>.onEvent(
//    action: suspend (E) -> Unit
//) {
//    eventFlow.onEach(action).launchIn(lifecycleScope)
//}
//
//context(ViewModel)
//var <E : UiEvent> Executor<*, E>.event: E?
//    get() = null
//    set(value) {
//        value?.run(::sendEvent)
//    }
//
//
//context(LifecycleOwner)
//fun <E : UiEvent> MviViewModel<*, E>.onEvent(
//    action: suspend (E) -> Unit
//) {
//    uiStore.onEvent(action)
//}