package com.example.phonicsgarden.data

import com.example.phonicsgarden.data.local.SeedData
import com.example.phonicsgarden.data.remote.WordPackDto
import com.example.phonicsgarden.engine.PackEntry
import com.example.phonicsgarden.engine.WordPackValidator
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.io.File

/**
 * Guards the real `content/word_pack.json` that the app downloads from GitHub: if someone edits
 * it badly, this test fails before the file is pushed.
 */
class ContentPackFileTest {

    // Gradle runs unit tests with the module folder (app/) as the working directory.
    private val file = File("../content/word_pack.json")

    private fun loadPack(): WordPackDto =
        Json { ignoreUnknownKeys = true }.decodeFromString(file.readText())

    @Test
    fun `the published word pack is valid JSON with at least one word`() {
        assumeTrue("content/word_pack.json not found", file.exists())
        val pack = loadPack()
        assertTrue(pack.version >= 1)
        assertTrue(pack.words.isNotEmpty())
    }

    @Test
    fun `every word in the published pack passes validation`() {
        assumeTrue("content/word_pack.json not found", file.exists())
        val pack = loadPack()
        val taught = SeedData.graphemes.associate { it.letter to it.phase }

        val result = WordPackValidator.validate(pack.words.map { PackEntry(it.text, it.emoji) }, taught)

        assertEquals("Some pack entries were rejected", 0, result.rejected)
        assertEquals(pack.words.size, result.accepted.size)
    }

    @Test
    fun `the published pack only adds new words`() {
        assumeTrue("content/word_pack.json not found", file.exists())
        val bundled = SeedData.words.map { it.text }.toSet()
        val duplicates = loadPack().words.map { it.text }.filter { it in bundled }
        assertTrue("Already bundled: $duplicates", duplicates.isEmpty())
    }
}
