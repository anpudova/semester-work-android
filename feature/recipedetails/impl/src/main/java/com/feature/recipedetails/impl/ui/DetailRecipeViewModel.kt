package com.feature.recipedetails.impl.ui

import androidx.compose.runtime.Immutable
import com.feature.recipedetails.api.model.DetailRecipe
import com.feature.recipedetails.api.model.Ingredients
import com.feature.recipedetails.impl.usecase.GetDetailRecipeByIdUseCase
import com.feature.recipedetails.impl.usecase.GetIngredientsByIdUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.recipedetails.api.model.FavoriteRecipe
import com.feature.recipedetails.impl.data.mapper.FavoriteRecipeMapper
import com.itis.core.db.DatabaseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Immutable
data class DetailRecipeViewState(
    val progressBarState: Boolean = false,
    val detailDataState: DetailRecipe? = null,
    val ingredientsDataState: Ingredients? = null,
    val errorState: Throwable? = null,
    val messageState: String = "",
    val heartState: Boolean = false
)

sealed interface DetailRecipeEvent {
    data class OnRequestDetail(val id: Long): DetailRecipeEvent
    data class OnRequestIngredients(val id: Long): DetailRecipeEvent
    data class OnHeartClick(val recipe: FavoriteRecipe): DetailRecipeEvent
    data class isFavoriteRecipe(val recipe: FavoriteRecipe): DetailRecipeEvent
    object OnNavigateBack: DetailRecipeEvent
    object OnMessageConnectShow: DetailRecipeEvent
}

sealed interface DetailRecipeAction {
    object NavigateBack : DetailRecipeAction
}

@HiltViewModel
class DetailRecipeViewModel @Inject constructor(
    private val getIngredientsUseCase: GetIngredientsByIdUseCase,
    private val getDetailUseCase: GetDetailRecipeByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailRecipeViewState())
    val state: StateFlow<DetailRecipeViewState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<DetailRecipeAction?>()
    val action: SharedFlow<DetailRecipeAction?>
        get() = _action.asSharedFlow()

    fun event(event: DetailRecipeEvent) {
        when (event) {
            DetailRecipeEvent.OnNavigateBack -> onNavigateBack()
            DetailRecipeEvent.OnMessageConnectShow -> onMessageConnectShow()
            is DetailRecipeEvent.isFavoriteRecipe -> isFavoriteRecipe(event)
            is DetailRecipeEvent.OnHeartClick -> onHeartClick(event)
            is DetailRecipeEvent.OnRequestDetail -> onRequestDetail(event)
            is DetailRecipeEvent.OnRequestIngredients -> onRequestIngredients(event)
        }
    }

    private fun isFavoriteRecipe(event: DetailRecipeEvent.isFavoriteRecipe) {
        viewModelScope.launch {
            if (DatabaseHandler.existInFavorites(event.recipe.id, event.recipe.idUser) != 0) {
                _state.emit(_state.value.copy(heartState = true))
            } else {
                _state.emit(_state.value.copy(heartState = false))
            }
        }
    }

    private fun onNavigateBack() {
        viewModelScope.launch {
            _action.emit(DetailRecipeAction.NavigateBack)
        }
    }

    private fun onHeartClick(event: DetailRecipeEvent.OnHeartClick) {
        viewModelScope.launch {
            if (DatabaseHandler.existInFavorites(event.recipe.id, event.recipe.idUser) != 0) {
                DatabaseHandler.deleteFavoriteRecipe(FavoriteRecipeMapper.mapFavoriteRecipeEntity(event.recipe))
                _state.emit(_state.value.copy(heartState = false))
            } else {
                DatabaseHandler.createFavoriteRecipe(FavoriteRecipeMapper.mapFavoriteRecipeEntity(event.recipe))
                _state.emit(_state.value.copy(heartState = true))
            }
        }
    }

    private fun onRequestDetail(event: DetailRecipeEvent.OnRequestDetail) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(progressBarState = true))

            runCatching {
                getDetailUseCase(event.id)
            }.onSuccess { dataModel ->
                _state.emit(_state.value.copy(progressBarState = false, detailDataState = dataModel))
            }.onFailure { ex ->
                _state.emit(_state.value.copy(progressBarState = false, errorState = ex))
            }
        }
    }

    private fun onRequestIngredients(event: DetailRecipeEvent.OnRequestIngredients) {
        viewModelScope.launch {

            runCatching {
                getIngredientsUseCase(event.id)
            }.onSuccess { dataModel ->
                _state.emit(_state.value.copy(progressBarState = false, ingredientsDataState = dataModel))
            }.onFailure { ex ->
                _state.emit(_state.value.copy(progressBarState = false, errorState = ex))
            }
        }
    }

    private fun onMessageConnectShow() {
        viewModelScope.launch {
            _state.emit(_state.value.copy(messageState = "No internet connection"))
        }
    }

}
