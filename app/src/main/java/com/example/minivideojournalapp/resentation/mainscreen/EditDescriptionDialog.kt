package com.example.minivideojournalapp.resentation.mainscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.minivideojournalapp.ui.theme.LocalExtendedColors

@Composable
fun EditDescriptionDialog(
	currentDescription: String,
	onConfirm: (String) -> Unit,
	onDismiss: () -> Unit
) {
	val extendedColors = LocalExtendedColors.current
	var text by remember { mutableStateOf(currentDescription) }

	AlertDialog(
		onDismissRequest = onDismiss,
		confirmButton = {
			TextButton(onClick = { onConfirm(text) }) {
				Text("Save")
			}
		},
		dismissButton = {
			TextButton(onClick = onDismiss) {
				Text("Cancel")
			}
		},
		title = { Text("Edit Description") },
		text = {
			OutlinedTextField(
				value = text,
				onValueChange = { text = it },
				label = { Text("Description") },
				singleLine = false,
				modifier = Modifier.fillMaxWidth()
			)
		},
		containerColor = extendedColors.bgCard
	)
}