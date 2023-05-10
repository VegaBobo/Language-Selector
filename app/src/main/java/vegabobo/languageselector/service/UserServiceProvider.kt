package vegabobo.languageselector.service

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vegabobo.languageselector.IUserService

object UserServiceProvider {

    private val tag = this.javaClass.simpleName

    var connection = Connection()

    fun run(
        onFail: () -> Unit = {},
        onConnected: suspend IUserService.() -> Unit,
    ) {
        fun service() = connection.SERVICE!!
        CoroutineScope(Dispatchers.IO).launch {
            if (isConnected()) {
                onConnected(service())
                return@launch
            }
            var timeout = 0
            while (!isConnected()) {
                timeout += 1000
                if (timeout > 20000) {
                    Log.e(tag, "Service unavailable.")
                    onFail()
                    return@launch
                }
                delay(1000)
                Log.d(tag, "Service unavailable, checking again in 1s.. [${timeout / 1000}s/20s]")
            }
            Log.d(tag, "IUserService available, uid: ${service().uid}")
            onConnected(service())
        }
    }

    fun isConnected(): Boolean {
        return connection.SERVICE != null
    }
}
