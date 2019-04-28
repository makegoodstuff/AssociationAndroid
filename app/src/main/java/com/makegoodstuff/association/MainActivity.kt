package com.makegoodstuff.association

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.makegoodstuff.association.root.RootBuilder
import com.makegoodstuff.association.root.RootInteractor
import com.makegoodstuff.association.root.RootRouter
import com.uber.autodispose.AutoDisposePlugins
import com.uber.rib.core.Router
import com.uber.rib.core.ViewRouter
import dagger.android.AndroidInjection
import javax.inject.Inject

interface RibHost {
    val rootRouter: Router<*, *>?
}

class MainActivity : RxActivity(), RibHost {

    private val TAG = MainActivity::class.java.simpleName

    @Inject
    lateinit var rootBuilder: RootBuilder

    override var rootRouter: RootRouter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedActivity = this
        AndroidInjection.inject(this)

        suppressAutodisposeExceptions()

        super.onCreate(savedInstanceState)
    }

    private fun suppressAutodisposeExceptions() {
        AutoDisposePlugins.setOutsideLifecycleHandler {
            App.sharedInstance().config.serviceConfig.clientLogService.logger(TAG).debug(
                    "Caught and suppressed OutsideLifecycleException: ${it.localizedMessage}"
            )
        }
    }

    override fun onDestroy() {
        sharedActivity = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    // RibActivity
    override fun createRouter(parentViewGroup: ViewGroup?): ViewRouter<*, *, *> {
        rootRouter = rootBuilder.build(parentViewGroup!!, intent.dataString ?: "")
        return rootRouter as RootRouter
    }

    inner class RootListener : RootInteractor.Listener {
        override fun removeFragmentByTag(tag: String) {
            val fragment = supportFragmentManager.findFragmentByTag(tag) ?: return
            supportFragmentManager.beginTransaction().remove(fragment).commitNowAllowingStateLoss()
        }
    }

    companion object {
        private var sharedActivity: MainActivity? = null

        fun sharedActivity(): MainActivity? {
            return sharedActivity
        }

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
