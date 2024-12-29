package vegabobo.languageselector.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
    snackBarHost: SnackbarHostState = SnackbarHostState(),
    topBar: (@Composable (TopAppBarScrollBehavior) -> Unit)? = null,
    navIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val defScrollBehavior = topBar != null || title?.isNotEmpty() == true
    val scrollBehavior =
        if (defScrollBehavior)
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        else
            null

    val sbMod =
        if (defScrollBehavior)
            Modifier.nestedScroll(scrollBehavior!!.nestedScrollConnection)
        else
            Modifier

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(sbMod)
            .then(modifier),
        snackbarHost = { SnackbarHost(hostState = snackBarHost) },
        topBar = {
            if (topBar != null) {
                topBar(scrollBehavior!!)
            } else if (title?.isNotEmpty() == true) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    navigationIcon = { navIcon?.invoke() },
                    title = { Text(title) },
                    actions = { actions(this) }
                )
            }
        },
        content = { content(it) }
    )
}