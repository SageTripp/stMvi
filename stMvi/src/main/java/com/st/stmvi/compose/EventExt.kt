@file:JvmName("UiStoreComposeExtKt")
@file:JvmMultifileClass

package com.st.stmvi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.st.stmvi.state.UiEvent
import com.st.stmvi.store.UiStore
import com.st.stmvi.viewmodel.MviViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <E : UiEvent> UiStore<*, E>.Event(cancelLast: Boolean = false, action: suspend (E) -> Unit) {
    val eventInvoker by rememberUpdatedState(action)
    LaunchedEffect(Unit) {
        if (cancelLast) {
            eventFlow.collectLatest(action)
        } else {
            eventFlow.collect(action)
        }
    }
}


@Composable
fun <E : UiEvent> MviViewModel<*, E>.Event(
    cancelLast: Boolean = false,
    action: suspend (E) -> Unit
) {
    uiStore.Event(cancelLast, action)
}