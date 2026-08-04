package com.example.linguatale.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.linguatale.ui.screen.auth.LoginScreen
import com.example.linguatale.ui.screen.library.LibraryScreen

sealed class Screen(val route: String) {
    object Login: Screen("login")
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
            LoginScreen(onLoginSuccess = {
                navController.navigate(Screen.Library.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
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

        /*composable(Screen.Upload.route) {
            *//*UploadScreen(onUploadSuccess = {
                navController.popBackStack()
            })*//*
        }

        composable(
            route = Screen.Reader.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType },
                navArgument("chapterOrder") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            ReaderScreen(
                bookId = backStackEntry.arguments?.getString("bookId")!!,
                chapterOrder = backStackEntry.arguments?.getInt("chapterOrder")!!,
                onNavigateToChapter = { newOrder ->
                    navController.navigate(
                        Screen.Reader.createRoute(
                            backStackEntry.arguments?.getString("bookId")!!,
                            newOrder
                        )
                    )
                }
            )
        }*/
    }
}