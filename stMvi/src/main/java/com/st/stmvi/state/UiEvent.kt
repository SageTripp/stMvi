package com.st.stmvi.state

interface UiEvent

sealed class LoadState(
    val loading: Boolean = false,
    val completed: Boolean = false,
) {
    object Loading : LoadState(loading = true, completed = false)
    object Finish : LoadState(loading = false, completed = true)
    data class Error(val msg: String, val e: Throwable? = null) :
        LoadState(loading = false, completed = true)
}

interface UiLoading {
    val loadState: LoadState
}