package com.demo.app.quiz.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demo.app.quiz.data.source.dao.QuizDao
import com.demo.app.quiz.data.source.entities.Quiz

@Database(entities = [Quiz::class], version = 1, exportSchema = false)
abstract class QuizDataSource: RoomDatabase() {
    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile private var INSTANCE: QuizDataSource? = null

        fun getDatabase(context: Context): QuizDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDataSource::class.java,
                    "app_database"
                ).createFromAsset("database/quiz.db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}