package com.demo.app.quiz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.app.quiz.data.repositories.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

data class MarathonUiState(
    val isLoading: Boolean = true,
    val questionNo: Int = 0,
    val questions: List<MarathonQuestionUiState> = emptyList(),
    val timeLeft: Int = time,
    val score: Int = 0,
    val isCheckNotNext: Boolean = true,
    val showBackConfirmation: Boolean = false,
) {
    val currentQuestion get() = questions[questionNo - 1]
    val hasNextQuestion get() = questionNo < questions.size
    val progress get() = timeLeft.toFloat() / time

    companion object {
        const val time: Int = 15 //In seconds
    }
}

data class MarathonQuestionUiState(
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
    val correctIndex get() = options.indexOf(answer).takeIf { it != -1 }
}

@HiltViewModel
class MarathonViewModel @Inject constructor(
    private val repository: QuizRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MarathonUiState())
    val uiState = _uiState.asStateFlow()

    private val totalQuestions = 20
    private var quizTimer: Job? = null

    init {
        viewModelScope.launch {
            val quiz = repository.getRandomQuiz(totalQuestions)
            _uiState.update { currentState ->
                currentState.copy(
                    questions = quiz.map {
                        val options = it
                            .run { listOf(answer, firstOption, secondOption, thirdOption) }
                            .shuffled()
                        MarathonQuestionUiState(
                            it.question, options[0], options[1], options[2], options[3], it.answer
                        )
                    }
                )
            }
        }.invokeOnCompletion {
            _uiState.update { it.copy(isLoading = false) }
            nextQuestion()
        }
    }

    fun passQuestion() {
        _uiState.update { state ->
            state.copy(questions = state.questions.map {
                if (it == state.currentQuestion) it.copy(selectedIndex = null)
                else it
            })
        }
        nextQuestion()
    }

    fun checkQuestion() {
        val question = _uiState.value.currentQuestion
        quizTimer?.cancel()

        _uiState.update {
            it.copy(
                score = if (question.isCorrectAnswer) it.score + 1 else it.score,
                isCheckNotNext = false,
            )
        }
    }

    fun nextQuestion() {
        _uiState.update {
            it.copy(
                questionNo = min(totalQuestions, it.questionNo + 1),
                isCheckNotNext = true,
                timeLeft = MarathonUiState.time
            )
        }

        quizTimer?.cancel()

        quizTimer = viewModelScope.launch(Dispatchers.Default) {
            while (_uiState.value.timeLeft > 0 && isActive) {
                delay(1000)
                _uiState.update {
                    it.copy(timeLeft = it.timeLeft - 1)
                }
            }

            checkQuestion()
        }
    }

    fun selectOptionForCurrentQuestion(index: Int) = _uiState.update { state ->
        if (state.isCheckNotNext) state.copy(
            questions = state.questions.map {
                if (it == state.currentQuestion)
                    it.copy(
                        selectedIndex = if (it.selectedIndex == index) null else index
                    )
                else it
            }
        )
        else state
    }

    fun done(navigate: (score: Int, outOf: Int) -> Unit) {
        navigate(uiState.value.score, totalQuestions)
    }

    fun setShowBackConfirmationDialog(show: Boolean) {
        _uiState.update { it.copy(showBackConfirmation = show) }
    }
}