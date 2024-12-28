package com.demo.app.quiz.compose

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home: Screen()

    @Serializable
    data object Quiz: Screen()

    @Serializable
    data class Result(
        val score: Int,
        val outOf: Int
    ): Screen()

    @Serializable
    data object Marathon: Screen()
}