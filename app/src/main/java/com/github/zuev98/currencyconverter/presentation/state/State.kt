package com.github.zuev98.currencyconverter.presentation.state

data class State<T>(val responseStatus: ResponseStatus, val data: T?, val message: String?) {

    companion object {
        fun <T> onSuccess(data: T?): State<T> =
            State(ResponseStatus.SUCCESS, data, null)

        fun <T> onError(message: String?): State<T> =
            State(ResponseStatus.ERROR, null, message)
    }
}

enum class ResponseStatus {
    SUCCESS,
    ERROR
}
