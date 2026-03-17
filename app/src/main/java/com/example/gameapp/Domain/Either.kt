package com.example.gameapp.Domain

sealed class Either<out T, out E> {
    data class Left<T>(val data: T) : Either<T, Nothing>()
    data class Right<E>(val data2: E) : Either<Nothing, E>()
}