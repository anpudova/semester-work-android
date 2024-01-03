package com.feature.recipesearch.impl.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.recipesearch.api.model.Recipes
import com.feature.recipesearch.impl.usecase.GetRecipesByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class RecipeSearchViewState(
    val progressBarState: Boolean = false,
    val recipeDataState: Recipes? = null,
    val errorState: Throwable? = null,
    val messageState: String = ""
)

sealed interface RecipeSearchEvent {
    data class OnRequestRecipes(val name: String): RecipeSearchEvent
    object OnRecipeClick: RecipeSearchEvent
    object OnMessageConnectShow: RecipeSearchEvent
}

sealed interface RecipeSearchAction {
    object NavigateDetails: RecipeSearchAction
}

@HiltViewModel
class RecipeSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecipesUseCase: GetRecipesByNameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeSearchViewState())
    val state: StateFlow<RecipeSearchViewState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<RecipeSearchAction?>()
    val action: SharedFlow<RecipeSearchAction?>
        get() = _action.asSharedFlow()

    fun event(recipeSearchEvent: RecipeSearchEvent) {
        when (recipeSearchEvent) {
            RecipeSearchEvent.OnRecipeClick -> onRecipeClick()
            RecipeSearchEvent.OnMessageConnectShow -> onMessageConnectShow()
            is RecipeSearchEvent.OnRequestRecipes -> onRequestRecipes(recipeSearchEvent)
        }
    }

    private fun onRequestRecipes(event: RecipeSearchEvent.OnRequestRecipes) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(progressBarState = true, recipeDataState = null))

            runCatching {
                getRecipesUseCase(event.name)
            }.onSuccess { data ->
                _state.emit(_state.value.copy(progressBarState = false, recipeDataState = data))
                if (_state.value.recipeDataState == null) {
                    _state.emit(_state.value.copy(messageState = "Not found recipes"))
                } else {
                    _state.emit(_state.value.copy(messageState = ""))
                }
            }.onFailure { ex ->
                _state.emit(_state.value.copy(progressBarState = false, errorState = ex))
            }
        }
    }

    private fun onRecipeClick() {
        viewModelScope.launch {
            _action.emit(RecipeSearchAction.NavigateDetails)
        }
    }

    private fun onMessageConnectShow() {
        viewModelScope.launch {
            _state.emit(_state.value.copy(messageState = "No internet connection"))
        }
    }

    companion object {

        const val REQUEST_TIME_MILLIS_DELAY = 2000L
    }
}
