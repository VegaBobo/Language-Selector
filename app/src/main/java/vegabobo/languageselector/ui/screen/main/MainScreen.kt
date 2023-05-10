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
import androidx.compose.ui.platform.LocalContext
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
    val pm = LocalContext.current.packageManager

    BaseScreen(
        screenTitle = stringResource(R.string.app_name),
        actions = {
            MainTopBarActions(
                isOnSearchMode = uiState.isSearchVisible,
                isDropdownVisible = uiState.isDropdownVisible,
                isShowingSystemApps = uiState.isShowingSystemApps,
                searchQuery = uiState.searchQuery,
                onClickToggleSearch = { mainScreenVm.toggleSearch() },
                onClickToggleDropdown = { mainScreenVm.toggleDropdown() },
                onSearchTextChange = { mainScreenVm.onSearchTextFieldChange(it) },
                onToggleDropdown = { mainScreenVm.toggleDropdown() },
                onClickToggleSystemApps = { mainScreenVm.onClickToggleSystemApps() },
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
            items(uiState.listOfApps.size) {
                val thisPackage = uiState.listOfApps[it]
                val appName = pm.getLabel(thisPackage)
                val packageName = thisPackage.packageName
                val appIconDrawable = pm.getAppIcon(thisPackage)
                val containsInSearchQuery =
                    packageName.lowercase().contains(uiState.searchQuery.lowercase()) ||
                            appName.lowercase().contains(uiState.searchQuery.lowercase())
                if (uiState.searchQuery == "" || containsInSearchQuery)
                    AppListItem(
                        packageName = packageName,
                        appName = appName,
                        drawable = appIconDrawable,
                        onClickApp = { navigateToAppScreen(packageName) }
                    )
            }
            item { Spacer(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) }
        }
    }

    if (uiState.isSearchVisible)
        BackHandler { mainScreenVm.toggleSearch() }
}