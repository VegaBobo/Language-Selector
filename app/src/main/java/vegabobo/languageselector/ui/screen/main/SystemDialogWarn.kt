package vegabobo.languageselector.ui.screen.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import vegabobo.languageselector.R

@Composable
fun SystemDialogWarn(
    onClickContinue: () -> Unit,
    onClickCancel: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Outlined.WarningAmber,
                contentDescription = "Warning icon"
            )
        },
        text = { Text(stringResource(R.string.warning_system_apps)) },
        title = { Text(stringResource(R.string.warning)) },
        onDismissRequest = { onClickCancel() },
        confirmButton = {
            TextButton(onClick = { onClickContinue() }) {
                Text(stringResource(R.string.proceed))
            }
        },
        dismissButton = {
            TextButton(onClick = { onClickCancel() }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}