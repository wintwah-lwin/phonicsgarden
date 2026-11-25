package com.example.phonicsgarden.ui.parentgate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.phonicsgarden.engine.ParentGateValidator

@Composable
fun ParentGateDialog(
    onSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    val question = remember { ParentGateValidator.generateQuestion() }
    var answerText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    fun submit() {
        if (ParentGateValidator.validateAnswer(answerText, question.answer)) {
            showError = false
            onSuccess()
        } else {
            showError = true
            answerText = ""
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Grown-Ups Only 🔒", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    "Please solve this math problem to continue to Parent Settings & Stats:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    question.prompt,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = answerText,
                    onValueChange = {
                        answerText = it
                        showError = false
                    },
                    label = { Text("Your answer") },
                    singleLine = true,
                    isError = showError,
                    supportingText = {
                        if (showError) {
                            Text(
                                "Incorrect answer. Please try again.",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { submit() }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { submit() }) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
