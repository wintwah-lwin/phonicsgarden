package com.example.phonicsgarden.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class ParentGateValidatorTest {

    @Test
    fun generateQuestion_calculatesCorrectAnswer() {
        val question = ParentGateValidator.generateQuestion(Random(42))
        assertEquals(question.num1 + question.num2, question.answer)
        assertTrue(question.prompt.contains("${question.num1} + ${question.num2}"))
    }

    @Test
    fun validateAnswer_correctInput_returnsTrue() {
        assertTrue(ParentGateValidator.validateAnswer("12", 12))
        assertTrue(ParentGateValidator.validateAnswer("  15 ", 15))
    }

    @Test
    fun validateAnswer_incorrectOrInvalidInput_returnsFalse() {
        assertFalse(ParentGateValidator.validateAnswer("10", 12))
        assertFalse(ParentGateValidator.validateAnswer("abc", 12))
        assertFalse(ParentGateValidator.validateAnswer("", 12))
    }
}
