package com.makegoodstuff.demoapp.root.loggedin

import com.google.common.base.Optional
import com.makegoodstuff.demoapp.root.loggedin.HomeInteractor
import com.makegoodstuff.util.isActivityReady
import com.uber.autodispose.kotlin.autoDisposable
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import com.uber.rib.core.lifecycle.ActivityLifecycleEvent
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

@RibInteractor
class LoggedInInteractor : Interactor<LoggedInInteractor.LoggedInPresenter, LoggedInRouter>() {

    @Inject lateinit var presenter: LoggedInPresenter

    @Inject @field:Named("mainThreadScheduler") lateinit var mainThreadScheduler: Scheduler
    @Inject lateinit var lifecycleEvent: Observable<ActivityLifecycleEvent>

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        lifecycleEvent
                .filter { it.type.isActivityReady }
                .take(1)
                .subscribe {
                    router.attachHome()
                }
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface LoggedInPresenter

    /**
     * Listener interface implemented by a parent RIB's interactor's inner class.
     */
    interface Listener

    inner class HomeListener : HomeInteractor.Listener {
        override fun finishedHome() {
            router.detachHome()
            router.attachLoggedOut(true, true)
        }
    }
}
