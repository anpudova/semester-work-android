package com.itis.recipeappproject.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.feature.profile.impl.ui.auth.signin.SignInScreen
import com.feature.profile.impl.ui.auth.signup.SignUpScreen
import com.feature.profile.impl.ui.profile.ProfileScreen
import com.feature.recipedetails.impl.ui.DetailRecipeScreen
import com.feature.recipesearch.impl.ui.RecipeSearchScreen
import com.itis.feature.favorites.impl.ui.FavoritesScreen

sealed class Screen(
    val route: String,
    val name: String,
    val icon: ImageVector,
) {
    object Search : Screen(
        route = "search",
        name = "Search",
        icon = Icons.Filled.Search,
    )
    object Details : Screen(
        route = "details",
        name = "Details",
        icon = Icons.Filled.Menu,
    )
    object Profile : Screen(
        route = "profile",
        name = "Profile",
        icon = Icons.Filled.AccountCircle,
    )
    object Favorites : Screen(
        route = "favorites",
        name = "Favorites",
        icon = Icons.Filled.Favorite,
    )
    object SignIn : Screen(
        route = "signin",
        name = "SignIn",
        icon = Icons.Filled.Person,
    )
    object SignUp : Screen(
        route = "signup",
        name = "SignUp",
        icon = Icons.Filled.Person,
    )
}

@Composable
fun RecipeNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.Search
) {
    val items = listOf(
        Screen.Search,
        Screen.Favorites,
        Screen.Profile
    )
    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.name) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = startDestination.route,
            Modifier.padding(innerPadding),
        ) {
            composable(Screen.Search.route) {
                RecipeSearchScreen(navController)
            }
            composable(Screen.Details.route) {
                DetailRecipeScreen(navController)
            }
            composable(Screen.SignIn.route) {
                SignInScreen(navController)
            }
            composable(Screen.SignUp.route) {
                SignUpScreen(navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(navController)
            }
        }
    }
}