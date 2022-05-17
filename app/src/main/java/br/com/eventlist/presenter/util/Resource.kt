package br.com.eventlist.presenter.util

data class Resource<T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null,
) {
    companion object {

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }
    }
}