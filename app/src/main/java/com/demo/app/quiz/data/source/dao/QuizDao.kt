package com.demo.app.quiz.data.source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.app.quiz.data.source.entities.Quiz

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quiz: Quiz)

    @Query("SELECT * FROM quiz")
    suspend fun getAllQuiz(): List<Quiz>

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    @Query("SELECT * FROM quiz ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomUsers(count: Int): List<Quiz>
}