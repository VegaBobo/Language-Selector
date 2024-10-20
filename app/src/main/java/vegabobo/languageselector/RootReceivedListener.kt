package vegabobo.languageselector


interface IRootListener {
    fun onRootReceived()
}

object RootReceivedListener {
    var callback: IRootListener? = null

    fun setListener(inCallback: IRootListener?) {
        callback = inCallback
    }

    fun onRootReceived() {
        callback?.onRootReceived()
    }

    fun destroy() {
        callback = null
    }
}