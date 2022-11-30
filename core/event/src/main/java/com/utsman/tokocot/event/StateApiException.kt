package com.utsman.tokocot.event

class StateApiException(message: String, private val code: Int) : Throwable(message) {
    fun code() = code
}