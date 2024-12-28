package com.demo.app.quiz.compose.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    navigateToQuiz: () -> Unit,
    navigateToMarathon: () -> Unit,
    exitApp: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = navigateToQuiz,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text(
                    text = "Start Quiz",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Button(
                onClick = navigateToMarathon,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text(
                    text = "Start Quiz Marathon",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            OutlinedButton(
                onClick = exitApp,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text(
                    text = "Exit",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}