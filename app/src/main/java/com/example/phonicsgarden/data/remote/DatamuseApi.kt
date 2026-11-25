package com.example.phonicsgarden.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Datamuse word-finding API (https://www.datamuse.com/api/).
 * Pattern example: sp=s?? returns three-letter words starting with "s".
 */
interface DatamuseApi {
    @GET("words")
    suspend fun words(
        @Query("sp") pattern: String,
        @Query("max") max: Int = 20
    ): List<WordDto>
}

@Serializable
data class WordDto(
    val word: String,
    val score: Int? = null
)
