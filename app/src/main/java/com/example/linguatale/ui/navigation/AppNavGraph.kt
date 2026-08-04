package com.example.linguatale.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.linguatale.ui.screen.auth.ConfirmScreen
import com.example.linguatale.ui.screen.auth.LoginScreen
import com.example.linguatale.ui.screen.auth.RegisterScreen
import com.example.linguatale.ui.screen.library.LibraryScreen

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Register: Screen("register")
    object Confirm: Screen("confirm/{email}") {
        fun createRoute(email: String) = "confirm/$email"
    }
    object Library: Screen("library")
    object Upload: Screen("upload")
    object Reader: Screen("reader/{bookId}/{chapterOrder}") {
        fun createRoute(bookId: String, chapterOrder: Int) =
            "reader/$bookId/$chapterOrder"
    }
}

@Composable
fun LinguaTaleAppNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Library.route
        else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Library.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistered = { email ->
                    navController.navigate(Screen.Confirm.createRoute(email))
                },
                onGoToLogin = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Confirm.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStack ->
            ConfirmScreen(
                email = backStack.arguments?.getString("email") ?: "",
                onConfirmed = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Library.route) {
            LibraryScreen(
                onBookClick = { bookId ->
                    navController.navigate(Screen.Reader.createRoute(bookId, 0))
                },
                onUploadClick = {
                    navController.navigate(Screen.Upload.route)
                }
            )
        }
    }
}