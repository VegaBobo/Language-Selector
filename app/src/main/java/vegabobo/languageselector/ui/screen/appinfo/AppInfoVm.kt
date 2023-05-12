package vegabobo.languageselector.ui.screen.appinfo

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.LocaleList
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import vegabobo.languageselector.LocaleManager
import vegabobo.languageselector.service.UserServiceProvider
import vegabobo.languageselector.ui.screen.main.getAppIcon
import vegabobo.languageselector.ui.screen.main.getLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import vegabobo.languageselector.BuildConfig
import java.util.Locale
import javax.inject.Inject

object PrefConstants {
    const val PINNED_LOCALES = "pinned_locales"
}


@HiltViewModel
class AppInfoVm @Inject constructor(
    val app: Application,
    val localeManager: LocaleManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppInfoState())
    val uiState: StateFlow<AppInfoState> = _uiState.asStateFlow()

    lateinit var appInfo: ApplicationInfo

    fun initFromAppId(appId: String) {
        appInfo =
            app.packageManager.getApplicationInfo(appId, PackageManager.ApplicationInfoFlags.of(0))
        _uiState.update {
            it.copy(
                appName = app.packageManager.getLabel(appInfo),
                appPackage = appInfo.packageName,
                appIcon = app.packageManager.getAppIcon(appInfo)
            )
        }

        UserServiceProvider.run {
            _uiState.value.listOfSuggestedLanguages.clear()
            for (locale in 0 until systemLocales.size()) {
                val thisLocale = systemLocales[locale]
                val thisLLI =
                    SingleLocale(thisLocale.capDisplayName(), thisLocale.toLanguageTag())
                _uiState.value.listOfSuggestedLanguages.add(thisLLI)
                updateCurrentLanguageState()
            }
        }

        _uiState.update { it.copy(listOfAllLanguages = localeManager.localeList) }
    }

    fun updateCurrentLanguageState() {
        UserServiceProvider.run {
            val currentLocale = getApplicationLocales(appInfo.packageName)
            if (!currentLocale.isEmpty)
                _uiState.update { it.copy(currentLanguage = currentLocale.get(0).capDisplayName()) }
        }
    }

    fun onClickSingleLanguage(index: Int) {
        _uiState.update { it.copy(selectedLanguage = index) }
    }

    fun onBackWhenSelectedLang() {
        _uiState.update { it.copy(selectedLanguage = -1) }
    }

    fun onClickLocale(singleLocale: SingleLocale) {
        UserServiceProvider.run {
            setApplicationLocales(
                appInfo.packageName,
                LocaleList(singleLocale.toLocale())
            )
            updateCurrentLanguageState()
        }
    }

    fun onClickSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", appInfo.packageName, null)
        intent.setData(uri)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }

    fun onClickOpen() {
        val launchIntent =
            app.packageManager.getLaunchIntentForPackage(appInfo.packageName)
        launchIntent?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ?: return
        app.startActivity(launchIntent)
    }

    fun onClickResetLang() {
        UserServiceProvider.run {
            setApplicationLocales(appInfo.packageName, LocaleList())
            updateCurrentLanguageState()
            _uiState.update { it.copy(currentLanguage = "")}
        }
    }

    fun onClickForceClose() {
        UserServiceProvider.run {
            forceStopPackage(appInfo.packageName)
        }
    }

    fun getSp(): SharedPreferences =
        app.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    fun onPinLang(singleLocale: SingleLocale) {
        val sp = getSp()
        val set = sp.getStringSet(PrefConstants.PINNED_LOCALES, emptySet()) ?: emptySet()
        val mset = set.toMutableSet()
        mset.add("${singleLocale.name},${singleLocale.languageTag}")
        sp.edit().putStringSet(PrefConstants.PINNED_LOCALES, mset).apply()
        updatePinnedLangsFromSP()
    }

    fun onRemovePin(singleLocale: SingleLocale) {
        val sp = getSp()
        val set = sp.getStringSet(PrefConstants.PINNED_LOCALES, emptySet()) ?: emptySet()
        val newSet = mutableSetOf<String>()
        set.forEach {
            if (!it.contains(singleLocale.languageTag))
                newSet.add(it)
        }
        sp.edit().putStringSet(PrefConstants.PINNED_LOCALES, newSet).apply()
        updatePinnedLangsFromSP()
    }

    fun updatePinnedLangsFromSP() {
        val sp = getSp()
        val set = sp.getStringSet(PrefConstants.PINNED_LOCALES, emptySet()) ?: return
        val pinnedLocaleList = set.mapNotNull {
            try {
                val stringLocale = it.split(",")
                val name = stringLocale[0]
                val tag = stringLocale[1]
                SingleLocale(name, tag)
            } catch (e: Exception) {
                Log.e(BuildConfig.APPLICATION_ID, e.stackTraceToString())
                null
            }
        }.toMutableList()
        _uiState.update { it.copy(listOfPinnedLanguages = pinnedLocaleList) }
    }

}

fun Locale.capDisplayName(): String {
    return this.getDisplayName(this).replaceFirstChar { it.uppercaseChar() }
}