package vegabobo.languageselector.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import vegabobo.languageselector.R
import vegabobo.languageselector.ui.components.AppListItem
import vegabobo.languageselector.ui.screen.BaseScreen

@Composable
fun MainScreen(
    mainScreenVm: MainScreenVm = hiltViewModel(),
    navigateToAppScreen: (String) -> Unit,
    navigateToAbout: () -> Unit,
) {
    val uiState by mainScreenVm.uiState.collectAsState()

    BaseScreen(
        screenTitle = stringResource(R.string.app_name),
        actions = {
            MainTopBarActions(
                isOnSearchMode = uiState.isSearchVisible,
                isDropdownVisible = uiState.isDropdownVisible,
                isShowingSystemApps = uiState.isShowingSystemApps,
                searchQuery = uiState.searchTextFieldValue,
                onClickToggleSearch = { mainScreenVm.toggleSearch() },
                onClickToggleDropdown = { mainScreenVm.toggleDropdown() },
                onSearchTextChange = { mainScreenVm.onSearchTextFieldChange(it) },
                onToggleDropdown = { mainScreenVm.toggleDropdown() },
                onClickToggleSystemApps = { mainScreenVm.onToggleDisplaySystemApps() },
                onClickAbout = { navigateToAbout() }
            )
        }
    ) { paddingValues ->
        if (!uiState.isShizukuAvail) {
            ShizukuRequiredWarning { mainScreenVm.onClickProceedShizuku() }
        }
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            if (uiState.isLoading)
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }

            val filteredApps = uiState.listOfApps.filter {
                uiState.searchTextFieldValue == "" ||
                        it.appPackageName.lowercase().contains(mainScreenVm.searchQuery.value.lowercase()) ||
                        it.appName.lowercase().contains(mainScreenVm.searchQuery.value.lowercase())
            }

            items(filteredApps.size) {
                val app = filteredApps[it]
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

    if (uiState.isSearchVisible)
        BackHandler { mainScreenVm.toggleSearch() }
}