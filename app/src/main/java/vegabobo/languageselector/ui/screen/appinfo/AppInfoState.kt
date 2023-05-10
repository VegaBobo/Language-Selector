package vegabobo.languageselector.ui.screen.appinfo

import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateListOf
import java.util.Locale

data class LocaleRegion(
    val language: String,
    val locales: ArrayList<SingleLocale>
)

data class SingleLocale(
    val name: String,
    val languageTag: String
) {
    fun toLocale(): Locale {
        return Locale.forLanguageTag(languageTag)
    }
}

data class AppInfoState(
    val appIcon: Drawable? = null,
    val appName: String = "",
    val appPackage: String = "",
    val currentLanguage: String = "",
    val listOfSuggestedLanguages: MutableList<SingleLocale> = mutableStateListOf(),
    val listOfPinnedLanguages: MutableList<SingleLocale> = mutableStateListOf(),
    val selectedLanguage: Int = -1,
    val listOfAllLanguages: MutableList<LocaleRegion> = mutableStateListOf(),
)