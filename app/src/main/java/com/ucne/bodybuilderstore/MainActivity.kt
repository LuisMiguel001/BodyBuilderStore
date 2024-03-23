package com.ucne.bodybuilderstore

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ucne.bodybuilderstore.ui.screens.detailProductScreen.ProductDetailsScreen
import com.ucne.bodybuilderstore.ui.screens.homeScreen.ProductosScreen
import com.ucne.bodybuilderstore.ui.screens.registroScreen.ProductViewModel
import com.ucne.bodybuilderstore.ui.screens.registroScreen.RegistroProduct
import com.ucne.bodybuilderstore.ui.theme.BodyBuilderStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BodyBuilderStoreTheme(darkTheme = false){
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                var selectedItem by remember { mutableStateOf(0) }
                val items = listOf("Suplementos", "Accesorios", "Ropas")

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "BodyBuilder Store")
                            },
                            actions = {
                                IconButton(onClick = { navController.navigate("") }) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = "Add"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(Color.Blue)
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        ) {
                            items.forEachIndexed { index, item ->
                                val icon = when (index) {
                                    0 -> Icons.Default.Add
                                    1 -> Icons.Default.Person
                                    2 -> Icons.Default.Home
                                    else -> Icons.Default.Add
                                }
                                NavigationBarItem(
                                    icon = { Icon(icon, contentDescription = item) },
                                    label = { Text(item) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        if (index == 0) {
                                            navController.navigate("consulta")
                                        }
                                        if (index == 1) {
                                            navController.navigate("registro")
                                        }
                                    },
                                    alwaysShowLabel = false
                                )
                            }
                        }
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "consulta"
                    ) {
                        composable("registro") {
                            RegistroProduct()
                        }
                        composable("consulta") {
                            ProductosScreen(navController = navController)
                        }
                        composable(
                            route = "detalle/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getInt("id")
                            productId?.let { ProductDetailsScreen(navController, it) }
                        }
                    }
                }
            }
        }
    }
}
