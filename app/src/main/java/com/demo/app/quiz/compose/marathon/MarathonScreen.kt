package com.demo.app.quiz.compose.marathon

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.app.quiz.compose.components.Question
import com.demo.app.quiz.viewmodels.MarathonViewModel

@SuppressLint("DefaultLocale")
@Composable
fun MarathonScreen(
    navigateToResult: (Int, Int) -> Unit,
    popBack: () -> Unit,
    vm: MarathonViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (uiState.isLoading || uiState.questionNo < 1) {
                CircularProgressIndicator()
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        // CircularProgressIndicator for the countdown
                        CircularProgressIndicator(
                            progress = { uiState.progress },
                            modifier = Modifier.size(60.dp),
                            strokeWidth = 8.dp,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color.Gray
                        )

                        // Display the time left in the center of the circular progress bar
                        Text(
                            text = "${uiState.timeLeft}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

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
                }

                Spacer(Modifier.weight(2F))

                Question(
                    onOptionSelect = vm::selectOptionForCurrentQuestion,
                    question = uiState.currentQuestion.question,
                    options = uiState.currentQuestion.options,
                    selectedIndex = uiState.currentQuestion.selectedIndex,
                    correctIndex = if (uiState.isCheckNotNext) null else uiState.currentQuestion.correctIndex,
                )

                Spacer(Modifier.weight(2F))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextButton(
                        onClick = if (uiState.hasNextQuestion) vm::passQuestion else ({vm.done(navigateToResult)}),
                        modifier = Modifier.weight(1F),
                        shape = RoundedCornerShape(8.dp),
                        enabled = uiState.isCheckNotNext
                    ) {
                        Text("Skip")
                    }

                    Button(
                        onClick = when {
                            uiState.isCheckNotNext -> vm::checkQuestion
                            uiState.hasNextQuestion -> vm::nextQuestion
                            else -> ({ vm.done(navigateToResult) })
                        },
                        modifier = Modifier.weight(1F),
                        shape = RoundedCornerShape(8.dp),
                        colors = if (uiState.isCheckNotNext) {
                            ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        } else { ButtonDefaults.buttonColors() }
                    ) {
                        Text(
                            text = when {
                                uiState.isCheckNotNext -> "Check"
                                uiState.hasNextQuestion -> "Next"
                                else -> "Done"
                            }
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
            }
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