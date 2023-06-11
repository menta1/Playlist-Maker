package com.example.playlistmaker.util

sealed class Resource<T>(val data: T? = null, val message: Boolean? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: Boolean, data: T? = null): Resource<T>(data, message)
}