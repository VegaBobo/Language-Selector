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
fun ShizukuRequiredWarning(
    onClickContinue: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = { onClickContinue() }) { Text(stringResource(id = R.string.proceed)) }
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.WarningAmber,
                contentDescription = "Warning icon"
            )
        },
        title = { Text(stringResource(id = R.string.permissions_required)) },
        text = { Text(stringResource(id = R.string.shizuku_required)) }
    )
}