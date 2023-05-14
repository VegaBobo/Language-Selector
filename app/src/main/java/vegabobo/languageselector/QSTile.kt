package vegabobo.languageselector

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.LocaleList
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import rikka.shizuku.Shizuku
import vegabobo.languageselector.service.UserServiceProvider
import vegabobo.languageselector.ui.screen.appinfo.PrefConstants
import vegabobo.languageselector.ui.screen.appinfo.SingleLocale
import vegabobo.languageselector.ui.screen.appinfo.capDisplayName
import vegabobo.languageselector.ui.screen.appinfo.parseSetLangs
import vegabobo.languageselector.ui.screen.main.getLabel


class QSTile : TileService() {

    private var isLoaded = false
    private val locales = mutableListOf<SingleLocale>()
    private lateinit var targetPackage: ApplicationInfo

    private fun getNextSingleLocale(localeList: LocaleList): SingleLocale {
        if (locales.isEmpty())
            throw Exception("getNextSingleLocale() should be not called with empty MutableList<SingleLocale> locales")
        if (localeList.isEmpty)
            return locales[1]
        for (i in 0 until locales.size) {
            val thisLocale = locales[i]
            if (localeList[0].toLanguageTag() == thisLocale.languageTag) {
                if (i == locales.size - 1) {
                    return locales.first()
                }
                return locales[i + 1]
            }
        }
        return locales.first()
    }

    private fun setDisabledTile() {
        qsTile.label = getString(R.string.app_name)
        qsTile.subtitle = getString(R.string.unavailable)
        qsTile.state = Tile.STATE_UNAVAILABLE
        qsTile.updateTile()
    }

    private fun updateTile() {
        UserServiceProvider.run {
            val currentAppPackage = firstRunningTaskPackage
            targetPackage =
                packageManager.getApplicationInfo(
                    currentAppPackage,
                    PackageManager.ApplicationInfoFlags.of(0)
                )
            if ((targetPackage.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
                // Prevent system apps to have their language override from QS
                setDisabledTile()
                return@run
            }
            var isCustomLocale = false
            val currentLocale =
                try {
                    val appLocales = getApplicationLocales(currentAppPackage)
                    if (!appLocales.isEmpty) {
                        isCustomLocale = true
                        appLocales[0].capDisplayName()
                    } else {
                        ""
                    }
                } catch (e: Exception) {
                    ""
                }.ifBlank { getString(R.string.system_default) }
            qsTile.label = currentLocale
            qsTile.subtitle = packageManager.getLabel(targetPackage)
            qsTile.state = if (isCustomLocale) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            qsTile.updateTile()
        }
    }

    fun loadLangs() {
        if (!isLoaded) {
            val sp = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
            val set = sp.getStringSet(PrefConstants.PINNED_LOCALES, emptySet()) ?: emptySet()
            if (set.isNotEmpty()) {
                val systemDefaultLocale = SingleLocale("", "")
                locales.add(systemDefaultLocale)
                locales.addAll(set.parseSetLangs())
            }
            isLoaded = true
        }
    }

    override fun onTileAdded() {
        if (BuildConfig.DEBUG)
            Log.d(BuildConfig.APPLICATION_ID, "QSTile onTileAdded()")
        super.onTileAdded()
    }

    override fun onStartListening() {
        if (BuildConfig.DEBUG)
            Log.d(BuildConfig.APPLICATION_ID, "QSTile onStartListening()")

        super.onStartListening()
        setDisabledTile()

        try {
            if (!UserServiceProvider.isConnected())
                Shizuku.bindUserService(ShizukuArgs.userServiceArgs, UserServiceProvider.connection)
        } catch (e: Exception) {
            Log.e(
                BuildConfig.APPLICATION_ID,
                "Cannot bind UserService, non-fatal because it happened on QSTile.\n" + e.stackTraceToString()
            )
            return
        }

        loadLangs()
        if (locales.isNotEmpty())
            updateTile()
    }

    override fun onStopListening() {
        if (BuildConfig.DEBUG)
            Log.d(BuildConfig.APPLICATION_ID, "QSTile onStopListening()")
        isLoaded = false
        locales.clear()
        if (UserServiceProvider.isConnected())
            Shizuku.unbindUserService(
                ShizukuArgs.userServiceArgs,
                UserServiceProvider.connection,
                true
            )
        super.onStopListening()
    }

    override fun onClick() {
        if (BuildConfig.DEBUG)
            Log.d(BuildConfig.APPLICATION_ID, "QSTile onClick()")

        super.onClick()

        if (!this::targetPackage.isInitialized)
            return

        UserServiceProvider.run {
            val currentLocale = getApplicationLocales(targetPackage.packageName)
            try {
                Log.d(BuildConfig.APPLICATION_ID, "QSTile: ${currentLocale.isEmpty}")
            } catch (e: Exception) {
                Log.d(BuildConfig.APPLICATION_ID, e.stackTraceToString())
            }
            val nextLocale = getNextSingleLocale(currentLocale)
            val localeList =
                if (nextLocale.languageTag.isEmpty())
                    LocaleList()
                else
                    LocaleList(nextLocale.toLocale())
            setApplicationLocales(targetPackage.packageName, localeList)
            updateTile()
        }
    }

    override fun onTileRemoved() {
        if (BuildConfig.DEBUG)
            Log.d(BuildConfig.APPLICATION_ID, "QSTile onTileRemoved()")
        super.onTileRemoved()
    }
}