package com.example.notificationlistener.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notificationlistener.ui.components.DebugUtilities
import com.example.notificationlistener.ui.components.LogsSection
import com.example.notificationlistener.ui.components.PermissionStatusCard
import com.example.notificationlistener.ui.components.SettingsForm
import com.example.notificationlistener.ui.state.UiEvent
import com.example.notificationlistener.ui.viewmodel.NotificationListenerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListenerScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationListenerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    // Battery optimization dialog
    if (uiState.showBatteryOptimizationDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.handleEvent(UiEvent.DismissBatteryOptimizationDialog) },
            title = { Text("Optimasi Baterai") },
            text = { 
                Text("Untuk menjaga layanan tetap aktif, disarankan untuk menonaktifkan optimasi baterai untuk aplikasi ini.")
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.handleEvent(UiEvent.OpenBatteryOptimizationSettings) }
                ) {
                    Text("Buka Pengaturan")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.handleEvent(UiEvent.DismissBatteryOptimizationDialog) }
                ) {
                    Text("Nanti")
                }
            }
        )
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = "Notification Listener",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Permission Status
        PermissionStatusCard(
            isGranted = uiState.isNotificationAccessGranted,
            onCheckStatus = { viewModel.handleEvent(UiEvent.CheckPermissionStatus) },
            onOpenSettings = { viewModel.handleEvent(UiEvent.OpenNotificationSettings) }
        )
        
        // Settings Form
        SettingsForm(
            uiState = uiState,
            onEvent = viewModel::handleEvent
        )
        
        // Logs Section
        LogsSection(
            logs = uiState.logs,
            onClearLogs = { viewModel.handleEvent(UiEvent.ClearLogs) },
            onShareLogs = { viewModel.handleEvent(UiEvent.ShareLogs) }
        )
        
        // Debug Utilities
        DebugUtilities(
            onTestSend = { viewModel.handleEvent(UiEvent.TestSend) },
            onCopySettings = { viewModel.handleEvent(UiEvent.CopySettings) }
        )
    }
}