package com.itis.feature.favorites.impl.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.core.db.DatabaseHandler
import com.itis.feature.favorites.api.model.FavoriteRecipe
import com.itis.feature.favorites.impl.mapper.FavoriteRecipeMapper
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
data class FavoritesViewState(
    val recipeDataState: List<FavoriteRecipe>? = null,
    val dataState: Boolean = false,
    val messageState: String = ""
)

sealed interface FavoritesEvent {
    data class OnRequestFavorites(val idUser: Long): FavoritesEvent
    object OnRecipeClick: FavoritesEvent
}

sealed interface FavoritesAction {
    object NavigateDetails : FavoritesAction
}

@HiltViewModel
class FavoritesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(FavoritesViewState())
    val state: StateFlow<FavoritesViewState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<FavoritesAction?>()
    val action: SharedFlow<FavoritesAction?>
        get() = _action.asSharedFlow()

    fun event(event: FavoritesEvent) {
        when (event) {
            FavoritesEvent.OnRecipeClick -> onRecipeClick()
            is FavoritesEvent.OnRequestFavorites -> onRequestFavorites(event)
        }
    }

    private fun onRequestFavorites(event: FavoritesEvent.OnRequestFavorites) {
        viewModelScope.launch {
            val recipes = arrayListOf<FavoriteRecipe>()
            DatabaseHandler.getFavoriteRecipes(event.idUser)?.forEach {
                recipes.add(FavoriteRecipeMapper.mapFavoriteRecipeModel(it))
            }
            if (recipes.isEmpty()) {
                _state.emit(_state.value.copy(messageState = "List is empty"))
            }
            _state.emit(_state.value.copy(recipeDataState = recipes))
        }
    }

    private fun onRecipeClick() {
        viewModelScope.launch {
            _action.emit(FavoritesAction.NavigateDetails)
        }
    }

}