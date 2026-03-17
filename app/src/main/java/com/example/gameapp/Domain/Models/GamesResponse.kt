package com.example.gameapp.Domain.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class GamesResponse(
    val count: Int?,
    val description: String?,
    val filters: Filters?,
    val next: String?,
    val nofollow: Boolean?,
    val nofollow_collections: List<String>?,
    val noindex: Boolean?,
    val previous: String?,
    @SerializedName("results")
    val gameResults: List<GameResult>?,
    val seo_description: String?,
    val seo_h1: String?,
    val seo_keywords: String?,
    val seo_title: String?
)

data class Filters(
    val years: List<Year>?
)

data class GameResult(
    val added: Int?,
    val added_by_status: AddedByStatus?,
    val background_image: String?,
    val clip: Any?,
    val dominant_color: String?,
    val esrb_rating: EsrbRating?,
    val genres: List<Genre>?,
    val id: Int?,
    val metacritic: Int?,
    val name: String?,
    val parent_platforms: List<ParentPlatform>?,
    val platforms: List<PlatformX>?,
    val playtime: Int?,
    val rating: Double?,
    val rating_top: Int?,
    val ratings: List<Rating>?,
    val ratings_count: Int?,
    val released: String?,
    val reviews_count: Int?,
    val reviews_text_count: Int?,
    val saturated_color: String?,
    val short_screenshots: List<ShortScreenshot>?,
    val slug: String?,
    val stores: List<Store>?,
    val suggestions_count: Int?,
    val tags: List<Tag>?,
    val tba: Boolean?,
    val updated: String?,
    val user_game: Any?
)

data class Year(
    val count: Int?,
    val decade: Int?,
    val filter: String?,
    val from: Int?,
    val nofollow: Boolean?,
    val to: Int?,
    val years: List<YearX>?
)

data class YearX(
    val count: Int?,
    val nofollow: Boolean?,
    val year: Int?
)

data class AddedByStatus(
    val beaten: Int?,
    val dropped: Int?,
    val owned: Int?,
    val playing: Int?,
    val toplay: Int?,
    val yet: Int?
)

data class EsrbRating(
    val id: Int?,
    val name: String?,
    val slug: String?
)
@Entity("genre")
data class Genre(
    val games_count: Int?,
    @PrimaryKey
    val id: Int?,
    val image_background: String?,
    val name: String?,
    val slug: String?
)

data class ParentPlatform(
    val platform: Platform?
)

data class PlatformX(
    val platform: PlatformXX?,
    val released_at: String?,
    val requirements_en: RequirementsEn?,
    val requirements_ru: RequirementsRu?
)

data class Rating(
    val count: Int?,
    val id: Int?,
    val percent: Double?,
    val title: String?
)

data class ShortScreenshot(
    val id: Int?,
    val image: String?
)

data class Store(
    val id: Int?,
    val store: StoreX?
)

data class Tag(
    val games_count: Int?,
    val id: Int?,
    val image_background: String?,
    val language: String?,
    val name: String?,
    val slug: String?
)

data class Platform(
    val id: Int?,
    val name: String?,
    val slug: String?
)

data class PlatformXX(
    val games_count: Int?,
    val id: Int?,
    val image: String?,
    val image_background: String?,
    val name: String?,
    val slug: String?,
    val year_end: Any?,
    val year_start: Int?
)

data class RequirementsEn(
    val minimum: String?,
    val recommended: String?
)

data class RequirementsRu(
    val minimum: String?,
    val recommended: String?
)

data class StoreX(
    val domain: String?,
    val games_count: Int?,
    val id: Int?,
    val image_background: String?,
    val name: String?,
    val slug: String?
)