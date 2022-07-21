@file:JvmName("UiStoreComposeExtKt")
@file:JvmMultifileClass

package com.st.stmvi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.st.stmvi.state.UiState
import com.st.stmvi.store.UiStore
import com.st.stmvi.viewmodel.MviViewModel


@Composable
fun <S : UiState> UiStore<S, *>.state(): State<S> {
    return stateFlow.collectAsState()
}


@Composable
fun <S : UiState> MviViewModel<S, *>.state(): State<S> {
    return uiStore.state()
}
