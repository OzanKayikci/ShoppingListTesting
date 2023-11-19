package com.laivinieks.shoppinglisttesting.other


data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    val shortMessage = when(status) {
        Status.SUCCESS ->"success"
        Status.ERROR -> "error"
        Status.LOADING -> "loading"
        else -> "unknown response"
    }
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}