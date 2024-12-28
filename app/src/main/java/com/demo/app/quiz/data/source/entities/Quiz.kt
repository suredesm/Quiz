package com.demo.app.quiz.data.source.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz")
data class Quiz(
    @PrimaryKey
    val question: String = "",
    val answer: String = "",
    val firstOption: String = "",
    val secondOption: String = "",
    val thirdOption: String = "",
)