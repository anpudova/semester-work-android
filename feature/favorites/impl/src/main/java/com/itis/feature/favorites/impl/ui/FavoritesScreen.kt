@file:OptIn(ExperimentalMaterial3Api::class)

package com.itis.feature.favorites.impl.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.itis.core.utils.PreferencesManager
import com.itis.feature.favorites.api.model.FavoriteRecipe

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    FavoritesContent(
        viewState = state.value,
        eventHandler = viewModel::event
    )

    FavoritesActions(
        navController = navController,
        viewAction = action
    )
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun FavoritesContent(
    viewState: FavoritesViewState,
    eventHandler: (FavoritesEvent) -> Unit,
) {
    val preferencesManager = PreferencesManager(LocalContext.current)
    val id = preferencesManager.getDataLong("id", -1L)
    if (id != -1L) {
        eventHandler.invoke(FavoritesEvent.OnRequestFavorites(id))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Toolbar

        TopAppBar(
            title = {
                Text(
                    text = "Favorites",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 8.dp)
        )

        if (id != -1L) {
            if (viewState.recipeDataState?.isNotEmpty() == true) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(items = viewState.recipeDataState, key = { it.id }) {
                        RecipeItem(recipe = it, eventHandler = eventHandler, preferencesManager = preferencesManager)
                    }
                }
            } else {
                Text(
                    text = viewState.messageState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Available only to authorized users",
                    modifier = Modifier
                        .padding(8.dp, 30.dp, 8.dp, 8.dp)
                )
            }
        }

    }
}

@Composable
private fun FavoritesActions(
    navController: NavController,
    viewAction: FavoritesAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            FavoritesAction.NavigateDetails -> {
                navController.navigate("details")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun RecipeItem(
    recipe: FavoriteRecipe,
    eventHandler: (FavoritesEvent) -> Unit,
    preferencesManager: PreferencesManager) {

    Column(
        modifier = Modifier
            .clickable(
                enabled = true,
                onClickLabel = null,
                role = null,
                onClick = {
                    eventHandler.invoke(FavoritesEvent.OnRecipeClick)
                    preferencesManager.saveDataLong("id-recipe", recipe.id)
                    preferencesManager.saveDataString("title-recipe", recipe.name)
                    preferencesManager.saveDataString("image-recipe", recipe.image)
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = rememberAsyncImagePainter(recipe.image),
                contentDescription = recipe.name,
                modifier = Modifier
                    .wrapContentSize()
                    .height(150.dp)
                    .wrapContentWidth()

            )
        }

        Text(
            text = recipe.name,
            modifier = Modifier
                .padding(8.dp, 1.dp, 8.dp, 5.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Preview(showBackground = true)
@Composable
fun FavoritesPreview() {
    FavoritesContent(
        viewState = FavoritesViewState(
            recipeDataState = null
        ), {}
    )
}