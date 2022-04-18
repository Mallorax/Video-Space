package pl.patrykzygo.videospace.ui

import android.app.Activity
import android.content.ComponentName
import android.net.Uri
import androidx.browser.customtabs.*

class ChromeCustomTab(private val activity: Activity, private val callback: CustomTabsCallback) {

    private var customTabSession: CustomTabsSession? = null
    private var client: CustomTabsClient? = null
    private var connection: CustomTabsServiceConnection? = null


    companion object {
        const val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    }

    init {
        bindCustomTabsService()
    }

    fun warmup() {
        client?.let { client!!.warmup(0) }
    }

    fun mayLaunch(url: String) {
        val session = getSession()
        client?.let {
            session?.mayLaunchUrl(Uri.parse(url), null, null)
        }
    }

    fun show(url: String) {
        val builder = CustomTabsIntent.Builder(getSession())
        val intent = builder.build()

        intent.launchUrl(activity, Uri.parse(url))
    }


    fun unbindCustomTabsService() {
        connection?.let {
            activity.unbindService(connection!!)
            client = null
            customTabSession = null
        }
    }

    private fun getSession(): CustomTabsSession? {
        if (client == null) {
            customTabSession = null
        } else if (customTabSession == null) {
            customTabSession = client!!.newSession(callback)
        }
        return customTabSession
    }

    private fun bindCustomTabsService() {
        if (client != null) {
            return
        }
        connection = object : CustomTabsServiceConnection() {

            override fun onServiceDisconnected(name: ComponentName?) {
                this@ChromeCustomTab.client = null
            }

            override fun onCustomTabsServiceConnected(
                name: ComponentName,
                client: CustomTabsClient
            ) {
                this@ChromeCustomTab.client = client
            }
        }

        val isOk =
            CustomTabsClient.bindCustomTabsService(activity, CUSTOM_TAB_PACKAGE_NAME, connection!!)
        if (!isOk) {
            connection = null
        }
    }


}