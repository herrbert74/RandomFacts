package com.pelagohealth.codingchallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.pelagohealth.codingchallenge.domain.api.FactRepository
import com.pelagohealth.codingchallenge.domain.model.Fact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val factRepository: FactRepository) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        listenToFactChanges()
        fetchNewFact()
    }

    fun fetchNewFact() {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(isLoading = true)
            }
            factRepository.get()
                .onSuccess {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            error = throwable
                        )
                    }
                }
        }
    }

    private fun listenToFactChanges() {
        viewModelScope.launch {
            factRepository.getFacts().collect { facts ->
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        error = null,
                        fact = facts?.lastOrNull(),
                        previousFacts = facts?.dropLast(1).orEmpty().reversed()
                    )
                }
            }
        }
    }

    fun deleteFact(text: String) {
        viewModelScope.launch {
            factRepository.deleteFact(text)
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val fact: Fact? = null,
        val previousFacts: List<Fact> = emptyList(),
        val error: Throwable? = null
    )

}
