package com.laivinieks.shoppinglisttesting.other



// we are using this to make our live data emit one-time events

/**
 * scenario:
 * after the request to Api we got error response and
 * we show a snackbar that an error occurred.
 * then if we rotate the device, the live data would automatically emit again
 * and the system will show error message again.
 * that is why we need event
 * */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}