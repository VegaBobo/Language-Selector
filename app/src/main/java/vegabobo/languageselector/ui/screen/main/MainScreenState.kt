package vegabobo.languageselector.ui.screen.main

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateListOf

data class MainScreenState(
    val searchTextFieldValue: String = "",
    val isLoading: Boolean = true,
    val isDropdownVisible: Boolean = false,
    val isShowingSystemApps: Boolean = false,
    val isShizukuAvail: Boolean = false,
    val isSearchVisible: Boolean = false,
     val isSystemAppDialogVisible: Boolean = false,
    val isAboutDialogVisible: Boolean = false,
    val listOfApps: MutableList<AppInfo> = mutableStateListOf()
)

data class AppInfo (
    val appIcon: Drawable,
    val appName: String,
    val appPackageName: String,
)

fun PackageManager.getLabel(applicationInfo: ApplicationInfo): String {
    return applicationInfo.loadLabel(this).toString()
}

fun PackageManager.getAppIcon(applicationInfo: ApplicationInfo): Drawable {
    return this.getApplicationIcon(applicationInfo)
}