package vegabobo.languageselector.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import vegabobo.languageselector.R
import vegabobo.languageselector.ui.components.AppListItem
import vegabobo.languageselector.ui.components.SearchBar
import vegabobo.languageselector.ui.screen.BaseScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainScreenVm: MainScreenVm = hiltViewModel(),
    navigateToAppScreen: (String) -> Unit,
    navigateToAbout: () -> Unit,
) {
    val uiState by mainScreenVm.uiState.collectAsState()

    BaseScreen(
        searchBar = {
            SearchBar(
                placeholder = stringResource(R.string.search),
                onUpdatedValue = { mainScreenVm.onSearchTextFieldChange(it) },
                query = uiState.searchTextFieldValue,
                onClickApp = { navigateToAppScreen(it) },
                apps = uiState.listOfApps,
                actions = {
                    SearchBarActions(
                        isDropdownVisible = uiState.isDropdownVisible,
                        isShowingSystemApps = uiState.isShowingSystemApps,
                        onClickToggleDropdown = { mainScreenVm.toggleDropdown() },
                        onToggleDropdown = { mainScreenVm.toggleDropdown() },
                        onClickToggleSystemApps = { mainScreenVm.onToggleDisplaySystemApps() },
                        onClickAbout = { navigateToAbout() }
                    )
                }
            )
        },
        actions = {

        }
    ) { paddingValues ->

        if (uiState.operationMode == OperationMode.NONE) {
            ShizukuRequiredWarning { mainScreenVm.onClickProceedShizuku() }
        }
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            if (uiState.isLoading)
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }

            items(uiState.listOfApps.size) {
                val app = uiState.listOfApps[it]
                AppListItem(
                    packageName = app.appPackageName,
                    appName = app.appName,
                    drawable = app.appIcon,
                    onClickApp = { navigateToAppScreen(app.appPackageName) }
                )
            }
            item { Spacer(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) }
        }
    }

    if (uiState.isSystemAppDialogVisible)
        SystemDialogWarn(
            onClickCancel = { mainScreenVm.onToggleDisplaySystemApps() },
            onClickContinue = { mainScreenVm.toggleSystemAppsVisibility() }
        )
}