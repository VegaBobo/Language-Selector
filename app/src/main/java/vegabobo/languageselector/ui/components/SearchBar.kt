package vegabobo.languageselector.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import vegabobo.languageselector.ui.screen.main.AppInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    placeholder: String = "",
    query: String,
    onUpdatedValue: (String) -> Unit,
    apps: List<AppInfo> = emptyList(),
    onClickApp: (String) -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        SearchBar(
            modifier = Modifier
                .animateContentSize(animationSpec = tween(durationMillis = 10))
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    onSearch = { onUpdatedValue(it) },
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it },
                    placeholder = { Text(placeholder) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Row { actions() }
                    },
                    query = query,
                    onQueryChange = { onUpdatedValue(it) }
                )
            },
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it; if (!it) onUpdatedValue("") },
        ) {
            LazyColumn {
                val filteredApps = apps.filter {
                    query != "" && (
                            it.appPackageName.lowercase().contains(query.lowercase()) ||
                                    it.appName.lowercase().contains(query.lowercase())
                            )
                }

                items(filteredApps.size) {
                    val app = filteredApps[it]
                    AppListItem(
                        packageName = app.appPackageName,
                        appName = app.appName,
                        drawable = app.appIcon,
                        onClickApp = {
                            //onUpdatedValue("")
                            onClickApp(app.appPackageName)
                        }
                    )
                }
            }
        }
        Spacer(Modifier.padding(6.dp))
    }
}