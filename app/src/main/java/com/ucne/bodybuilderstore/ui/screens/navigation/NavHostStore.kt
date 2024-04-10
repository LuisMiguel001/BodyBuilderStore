package com.ucne.bodybuilderstore.ui.screens.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.ucne.bodybuilderstore.R
import com.ucne.bodybuilderstore.login.LoginScreen
import com.ucne.bodybuilderstore.login.RegistroScreen
import com.ucne.bodybuilderstore.ui.screens.cartScreen.CartScreen
import com.ucne.bodybuilderstore.ui.screens.detailProductScreen.FavoritosScreen
import com.ucne.bodybuilderstore.ui.screens.detailProductScreen.ProductDetailsScreen
import com.ucne.bodybuilderstore.ui.screens.registroScreen.RegistroProduct
import com.ucne.bodybuilderstore.ui.screens.registroScreen.edit
import com.ucne.bodybuilderstore.ui.screens.splashScreen.SplashScreen
import com.ucne.bodybuilderstore.ui.screens.typeProductScreen.ProductosScreen
import com.ucne.bodybuilderstore.ui.screens.typeProductScreen.accesorioScreen
import com.ucne.bodybuilderstore.ui.screens.typeProductScreen.ropaScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val items = listOf("Suplementos", "Accesorios", "Ropas")

    NavHost(
        navController = navController,
        startDestination = "splach",
    ) {
        composable("splach") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
                LoginScreen(navController = navController)
            } else {
                navController.navigate("suplemento") {
                    popUpTo("splach") {
                        inclusive = true
                    }
                }
            }
        }
        composable("registroUser") {
            RegistroScreen(navController = navController)
        }
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
            RegistroProduct(navigateBack = { navController.navigateUp() })
        }
        composable("cart") {
            CartScreen(navigateBack = { navController.navigateUp() })
        }
        composable("favorito") {
            FavoritosScreen(navigateBack = { navController.navigateUp() })
        }
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("id")
            productId?.let { productId ->
                ProductDetailsScreen(productId, navController = navController)
            }
        }
        composable(
            route = "edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("id")
            productId?.let { productId ->
                edit(productId, navController = navController, navigateBack = { navController.navigateUp() })
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
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userName = currentUser?.displayName ?: ""
    val userEmail = currentUser?.email ?: ""
    val userPhotoUrl = currentUser?.photoUrl
    val myGreen = Color(android.graphics.Color.parseColor("#04764B"))
    val isAdmin = userEmail == "admin@gmail.com"

    Scaffold(
        topBar = {
            Surface(
                color = myGreen
            ) {
                Column(
                    modifier = Modifier
                        .height(60.dp)
                        .background(myGreen)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Row {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(35.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(50.dp))
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            text = "BodyBuilder Store",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(50.dp))
                        IconButton(onClick = { navController.navigate("cart") }) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "cart",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(35.dp)
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = myGreen
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
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.6f)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = myGreen
                ) {
                    if (userPhotoUrl != null) {
                        DropdownMenuItem(
                            onClick = { /**/ },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = rememberImagePainter(userPhotoUrl),
                                        contentDescription = "User Photo",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                    )
                                    Text(userName, color = Color.White)
                                    userEmail?.let { name ->
                                        DropdownMenuItem(
                                            onClick = { /* Handle user profile click */ },
                                            text = {
                                                Text(name, color = Color.White)
                                            }
                                        )
                                    }
                                }
                            }
                        )
                    } else {
                        DropdownMenuItem(
                            onClick = { /**/ },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.AccountCircle,
                                        contentDescription = "Default User Icon",
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.LightGray
                                    )
                                    Text(userName, color = Color.Black)
                                    userEmail?.let { name ->
                                        DropdownMenuItem(
                                            onClick = { /**/ },
                                            text = {
                                                Text(name, color = Color.White)
                                            }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
                Divider()
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
                    onClick = {
                        menuExpanded = false
                        navController.navigate("favorito")
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Favorite",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Favoritos")
                        }
                    }
                )
                if (isAdmin) {
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
                Divider()
                DropdownMenuItem(
                    onClick = {/**/ },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Configuración",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Configuración")
                        }
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.ExitToApp,
                                contentDescription = "Cerrar Sesión",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cerrar Sesión")
                        }
                    }
                )
            }
        }
    }
}
