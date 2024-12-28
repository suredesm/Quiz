package com.demo.app.quiz.compose.quiz

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.app.quiz.compose.components.Question
import com.demo.app.quiz.ui.theme.QuizTheme
import com.demo.app.quiz.viewmodels.QuizViewModel

@SuppressLint("DefaultLocale")
@Composable
fun QuizScreen(
    navigateToResult: (Int, Int) -> Unit,
    popBack: () -> Unit,
    vm: QuizViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsState()

    BackHandler { vm.setShowBackConfirmationDialog(true) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = "Q${String.format("%02d", uiState.questionNo)}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.weight(2F))

                Question(
                    onOptionSelect = vm::selectOptionForCurrentQuestion,
                    question = uiState.currentQuestion.question,
                    options = uiState.currentQuestion.options,
                    selectedIndex = uiState.currentQuestion.selectedIndex,
                )

                Spacer(Modifier.weight(2F))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(
                            onClick = vm::prevQuestion,
                            modifier = Modifier.weight(1F),
                            shape = RoundedCornerShape(8.dp),
                            enabled = uiState.hasPrevQuestion,
                        ) {
                            Text("Prev")
                        }

                        OutlinedButton(
                            onClick = vm::nextQuestion,
                            modifier = Modifier.weight(1F),
                            shape = RoundedCornerShape(8.dp),
                            enabled = uiState.hasNextQuestion,
                        ) {
                            Text("Next")
                        }
                    }

                    Button(
                        onClick = {
                            vm.setShowSubmitConfirmationDialog(show = true)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Submit")
                    }
                }

                Spacer(Modifier.height(20.dp))
            }
        }

        if (uiState.showSubmitConfirmation) {
            AlertDialog(
                onDismissRequest = { vm.setShowSubmitConfirmationDialog(false) },
                title = { Text(text = "Submit") },
                text = { Text(text = "Are you sure you want to submit?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            vm.setShowSubmitConfirmationDialog(false)
                            vm.evaluateQuiz(navigateToResult)
                        },
                    ) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { vm.setShowSubmitConfirmationDialog(false) },
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (uiState.showBackConfirmation) {
            AlertDialog(
                onDismissRequest = { vm.setShowBackConfirmationDialog(false) },
                title = { Text(text = "Go Back") },
                text = { Text(text = "Are you sure you want to go back?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            vm.setShowBackConfirmationDialog(false)
                            popBack()
                        },
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { vm.setShowBackConfirmationDialog(false) },
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizScreenPreview() {
    QuizTheme {
        QuizScreen({_,_ ->}, {})
    }
}