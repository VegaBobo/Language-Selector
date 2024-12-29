package vegabobo.languageselector.ui.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import vegabobo.languageselector.R
import vegabobo.languageselector.ui.components.AppListItem
import vegabobo.languageselector.ui.components.AppSearchBar
import vegabobo.languageselector.ui.screen.BaseScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    mainScreenVm: MainScreenVm = hiltViewModel(),
    navigateToAppScreen: (String) -> Unit,
    navigateToAbout: () -> Unit,
) {
    val uiState by mainScreenVm.uiState.collectAsState()
    val sb = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        mainScreenVm.reloadLastSelectedItem()
        mainScreenVm.uiState.collectLatest {
            when (it.snackBarDisplay) {
                SnackBarDisplay.MOVED_TO_TOP -> {
                    val r = sb.showSnackbar(
                        message = "Modified app has been moved up",
                        actionLabel = "Navigate"
                    )
                    if (r == SnackbarResult.ActionPerformed) {
                        val i =
                            mainScreenVm.getIndexFromAppInfoItem() + 1 /* first item is a spacer */
                        lazyListState.animateScrollToItem(i)
                    }
                }

                SnackBarDisplay.MOVED_TO_BOTTOM -> {
                    val r = sb.showSnackbar(
                        message = "Unmodified has been moved down",
                        actionLabel = "Navigate"
                    )
                    if (r == SnackbarResult.ActionPerformed) {
                        val i =
                            mainScreenVm.getIndexFromAppInfoItem() + 1 /* first item is a spacer */
                        lazyListState.animateScrollToItem(i)
                    }
                }

                else -> {}
            }
            mainScreenVm.resetSnackBarDisplay()
        }
    }
    BaseScreen(snackBarHost = sb) {
        if (uiState.isLoading)
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        else {
            Box(
                Modifier
                    .fillMaxSize()
                    .semantics { isTraversalGroup = true }) {
                AppSearchBar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .semantics { traversalIndex = 0f },
                    placeholder = stringResource(R.string.search),
                    onUpdatedValue = { mainScreenVm.onSearchTextFieldChange(it) },
                    query = uiState.searchTextFieldValue,
                    onClickApp = { mainScreenVm.onClickApp(it); navigateToAppScreen(it.pkg) },
                    history = uiState.history,
                    apps = uiState.listOfApps,
                    isExpanded = uiState.isExpanded,
                    onExpandedChange = { mainScreenVm.onSearchExpandedChange() },
                    selectedLabels = uiState.selectLabels,
                    onSelectedLabelsChange = { mainScreenVm.onSelectedLabelChange(it) },
                    onClickClear = { mainScreenVm.onClickClear() },
                    actions = {
                        if (!uiState.isExpanded)
                            SearchBarActions(
                                isDropdownVisible = uiState.isDropdownVisible,
                                isShowingSystemApps = uiState.isShowSystemAppsHome,
                                onClickToggleDropdown = { mainScreenVm.toggleDropdown() },
                                onToggleDropdown = { mainScreenVm.toggleDropdown() },
                                onClickToggleSystemApps = { mainScreenVm.toggleSystemAppsVisibility() },
                                onClickAbout = { navigateToAbout() }
                            )
                    })

                if (uiState.operationMode == OperationMode.NONE) {
                    ShizukuRequiredWarning { mainScreenVm.onClickProceedShizuku() }
                }

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.semantics { traversalIndex = 1f }
                ) {
                    item {
                        Spacer(
                            Modifier
                                .statusBarsPadding()
                                .padding(top = 72.dp) /* 64 + 10 */
                        )
                    }
                    items(uiState.listOfApps.size) {
                        val thisApp = uiState.listOfApps[it]
                        if (!uiState.isShowSystemAppsHome && thisApp.isSystemApp() && !thisApp.isModified())
                            return@items
                        AppListItem(
                            modifier = Modifier.padding(
                                start = 26.dp,
                                end = 26.dp,
                                top = 4.dp,
                                bottom = 4.dp
                            ),
                            app = thisApp,
                            onClickApp = {
                                mainScreenVm.onClickApp(thisApp)
                                navigateToAppScreen(it)
                            }
                        )
                    }
                }
            }
        }
    }
}