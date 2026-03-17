package com.example.gameapp.Domain.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity("last_games")
data class LastGamesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val name: String,
    val released: String,
    val background_image: String,
    val rating: Double
)