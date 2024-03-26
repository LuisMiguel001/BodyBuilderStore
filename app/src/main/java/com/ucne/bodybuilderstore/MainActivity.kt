package com.ucne.bodybuilderstore

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ucne.bodybuilderstore.ui.screens.detailProductScreen.ProductDetailsScreen
import com.ucne.bodybuilderstore.ui.screens.registroScreen.RegistroProduct
import com.ucne.bodybuilderstore.ui.screens.cartScreen.CartScreen
import com.ucne.bodybuilderstore.ui.screens.typeProductScreen.ProductosScreen
import com.ucne.bodybuilderstore.ui.screens.typeProductScreen.accesorioScreen
import com.ucne.bodybuilderstore.ui.screens.typeProductScreen.ropaScreen
import com.ucne.bodybuilderstore.ui.theme.BodyBuilderStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BodyBuilderStoreTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val items = listOf("Suplementos", "Accesorios", "Ropas")

                NavHost(
                    navController = navController,
                    startDestination = "suplemento",
                ) {
                    composable("suplemento") {
                        ScaffoldContent(navController, items, selectedItem = 0, menuExpanded = false) {
                            ProductosScreen(navController = navController)
                        }
                    }
                    composable("accesorio") {
                        ScaffoldContent(navController, items, selectedItem = 1, menuExpanded = false) {
                            accesorioScreen(navController = navController)
                        }
                    }
                    composable("ropa") {
                        ScaffoldContent(navController, items, selectedItem = 2, menuExpanded = false) {
                            ropaScreen(navController = navController)
                        }
                    }
                    composable("registro") {
                        RegistroProduct(navigateBack = {navController.navigateUp()})
                    }
                    composable("cart") {
                        CartScreen(navigateBack = {navController.navigateUp()})
                    }
                    composable(
                        route = "detalle/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getInt("id")
                        productId?.let { productId ->
                            ProductDetailsScreen(navController, productId)
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScaffoldContent(
    navController: NavController,
    items: List<String>,
    selectedItem: Int,
    menuExpanded: Boolean,
    content: @Composable () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .height(115.dp)
                    .background(Color.Blue),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { menuExpanded = true}) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = "",
                    onValueChange = { /* Handle search input change */ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    placeholder = { Text("Search") },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Black,
                        cursorColor = Color.LightGray,
                        disabledLeadingIconColor = Color.White,
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.LightGray
                        )
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Blue
            ) {
                items.forEachIndexed { index, item ->
                    val icon: Painter = when (index) {
                        0 -> painterResource(id = R.drawable.icons8_protein_48)
                        1 -> painterResource(id = R.drawable.icons8_gym_48)
                        2 -> painterResource(id = R.drawable.icons8_clothes_48)
                        else -> painterResource(id = R.drawable.icons8_clothes_48)
                    }
                    NavigationBarItem(
                        icon = {
                            Icon(
                                icon,
                                contentDescription = item,
                                tint = Color.White,
                                modifier = Modifier.background(Color.Transparent)
                            )
                        },
                        label = { Text(item, color = Color.White) },
                        selected = selectedItem == index,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate("suplemento")
                                1 -> navController.navigate("accesorio")
                                2 -> navController.navigate("ropa")
                            }
                        },
                        alwaysShowLabel = false
                    )
                }
            }
        }
    ) {
        content()
        if (menuExpanded) {
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false  },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
            ) {
                DropdownMenuItem(
                    onClick = { navController.navigate("cart") },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Shopping Cart",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Carrito")
                        }
                    }
                )
                DropdownMenuItem(
                    onClick = { navController.navigate("registro") },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Registro")
                        }
                    }
                )
            }
        }
    }
}

