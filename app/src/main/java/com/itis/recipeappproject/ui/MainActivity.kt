package com.itis.recipeappproject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import com.itis.core.designsystem.Theme
import com.itis.core.db.DatabaseHandler
import com.itis.recipeappproject.navigation.RecipeNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DatabaseHandler.provideDatabase(applicationContext)
        setContent {
            Theme {
                Surface {
                    RecipeNavHost()
                }
            }
        }
    }
}