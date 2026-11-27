package com.example.phonicsgarden.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET

/**
 * Word content packs are plain JSON files hosted in this project's GitHub repository
 * (`content/word_pack.json`), so the content is curated by us and no personal data is sent.
 */
interface WordPackApi {
    @GET("wintwah-lwin/phonicsgarden/main/content/word_pack.json")
    suspend fun wordPack(): WordPackDto

    companion object {
        const val BASE_URL = "https://raw.githubusercontent.com/"
    }
}

@Serializable
data class WordPackDto(
    val version: Int = 0,
    val words: List<PackWordDto> = emptyList()
)

@Serializable
data class PackWordDto(
    val text: String,
    val emoji: String
)
