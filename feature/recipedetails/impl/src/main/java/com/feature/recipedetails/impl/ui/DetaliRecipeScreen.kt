@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.recipedetails.impl.ui

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.feature.recipedetails.api.model.DetailRecipe
import com.feature.recipedetails.api.model.FavoriteRecipe
import com.feature.recipedetails.api.model.Ingredients
import com.itis.core.db.DatabaseHandler
import com.itis.core.utils.PreferencesManager
import java.util.Random

@Composable
fun DetailRecipeScreen(
    navController: NavController,
    viewModel: DetailRecipeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    DetailRecipeContent(
        viewState = state.value,
        eventHandler = viewModel::event
    )

    DetailRecipeActions(
        navController = navController,
        viewAction = action
    )
}

@Composable
fun DetailRecipeContent(
    viewState: DetailRecipeViewState,
    eventHandler: (DetailRecipeEvent) -> Unit,
) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    val id = preferencesManager.getDataLong("id-recipe", -1)
    val title = preferencesManager.getDataString("title-recipe", "")
    val image = preferencesManager.getDataString("image-recipe", "")
    val idUser = preferencesManager.getDataLong("id", -1)

    val isLoading = remember { mutableStateOf(true) }

    val recipe = FavoriteRecipe(id, title, image, idUser)
    if (isOnline(context)) {
        LaunchedEffect(isLoading.value) {
            if (isLoading.value) {
                eventHandler.invoke(DetailRecipeEvent.OnRequestIngredients(id))
                eventHandler.invoke(DetailRecipeEvent.OnRequestDetail(id))
                eventHandler.invoke(DetailRecipeEvent.isFavoriteRecipe(recipe))
                isLoading.value = false
            }
        }
    } else {
        LaunchedEffect(isLoading.value) {
            if (isLoading.value) {
                eventHandler.invoke(DetailRecipeEvent.OnMessageConnectShow)
                isLoading.value = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        // Toolbar

        TopAppBar(
            title = {
                Text(
                    text = ""
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    eventHandler.invoke(DetailRecipeEvent.OnNavigateBack)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            },
            actions = {
                if (idUser != -1L) {
                    IconButton(onClick = {
                        eventHandler.invoke(DetailRecipeEvent.OnHeartClick(recipe))
                    }) {
                        Icon(
                            imageVector = (if (viewState.heartState) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Filled.FavoriteBorder
                            }),
                            contentDescription = ""
                        )
                    }
                }
            }
        )
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 2.dp, 8.dp, 8.dp),
            textAlign = TextAlign.Center
        )

        // Recipe List
        if (viewState.detailDataState?.steps?.isNotEmpty() == true && viewState.ingredientsDataState?.ingredients?.isNotEmpty() == true) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Image(
                            painter = rememberAsyncImagePainter(image),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .border(BorderStroke(1.5.dp, Color.LightGray))
                        )
                    }
                }
                item {
                    Text(
                        text = "Ingredients:",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
                items(items = viewState.ingredientsDataState.ingredients!!, key = { it.id }) {
                    // Replace with your recipe item composable
                    IngredientItem(ingredient = it)
                }
                item {
                    Text(
                        text = "Steps:",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
                items(items = viewState.detailDataState.steps!!, key = { it.id }) {
                    // Replace with your recipe item composable
                    StepItem(step = it)
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

        // Progress Bar
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
private fun IngredientItem(ingredient: Ingredients.Ingredient) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(start = 15.dp, end = 15.dp, bottom = 5.dp)
            .background(MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = ingredient.name,
                modifier = Modifier
                    .padding(end = 5.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = ingredient.value.toString(),
                    modifier = Modifier
                        .padding(end = 5.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = ingredient.unit,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun StepItem(step: DetailRecipe.Step) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(start = 15.dp, end = 15.dp, bottom = 5.dp)
            .background(MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "Step " + step.number + " ",
                modifier = Modifier
                    .padding(end = 10.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = step.step,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun DetailRecipeActions(
    navController: NavController,
    viewAction: DetailRecipeAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            DetailRecipeAction.NavigateBack -> {
                navController.navigateUp()
            }
        }
    }
}

fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
}

@Preview(showBackground = true)
@Composable
fun DetailRecipePreview() {
    DetailRecipeContent(
        viewState = DetailRecipeViewState(
            progressBarState = false,
            detailDataState = null,
            ingredientsDataState = null,
            errorState = null
        ), {}
    )
}