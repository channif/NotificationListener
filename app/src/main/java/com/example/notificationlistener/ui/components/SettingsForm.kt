package com.example.notificationlistener.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.notificationlistener.ui.state.NotificationListenerUiState
import com.example.notificationlistener.ui.state.UiEvent

@Composable
fun SettingsForm(
    uiState: NotificationListenerUiState,
    onEvent: (UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Pengaturan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            // Endpoint URL
            OutlinedTextField(
                value = uiState.endpointUrl,
                onValueChange = { onEvent(UiEvent.UpdateEndpointUrl(it)) },
                label = { Text("Endpoint URL (wajib)") },
                placeholder = { Text("https://api.example.com/webhook") },
                isError = uiState.validationErrors.endpointUrl != null,
                supportingText = uiState.validationErrors.endpointUrl?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
            
            // API Key
            OutlinedTextField(
                value = uiState.apiKey,
                onValueChange = { onEvent(UiEvent.UpdateApiKey(it)) },
                label = { Text("API Key (opsional)") },
                placeholder = { Text("Masukkan API key jika diperlukan") },
                isError = uiState.validationErrors.apiKey != null,
                supportingText = uiState.validationErrors.apiKey?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (uiState.isApiKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { onEvent(UiEvent.ToggleApiKeyVisibility) }
                    ) {
                        Text(if (uiState.isApiKeyVisible) "Hide" else "Show")
                    }
                }
            )
            
            // Filter Packages
            OutlinedTextField(
                value = uiState.filterPackages,
                onValueChange = { onEvent(UiEvent.UpdateFilterPackages(it)) },
                label = { Text("Filter Package (comma-separated)") },
                placeholder = { Text("id.dana, com.whatsapp") },
                isError = uiState.validationErrors.filterPackages != null,
                supportingText = uiState.validationErrors.filterPackages?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            
            // Forward all apps toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Forward semua aplikasi (abaikan filter package)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Jika ON, semua notifikasi dikirim ke API.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = uiState.forwardAllApps,
                    onCheckedChange = { onEvent(UiEvent.UpdateForwardAllApps(it)) }
                )
            }
            
            // Save button
            Button(
                onClick = { onEvent(UiEvent.SaveSettings) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Menyimpan...")
                } else {
                    Text("Simpan Pengaturan")
                }
            }
        }
    }
}