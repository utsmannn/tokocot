package com.utsman.tokocot.event

import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Suppress("FunctionName")
fun <T> DefaultEventFlow(): MutableStateFlow<StateEvent<T>> =
    MutableStateFlow(StateEvent.Idle())

@Suppress("FunctionName")
fun <T> LoadingEventFlow(): MutableStateFlow<StateEvent<T>> =
    MutableStateFlow(StateEvent.Loading())

@Suppress("FunctionName")
fun <T> EmptyEventFlow(): MutableStateFlow<StateEvent<T>> =
    MutableStateFlow(StateEvent.Empty())

@Suppress("FunctionName")
fun <T> ErrorEventFlow(message: String?): MutableStateFlow<StateEvent<T>> =
    MutableStateFlow(StateEvent.Failure(Throwable(message)))

inline fun <T, U> StateEvent<T>.map(mapper: (T) -> U): StateEvent<U> {
    return when (this) {
        is StateEvent.Idle -> StateEvent.Idle()
        is StateEvent.Loading -> StateEvent.Loading()
        is StateEvent.Failure -> StateEvent.Failure(exception)
        is StateEvent.Success -> StateEvent.Success(mapper(data))
        is StateEvent.Empty -> StateEvent.Empty()
    }
}

inline fun <reified T> StateEvent<T>.doOnIdle(action: () -> Unit): StateEvent<T> {
    if (this is StateEvent.Idle) {
        action.invoke()
    }
    return this
}

inline fun <reified T> StateEvent<T>.doOnLoading(action: () -> Unit): StateEvent<T> {
    if (this is StateEvent.Loading) {
        action.invoke()
    }
    return this
}

inline fun <reified T> StateEvent<T>.doOnSuccess(data: (T) -> Unit): StateEvent<T> {
    if (this is StateEvent.Success) {
        data.invoke(this.data)
    }
    return this
}

inline fun <reified T> StateEvent<T>.doOnFailure(failure: (Throwable) -> Unit): StateEvent<T> {
    if (this is StateEvent.Failure) {
        failure.invoke(this.exception)
    }
    return this
}

inline fun <reified T> StateEvent<T>.doOnEmpty(failure: () -> Unit): StateEvent<T> {
    if (this is StateEvent.Empty) {
        failure.invoke()
    }
    return this
}

suspend inline fun <reified T>Response<T>.reduce(): StateEvent<T> {
    return suspendCoroutine { task ->
        val emitData: StateEvent<T> = try {
            val body = body()
            if (isSuccessful && body != null) {
                if (body is List<*> && body.isEmpty()) {
                    StateEvent.Empty()
                } else {
                    StateEvent.Success(body)
                }
            } else {
                val throwable = StateApiException(message(), code())
                StateEvent.Failure(throwable)
            }
        } catch (e: Throwable) {
            StateEvent.Failure(e)
        }

        task.resume(emitData)
    }
}
