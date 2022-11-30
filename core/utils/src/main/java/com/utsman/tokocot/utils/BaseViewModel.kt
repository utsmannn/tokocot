package com.utsman.tokocot.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus

abstract class BaseViewModel : ViewModel() {
    abstract fun postError(throwable: Throwable)

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        postError(throwable)
    }

    protected val safeScope: CoroutineScope
        get() = viewModelScope + errorHandler
}