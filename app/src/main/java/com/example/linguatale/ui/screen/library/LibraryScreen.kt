package com.example.linguatale.ui.screen.library

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.linguatale.ui.screen.auth.AuthViewModel

@Composable
fun LibraryScreen(
    onBookClick: (String) -> Unit,
    onUploadClick: () -> Unit,
    viewModel: LibraryViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val state by authViewModel.state.collectAsStateWithLifecycle()

    // When logout completes, DataStore clears → isLoggedIn() emits false
    // → MainActivity re-composes → NavGraph starts at Login automatically
    // No manual navigation needed

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Books") },
                actions = {
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        // library content
    }
}