package vegabobo.languageselector.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vegabobo.languageselector.R

@Composable
fun MainTopBarActions(
    isOnSearchMode: Boolean = false,
    isDropdownVisible: Boolean = false,
    isShowingSystemApps: Boolean = false,
    searchQuery: String,
    onClickToggleSearch: () -> Unit,
    onClickToggleDropdown: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onToggleDropdown: () -> Unit,
    onClickToggleSystemApps: () -> Unit,
    onClickAbout: () -> Unit
) {
    Box(
        modifier = Modifier.wrapContentSize(Alignment.Center)
    ) {
        if (isOnSearchMode) {
            SearchBar(
                searchQuery = searchQuery,
                onClickBack = { onClickToggleSearch() },
                onTextFieldChange = { onSearchTextChange(it) }
            )
        } else {
            ToolbarNormal(
                onClickSearch = { onClickToggleSearch() },
                onToggleDropdown = { onToggleDropdown() }
            )
        }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    onClickBack: () -> Unit,
    onTextFieldChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onClickBack() }) {
            Icon(
                contentDescription = "Back icon",
                imageVector = Icons.Outlined.ArrowBack
            )
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .weight(1f),
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            value = searchQuery,
            onValueChange = { onTextFieldChange(it) })
    }
}

@Composable
fun ToolbarNormal(
    onClickSearch: () -> Unit,
    onToggleDropdown: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onClickSearch() }) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search icon"
            )
        }

        IconButton(onClick = { onToggleDropdown() }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More icon"
            )
        }
    }
}
