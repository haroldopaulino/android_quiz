package com.jetpack.quiz

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testNextQuestionIncrementsIndex() {
        val viewModel = QuizViewModel()
        val initialIndex = viewModel.currentQuestionIndex
        viewModel.nextQuestion()
        assertEquals(initialIndex + 1, viewModel.currentQuestionIndex)
    }

    @Test
    fun testNextQuestionDoesNotExceedQuestionCount() {
        val viewModel = QuizViewModel()
        // Test going to the last question
        while (viewModel.currentQuestionIndex < viewModel.questions.size - 1) {
            viewModel.nextQuestion()
        }
        val lastIndex = viewModel.currentQuestionIndex
        viewModel.nextQuestion()
        assertEquals(lastIndex, viewModel.currentQuestionIndex)
    }

    @Test
    fun testPreviousQuestionDecrementsIndex() {
        val viewModel = QuizViewModel()
        // test going to the second question
        viewModel.nextQuestion()
        val initialIndex = viewModel.currentQuestionIndex
        viewModel.previousQuestion()
        assertEquals(initialIndex - 1, viewModel.currentQuestionIndex)
    }

    @Test
    fun testPreviousQuestionDoesNotGoBelowZero() {
        val viewModel = QuizViewModel()
        viewModel.previousQuestion()
        assertEquals(0, viewModel.currentQuestionIndex)
    }

    @Test
    fun testNextQuestionClearsAnswers() {
        val viewModel = QuizViewModel()
        // test setting more answers
        viewModel.selectedAnswerTF = true
        viewModel.selectedOptionMCS = "A test option"
        viewModel.selectedOptionsMCM.add("Another test option")
        viewModel.textAnswer = "Test Answer"

        viewModel.nextQuestion()

        assertNull(viewModel.selectedAnswerTF)
        assertNull(viewModel.selectedOptionMCS)
        assertTrue(viewModel.selectedOptionsMCM.isEmpty())
        assertEquals("", viewModel.textAnswer)
    }

    @Test
    fun testPreviousQuestionClearsAnswers() {
        val viewModel = QuizViewModel()
        // test going to the second question
        viewModel.nextQuestion()
        // test setting more answers
        viewModel.selectedAnswerTF = true
        viewModel.selectedOptionMCS = "Yet another test option"
        viewModel.selectedOptionsMCM.add("You got it, another option")
        viewModel.textAnswer = "This is the answer"
        // testing going to the previous question
        viewModel.previousQuestion()

        assertNull(viewModel.selectedAnswerTF)
        assertNull(viewModel.selectedOptionMCS)
        assertNull(viewModel.selectedOptionsMCM.isEmpty())
        assertEquals("", viewModel.textAnswer)
    }
}