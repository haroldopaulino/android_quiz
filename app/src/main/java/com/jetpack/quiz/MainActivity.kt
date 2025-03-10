package com.jetpack.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.quiz.ui.theme.QuizTheme

class MainActivity : ComponentActivity() {
    lateinit var globalViewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    globalViewModel = viewModel()
                    QuizScreen(globalViewModel)
                }
            }
        }
    }
}

class QuizViewModel : ViewModel() {
    val questions = listOf(
        Question.TrueFalseQuestion("True or False: An Activity is destroyed during recomposition?", false),
        Question.MultipleChoiceSingle("Which component manages the app's UI?", listOf("Activity", "Service", "ContentProvider"), "Activity"),
        Question.MultipleChoiceMultiple("Select all that are Android UI elements.", listOf("TextView", "Service", "RecyclerView"), listOf("TextView", "RecyclerView")),
        Question.TextQuestion("What is the primary language used for Android development?")
    )

    var currentQuestionIndex by mutableIntStateOf(0)

    // State variables for each question type
    var selectedAnswerTF by mutableStateOf<Boolean?>(null)
    var selectedOptionMCS by mutableStateOf<String?>(null)
    var selectedOptionsMCM = mutableStateListOf<String>()
    var textAnswer by mutableStateOf("")

    fun nextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
        }
    }

    fun previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
        }
    }
}

sealed class Question {
    data class TrueFalseQuestion(val question: String, val correctAnswer: Boolean) : Question()
    data class MultipleChoiceSingle(val question: String, val options: List<String>, val correctAnswer: String) : Question()
    data class MultipleChoiceMultiple(val question: String, val options: List<String>, val correctAnswers: List<String>) : Question()
    data class TextQuestion(val question: String) : Question()
}

@Composable
fun QuizScreen(viewModel: QuizViewModel) {
    val isGoingForward = remember { mutableStateOf(true) }

    AnimatedContent(
        targetState = viewModel.currentQuestionIndex,
        transitionSpec = {
            if (isGoingForward.value) {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            } else {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }
        }, label = ""
    ) { targetQuestionIndex ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = viewModel.questions[targetQuestionIndex].toString(), style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            when (viewModel.questions[targetQuestionIndex]) {
                is Question.TrueFalseQuestion -> TrueFalseQuestionView(viewModel)
                is Question.MultipleChoiceSingle -> MultipleChoiceSingleView(viewModel.questions[targetQuestionIndex] as Question.MultipleChoiceSingle, viewModel)
                is Question.MultipleChoiceMultiple -> MultipleChoiceMultipleView(viewModel.questions[targetQuestionIndex] as Question.MultipleChoiceMultiple, viewModel)
                is Question.TextQuestion -> TextQuestionView(viewModel)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (viewModel.currentQuestionIndex > 0) {
                    Button(onClick = {
                        isGoingForward.value = false
                        viewModel.previousQuestion()
                    }) {
                        Text("Back")
                    }
                }

                if (viewModel.currentQuestionIndex < 3) {
                    Button(onClick = {
                        isGoingForward.value = true
                        viewModel.nextQuestion()
                    }) {
                        Text("Next")
                    }
                }
            }
        }
    }
}

@Composable
fun MultipleChoiceSingleView(question: Question.MultipleChoiceSingle, viewModel: QuizViewModel) {
    Column {
        question.options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectedOptionMCS = option }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = viewModel.selectedOptionMCS == option,
                    onClick = { viewModel.selectedOptionMCS = option }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
            }
        }
    }
}

@Composable
fun TrueFalseQuestionView(viewModel: QuizViewModel) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.selectedAnswerTF = true }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = viewModel.selectedAnswerTF == true,
                onClick = { viewModel.selectedAnswerTF = true }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "True")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.selectedAnswerTF = false }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = viewModel.selectedAnswerTF == false,
                onClick = { viewModel.selectedAnswerTF = false }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "False")
        }
    }
}

@Composable
fun MultipleChoiceMultipleView(question: Question.MultipleChoiceMultiple, viewModel: QuizViewModel) {
    Column {
        question.options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (viewModel.selectedOptionsMCM.contains(option)) {
                            viewModel.selectedOptionsMCM.remove(option)
                        } else {
                            viewModel.selectedOptionsMCM.add(option)
                        }
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = viewModel.selectedOptionsMCM.contains(option),
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            viewModel.selectedOptionsMCM.add(option)
                        } else {
                            viewModel.selectedOptionsMCM.remove(option)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
            }
        }
    }
}

@Composable
fun TextQuestionView(viewModel: QuizViewModel) {
    var text by remember { mutableStateOf(viewModel.textAnswer) }
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.textAnswer = it
            },
            label = { Text("Your Answer") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}