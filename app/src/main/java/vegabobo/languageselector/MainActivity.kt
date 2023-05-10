package vegabobo.languageselector

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import vegabobo.languageselector.service.UserService
import vegabobo.languageselector.service.UserServiceProvider
import vegabobo.languageselector.ui.screen.Navigation
import vegabobo.languageselector.ui.theme.LanguageSelector
import dagger.hilt.android.AndroidEntryPoint
import rikka.shizuku.Shizuku


@AndroidEntryPoint
class MainActivity : ComponentActivity(), Shizuku.OnRequestPermissionResultListener {

    val userServiceArgs =
        Shizuku.UserServiceArgs(
            ComponentName(BuildConfig.APPLICATION_ID, UserService::class.java.name),
        )
            .daemon(false)
            .processNameSuffix("service")
            .debuggable(BuildConfig.DEBUG)
            .version(BuildConfig.VERSION_CODE)

    val acRequestCode = 1

    fun bindShizuku() {
        Shizuku.bindUserService(userServiceArgs, UserServiceProvider.connection)
    }

    private val REQUEST_PERMISSION_RESULT_LISTENER = this::onRequestPermissionResult

    override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
        if (grantResult == PackageManager.PERMISSION_GRANTED)
            bindShizuku()
    }

    override fun onDestroy() {
        Shizuku.removeRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER)
        super.onDestroy()
    }

    private fun checkPermission(code: Int): Boolean {
        return if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            bindShizuku()
            true
        } else if (Shizuku.shouldShowRequestPermissionRationale()) {
            false
        } else {
            Shizuku.requestPermission(code)
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            LanguageSelector { Navigation() }
        }

        if (Shizuku.pingBinder() && savedInstanceState == null) {
            Shizuku.addRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER)
            checkPermission(acRequestCode)
        }
    }

}