@file:JvmName("UiStoreComposeExtKt")
@file:JvmMultifileClass

package com.st.stmvi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.st.stmvi.state.UiEvent
import com.st.stmvi.store.UiStore
import com.st.stmvi.viewmodel.MviViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun <E : UiEvent> UiStore<*, E>.Event(action: suspend (E) -> Unit) {
    LaunchedEffect(Unit) {
        eventFlow.onEach(action).launchIn(this)
    }
}


@Composable
fun <E : UiEvent> MviViewModel<*, E>.Event(action: suspend (E) -> Unit) {
    uiStore.Event(action)
}