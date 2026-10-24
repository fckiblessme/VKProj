package com.kitchino.vkproj.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kitchino.vkproj.R
import com.kitchino.vkproj.model.App
import com.kitchino.vkproj.model.AppRepository

/**
 * Экран «Список приложений» с presentation-слоем на ViewModel (MVVM).
 *
 * Состояние хранится в [AppListViewModel], поэтому контент сохраняется при
 * повороте экрана и не загружается повторно. Snackbar показывается по
 * единоразовому событию, поэтому не появляется повторно после поворота.
 */
@Composable
fun AppListScreen(
    onAppClick: (App) -> Unit,
    viewModel: AppListViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = stringResource(R.string.app_list_snackbar)

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                AppListEvent.ShowToolbarSnackbar ->
                    snackbarHostState.showSnackbar(snackbarMessage)
            }
        }
    }

    AppListContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onToolbarClick = viewModel::onToolbarClick,
        onAppClick = onAppClick,
    )
}

/**
 * Stateless-часть экрана: отрисовывает переданное состояние.
 * Отделена от [AppListScreen], чтобы её можно было использовать в preview.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListContent(
    uiState: AppListUiState,
    snackbarHostState: SnackbarHostState,
    onToolbarClick: () -> Unit,
    onAppClick: (App) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToolbarClick),
                title = {
                    Text(
                        text = stringResource(R.string.app_list_title),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Apps,
                        contentDescription = stringResource(R.string.current_app_icon_desc),
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 16.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.apps, key = { it.id }) { app ->
                        AppItem(app = app, onClick = { onAppClick(app) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppItem(app: App, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { _: Offset -> onClick() })
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Icon
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = app.icon,
                    contentDescription = app.name,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // App Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = app.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = app.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = app.category,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AppListScreenPreview() {
    com.kitchino.vkproj.ui.theme.VKProjTheme {
        AppListContent(
            uiState = AppListUiState(
                apps = AppRepository.sampleApps,
                isLoading = false
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onToolbarClick = {},
            onAppClick = {}
        )
    }
}