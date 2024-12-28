package com.demo.app.quiz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.app.quiz.data.repositories.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

data class QuizUiState(
    val isLoading: Boolean = true,
    val questionNo: Int = 1,
    val questions: List<QuizQuestionUiState> = emptyList(),
    val showSubmitConfirmation: Boolean = false,
    val showBackConfirmation: Boolean = false,
) {
    val currentQuestion get() = questions[questionNo - 1]
    val hasPrevQuestion get() = questionNo > 1
    val hasNextQuestion get() = questionNo < questions.size
}

data class QuizQuestionUiState(
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val answer: String,
    val selectedIndex: Int? = null,
) {
    val options = listOf(optionA, optionB, optionC, optionD)
    private val selectedOption get() = selectedIndex?.let { options[it] }
    val isCorrectAnswer get() = selectedOption == answer
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState = _uiState.asStateFlow()

    private val totalQuestions = 10

    init {
        viewModelScope.launch {
            val quiz = repository.getRandomQuiz(totalQuestions)
            _uiState.update { currentState ->
                currentState.copy(
                    questions = quiz.map {
                        val options = it
                            .run { listOf(answer, firstOption, secondOption, thirdOption) }
                            .shuffled()
                        QuizQuestionUiState(
                            it.question, options[0], options[1], options[2], options[3], it.answer
                        )
                    }
                )
            }
        }.invokeOnCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun prevQuestion() = _uiState.update {
        it.copy(questionNo = max(0, it.questionNo - 1))
    }

    fun nextQuestion() = _uiState.update {
        it.copy(questionNo = min(totalQuestions, it.questionNo + 1))
    }

    fun selectOptionForCurrentQuestion(index: Int) = _uiState.update { state ->
        state.copy(
            questions = state.questions.map {
                if (it == state.currentQuestion)
                    it.copy(
                        selectedIndex = if (it.selectedIndex == index) null else index
                    )
                else it
            }
        )
    }

    fun evaluateQuiz(onEvaluation: (score:Int, outOf: Int) -> Unit) {
        _uiState.update { it.copy(isLoading = true) }
        val questions = _uiState.value.questions
        var score = 0

        viewModelScope.launch {
            for (question in questions) {
                if (question.isCorrectAnswer) score++
            }
        }.invokeOnCompletion {
            onEvaluation(score, totalQuestions)
        }
    }

    fun setShowSubmitConfirmationDialog(show: Boolean) {
        _uiState.update { it.copy(showSubmitConfirmation = show) }
    }


    fun setShowBackConfirmationDialog(show: Boolean) {
        _uiState.update { it.copy(showBackConfirmation = show) }
    }
}