package com.example.gameapp.Domain.Models

data class GenreResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Genre>?
)
