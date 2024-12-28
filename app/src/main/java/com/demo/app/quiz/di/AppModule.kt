package com.demo.app.quiz.di

import android.content.Context
import com.demo.app.quiz.data.repositories.QuizRepository
import com.demo.app.quiz.data.source.QuizDataSource
import com.demo.app.quiz.data.source.dao.QuizDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabaseQuiz(@ApplicationContext context: Context) = QuizDataSource.getDatabase(context).quizDao()

    @Provides
    @Singleton
    fun provideQuizRepository(dao: QuizDao) = QuizRepository(dao)
}