package vegabobo.languageselector.ui.screen.main

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku
import vegabobo.languageselector.BuildConfig
import javax.inject.Inject


@HiltViewModel
class MainScreenVm @Inject constructor(
    val app: Application
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    fun refreshShizukuAvail() {
        val isAvail = Shizuku.pingBinder() &&
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        if (isAvail)
            _uiState.update { it.copy(isShizukuAvail = true) }
    }

    init {
        fillListOfApps()
        refreshShizukuAvail()
    }

    fun fillListOfApps(getAlsoSystemApps: Boolean = false) {
        _uiState.value.listOfApps.clear()
        viewModelScope.launch(Dispatchers.IO) {
            val packageList = getInstalledPackages(getAlsoSystemApps).map { it }
            val sortedList = packageList.sortedBy { app.packageManager.getLabel(it).lowercase() }
            _uiState.value.listOfApps.addAll(sortedList)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun getInstalledPackages(getAlsoSystemApps: Boolean = false): List<ApplicationInfo> {
        return app.packageManager.getInstalledApplications(
            PackageManager.ApplicationInfoFlags.of(0)
        ).mapNotNull {
            if (!it.enabled)
                null
            else if (BuildConfig.APPLICATION_ID == it.packageName)
                null
            else if (getAlsoSystemApps)
                it
            else
                if (it.flags and ApplicationInfo.FLAG_SYSTEM == 0)
                    it
                else
                    null

        }
    }

    fun toggleDropdown() {
        val newDropdownVisibility = !uiState.value.isDropdownVisible
        _uiState.update { it.copy(isDropdownVisible = newDropdownVisibility) }
    }

    fun toggleSystemAppsVisibility() {
        val newShowSystemApps = !uiState.value.isShowingSystemApps
        _uiState.update {
            it.copy(
                isSystemAppDialogVisible = false,
                isLoading = true,
                isShowingSystemApps = newShowSystemApps
            )
        }
        fillListOfApps(newShowSystemApps)
        toggleDropdown()
    }

    fun onToggleDisplaySystemApps() {
        if (!uiState.value.isShowingSystemApps) {
            val newSystemDialogWarnVisibility = !uiState.value.isSystemAppDialogVisible
            _uiState.update { it.copy(isSystemAppDialogVisible = newSystemDialogWarnVisibility) }
        } else {
            toggleSystemAppsVisibility()
            _uiState.update { it.copy(isShowingSystemApps = false) }
        }
    }

    fun onClickProceedShizuku() {
        refreshShizukuAvail()
    }

    fun toggleSearch() {
        val newSearchVisibility = !uiState.value.isSearchVisible
        if (!newSearchVisibility)
            _uiState.update { it.copy(searchTextFieldValue = "") }
        _uiState.update { it.copy(isSearchVisible = newSearchVisibility) }
    }


    val searchQuery = mutableStateOf("")
    private val handler = Handler(Looper.getMainLooper())
    private var workRunnable: Runnable? = null

    fun onSearchTextFieldChange(newText: String) {
        _uiState.update { it.copy(searchTextFieldValue = newText) }

        if (workRunnable != null)
            handler.removeCallbacks(workRunnable!!)

        workRunnable = Runnable { searchQuery.value = newText }
        handler.postDelayed(workRunnable!!, 1000)
    }

}
