package com.demo.app.quiz.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.demo.app.quiz.compose.home.HomeScreen
import com.demo.app.quiz.compose.marathon.MarathonScreen
import com.demo.app.quiz.compose.quiz.QuizScreen
import com.demo.app.quiz.compose.result.ResultScreen

@Composable
fun QuizApp(
    exitApp: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home) {
        composable<Screen.Home> {
            HomeScreen(
                navigateToQuiz = {
                    navController.navigate(Screen.Quiz)
                },
                navigateToMarathon = {
                    navController.navigate(Screen.Marathon)
                },
                exitApp = exitApp,
            )
        }

        composable<Screen.Quiz> {
            QuizScreen(
                navigateToResult = { score: Int, outOf: Int ->
                    navController.navigate(Screen.Result(score, outOf)) {
                        popUpTo(Screen.Quiz) { inclusive = true }
                    }
                },
                popBack = navController::popBackStack
            )
        }

        composable<Screen.Result> {
            val screen = it.toRoute<Screen.Result>()

            ResultScreen(
                score = screen.score,
                outOf = screen.outOf,
                navigateToHome = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.Marathon> {
            MarathonScreen(
                navigateToResult = { score: Int, outOf: Int ->
                    navController.navigate(Screen.Result(score, outOf)) {
                        popUpTo(Screen.Marathon) { inclusive = true }
                    }
                },
                popBack = navController::popBackStack,
            )
        }
    }
}