package com.jetpack.quiz

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performTextInput
import com.jetpack.quiz.ui.theme.QuizTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = composeTestRule.activity.applicationContext
        assertEquals("com.example.quiz", appContext.packageName)
    }

    @Test
    fun testInitialQuestionDisplayed() {
        composeTestRule.setContent {
            val viewModel: QuizViewModel = viewModel()
            QuizTheme {
                QuizScreen(viewModel)
            }
        }
        composeTestRule.onNodeWithText("True or False: An Activity is destroyed during recomposition?").assertExists()
    }

    @Test
    fun testNextButtonNavigatesToNextQuestion() {
        composeTestRule.setContent {
            val viewModel: QuizViewModel = viewModel()
            QuizTheme {
                QuizScreen(viewModel)
            }
        }
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Which component manages the app's UI?").assertExists()
    }

    @Test
    fun testBackButtonNavigatesToPreviousQuestion() {
        composeTestRule.setContent {
            val viewModel: QuizViewModel = viewModel()
            QuizTheme {
                QuizScreen(viewModel)
            }
        }
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Back").performClick()
        composeTestRule.onNodeWithText("True or False: An Activity is destroyed during recomposition?").assertExists()
    }

    @Test
    fun testTrueFalseQuestionAnswer() {
        composeTestRule.setContent {
            val viewModel: QuizViewModel = viewModel()
            QuizTheme {
                QuizScreen(viewModel)
            }
        }
        composeTestRule.onNodeWithText("True").performClick()
        assertTrue(composeTestRule.activity.globalViewModel.selectedAnswerTF == true)
    }

    @Test
    fun testMultipleChoiceSingleQuestionAnswer() {
        composeTestRule.setContent {
            val viewModel: QuizViewModel = viewModel()
            QuizTheme {
                QuizScreen(viewModel)
            }
        }
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Activity").performClick()
        assertTrue(composeTestRule.activity.globalViewModel.selectedOptionMCS == "Activity")
    }

    @Test
    fun testMultipleChoiceMultipleQuestionAnswer() {
        composeTestRule.setContent {
            val viewModel: QuizViewModel = viewModel()
            QuizTheme {
                QuizScreen(viewModel)
            }
        }
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("TextView").performClick()
        assertTrue(composeTestRule.activity.globalViewModel.selectedOptionsMCM.contains("TextView"))
    }

    @Test
    fun testTextQuestionAnswer() {
        composeTestRule.setContent {
            val viewModel: QuizViewModel = viewModel()
            QuizTheme {
                QuizScreen(viewModel)
            }
        }
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Your Answer").performClick()
        composeTestRule.onNodeWithText("Your Answer").performTextInput("Kotlin")
        assertTrue(composeTestRule.activity.globalViewModel.textAnswer == "Kotlin")
    }
}