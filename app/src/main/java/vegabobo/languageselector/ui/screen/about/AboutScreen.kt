package vegabobo.languageselector.ui.screen.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import vegabobo.languageselector.R
import vegabobo.languageselector.ui.components.BackButton
import vegabobo.languageselector.ui.components.Title
import vegabobo.languageselector.ui.screen.BaseScreen
import vegabobo.languageselector.ui.screen.main.getAppIcon
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withContext
import vegabobo.languageselector.BuildConfig

@Composable
fun AboutScreen(
    navigateBack: () -> Unit
) {
    val libs = remember { mutableStateOf<Libs?>(null) }
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    libs.value = Libs.Builder().withContext(context).build()
    val libraries = libs.value!!.libraries

    BaseScreen(
        screenTitle = stringResource(R.string.about),
        navIcon = { BackButton { navigateBack() } }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(96.dp),
                        bitmap = context.packageManager
                            .getAppIcon(context.applicationInfo)
                            .toBitmap().asImageBitmap(),
                        contentDescription = "App icon"
                    )
                    Text(text = stringResource(R.string.app_name), fontSize = 22.sp)
                    Text(
                        stringResource(R.string.version).format(
                            BuildConfig.VERSION_NAME,
                            BuildConfig.VERSION_CODE
                        )
                    )
                }
            }
            item {
                Title(stringResource(id = R.string.app))
                PreferenceItem(
                    title = stringResource(R.string.ghrepo),
                    description = stringResource(R.string.view_source)
                ) {
                    uriHandler.openUri("https://github.com/VegaBobo/Language-Selector")
                }
            }
            item { Title(stringResource(R.string.deps_libs)) }
            items(libraries.size) {
                val thisLibrary = libraries[it]
                val name = thisLibrary.name
                var licenses = ""
                for (license in thisLibrary.licenses) {
                    licenses += license.name
                }
                val urlToOpen = thisLibrary.website ?: ""
                PreferenceItem(
                    title = name,
                    description = licenses,
                    onClick = {
                        if (urlToOpen.isNotEmpty()) {
                            uriHandler.openUri(urlToOpen)
                        }
                    },
                )
            }
            item { Spacer(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) }
        }
    }

}

@Composable
fun PreferenceItem(
    title: String,
    description: String,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                start = 24.dp,
                top = 16.dp,
                bottom = 16.dp,
                end = 16.dp
            )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
            )
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}