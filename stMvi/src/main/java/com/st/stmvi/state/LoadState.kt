package com.st.stmvi.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


sealed class LoadState(
    @Stable
    val loading: Boolean = false,
    @Stable
    val completed: Boolean = false,
    @Stable
    val isError: Boolean = false,
) {
    object Idle : LoadState()
    object Loading : LoadState(loading = true)
    object Finish : LoadState(completed = true)
    data class Error(val msg: String, val e: Throwable? = null) :
        LoadState(completed = true, isError = true)
}

class AsyncTask {
    internal var loadState: MutableStateFlow<LoadState> = MutableStateFlow(LoadState.Idle)

    val loading get() = loadState.value.loading
    val completed get() = loadState.value.completed

    val isError get() = loadState.value.isError

    suspend fun collect(
        listen: LoadStateListener.() -> Unit = {},
    ) {
        val listener = LoadStateListener().apply { listen() }
        loadState.collectLatest {
            when (it) {
                is LoadState.Error -> listener.onError?.invoke(it.msg, it.e)
                LoadState.Finish -> listener.onFinish?.invoke()
                LoadState.Idle -> listener.onIdle?.invoke()
                LoadState.Loading -> listener.onLoading?.invoke()
            }
        }
    }

    @Composable
    fun collectLoadState(
        listen: LoadStateListener.() -> Unit = {},
    ): State<LoadState> {
        val stateListen by rememberUpdatedState(newValue = listen)
        val listener by remember { mutableStateOf(LoadStateListener().apply { stateListen() }) }
        LaunchedEffect(stateListen) {
            loadState.collectLatest {
                when (it) {
                    is LoadState.Error -> listener.onError?.invoke(it.msg, it.e)
                    LoadState.Finish -> listener.onFinish?.invoke()
                    LoadState.Idle -> listener.onIdle?.invoke()
                    LoadState.Loading -> listener.onLoading?.invoke()
                }
            }
        }
        return loadState.collectAsState()
    }
}

class LoadStateListener {
    internal var onIdle: (suspend () -> Unit)? = null
        private set
    internal var onLoading: (suspend () -> Unit)? = null
        private set
    internal var onError: (suspend (msg: String, e: Throwable?) -> Unit)? = null
        private set
    internal var onFinish: (suspend () -> Unit)? = null
        private set

    fun onIdle(block: suspend () -> Unit) {
        onIdle = block
    }

    fun onLoading(block: suspend () -> Unit) {
        onLoading = block
    }

    fun onError(block: suspend (msg: String, e: Throwable?) -> Unit) {
        onError = block
    }

    fun onFinish(block: suspend () -> Unit) {
        onFinish = block
    }
}