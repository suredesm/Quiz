package com.demo.app.quiz.data.repositories

import com.demo.app.quiz.data.source.dao.QuizDao
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val source: QuizDao
) {
    suspend fun getRandomQuiz(count: Int) = source.getRandomUsers(count)
}