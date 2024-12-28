package com.demo.app.quiz.compose.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.app.quiz.ui.theme.QuizTheme

@Composable
fun ResultScreen(
    score: Int,
    outOf: Int,
    navigateToHome: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Congratulations!",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(horizontal = 48.dp),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "$score/$outOf",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(horizontal = 48.dp),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "You have successfully finished the quiz.",
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = navigateToHome
            ) { Text("Done") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    QuizTheme {
        ResultScreen(0, 0, {})
    }
}