package com.makegoodstuff.demoapp.root.loggedout

import com.google.common.base.Optional
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
class LoggedOutInteractor : Interactor<LoggedOutInteractor.LoggedOutPresenter, LoggedOutRouter>() {

    @Inject lateinit var presenter: LoggedOutPresenter

    @Inject @field:Named("mainThreadScheduler") lateinit var mainThreadScheduler: Scheduler
    @Inject lateinit var lifecycleEvent: Observable<ActivityLifecycleEvent>

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
    }

    interface LoggedOutPresenter

    interface Listener
}
