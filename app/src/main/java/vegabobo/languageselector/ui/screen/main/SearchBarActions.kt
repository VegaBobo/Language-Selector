package vegabobo.languageselector.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import vegabobo.languageselector.R

@Composable
fun SearchBarActions(
    isDropdownVisible: Boolean = false,
    isShowingSystemApps: Boolean = false,
    onClickToggleDropdown: () -> Unit,
    onToggleDropdown: () -> Unit,
    onClickToggleSystemApps: () -> Unit,
    onClickAbout: () -> Unit
) {
    Box(
        modifier = Modifier.wrapContentSize(Alignment.Center)
    ) {
        ToolbarNormal(
            onToggleDropdown = { onToggleDropdown() }
        )

        DropdownMenu(
            expanded = isDropdownVisible,
            onDismissRequest = { onClickToggleDropdown() }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = if (isShowingSystemApps)
                            stringResource(R.string.show_only_user_apps)
                        else
                            stringResource(R.string.show_system_apps)
                    )
                },
                onClick = { onClickToggleSystemApps() }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.about)) },
                onClick = { onClickAbout(); onClickToggleDropdown() }
            )
        }
    }
}

@Composable
fun ToolbarNormal(
    onToggleDropdown: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onToggleDropdown() }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More icon"
            )
        }
    }
}
