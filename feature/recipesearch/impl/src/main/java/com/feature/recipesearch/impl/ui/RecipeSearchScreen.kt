@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.recipesearch.impl.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feature.recipesearch.api.model.Recipes
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.feature.recipesearch.impl.R
import com.itis.core.utils.PreferencesManager

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun RecipeSearchScreen(
    navController: NavController,
    viewModel: RecipeSearchViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    RecipeSearchContent(
        viewState = state.value,
        eventHandler = viewModel::event
    )

    SearchScreenActions(
        navController = navController,
        viewAction = action
    )
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun RecipeSearchContent(
    viewState: RecipeSearchViewState,
    eventHandler: (RecipeSearchEvent) -> Unit,
) {
    var recipeName by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Toolbar

        TopAppBar(
            title = {
                Text(
                    text = "Search",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 8.dp)
        )



        // Search Box
        TextField(
            value = recipeName,
            onValueChange = {
                recipeName = it
            },
            label = { Text("name recipe")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (isOnline(context)) {
                            eventHandler.invoke(RecipeSearchEvent.OnRequestRecipes(recipeName.text))
                        } else {
                            eventHandler.invoke(RecipeSearchEvent.OnMessageConnectShow)
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                }
            }

        )

        // Recipe List
        if (viewState.recipeDataState?.recipes?.isNotEmpty() == true) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(items = viewState.recipeDataState.recipes!!, key = { it.id }) {
                    // Replace with your recipe item composable
                    RecipeItem(recipe = it, eventHandler = eventHandler, preferencesManager = preferencesManager)
                }
            }
        }

        Text(
            text = viewState.messageState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )


        if (viewState.progressBarState) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)

            )
        }
    }
}

@Composable
private fun SearchScreenActions(
    navController: NavController,
    viewAction: RecipeSearchAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            RecipeSearchAction.NavigateDetails -> {
                navController.navigate("details")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun RecipeItem(
    recipe: Recipes.Recipe,
    eventHandler: (RecipeSearchEvent) -> Unit,
    preferencesManager: PreferencesManager) {

    Column(
        modifier = Modifier
            .clickable(
                enabled = true,
                onClickLabel = null,
                role = null,
                onClick = {
                    eventHandler.invoke(RecipeSearchEvent.OnRecipeClick)
                    preferencesManager.saveDataLong("id-recipe", recipe.id)
                    preferencesManager.saveDataString("title-recipe", recipe.title)
                    preferencesManager.saveDataString("image-recipe", recipe.image)
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = rememberAsyncImagePainter(recipe.image),
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .border(BorderStroke(1.5.dp, Color.LightGray))
            )
        }
        Text(
            text = recipe.title,
            modifier = Modifier
                .padding(8.dp, 5.dp, 8.dp, 10.dp)
        )
    }
}

fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Preview(showBackground = true)
@Composable
fun RecipeSearchPreview() {
    RecipeSearchContent(
        viewState = RecipeSearchViewState(
            progressBarState = false,
            recipeDataState = null,
            errorState = null
        ), {}
    )
}