package com.dtx12.tempertechassessment.core.extensions

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

fun CombinedLoadStates.getExceptionFromSource(): Throwable? {
    val refreshState = source.refresh
    if (refreshState is LoadState.Error) {
        return refreshState.error
    }
    val appendState = source.append
    if (appendState is LoadState.Error) {
        return appendState.error
    }
    val prependState = source.prepend
    if (prependState is LoadState.Error) {
        return prependState.error
    }
    return null
}