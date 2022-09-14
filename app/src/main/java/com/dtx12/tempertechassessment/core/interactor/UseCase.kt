package com.dtx12.tempertechassessment.core.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<out Type, in Params> {

    protected abstract suspend fun run(params: Params): Type

    suspend fun execute(params: Params): Type {
        return withContext(Dispatchers.IO) {
            run(params)
        }
    }

    object None
}