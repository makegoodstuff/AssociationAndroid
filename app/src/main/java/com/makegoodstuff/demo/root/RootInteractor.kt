package com.makegoodstuff.demoapp.root

import com.makegoodstuff.demoapp.R
import com.makegoodstuff.demoapp.navigation.NavigatorComponent
import com.makegoodstuff.demoapp.root.loggedin.rootnavigator.RootNavigatorInteractor
import com.makegoodstuff.demoapp.root.loggedout.LoggedOutInteractor
import com.makegoodstuff.demoapp.service.AuthService
import com.makegoodstuff.demoapp.service.BackButtonBehaviorService
import com.makegoodstuff.demoapp.service.IntentLauncher
import com.makegoodstuff.demoapp.service.NavigationService
import com.uber.autodispose.kotlin.autoDisposable
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Coordinates Business Logic for [RootScope].
 */
@RibInteractor
class RootInteractor : Interactor<RootInteractor.RootPresenter, RootRouter>() {

    @Inject lateinit var presenter: RootPresenter
    @Inject lateinit var listener: Listener

    @Inject lateinit var authService: AuthService
    @Inject lateinit var backButtonBehaviorService: BackButtonBehaviorService
    @Inject lateinit var intentLauncher: IntentLauncher
    @Inject @field:Named("mainThreadScheduler") lateinit var mainThreadScheduler: Scheduler

    // Temporary until NavigationService goes away
    @Inject lateinit var navigationService: NavigationService
    @Inject @field:Named("rootComponent") lateinit var rootComponent: NavigatorComponent
    @Inject @field:Named("appOpenUrl") lateinit var appOpenUrl: String
    @Inject @field:Named("sessionId") lateinit var sessionId: String
    @Inject @JvmField @field:Named("shouldShowSessionId") var shouldShowSessionId = false

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
    }

    override fun handleBackPress(): Boolean {
        if (router.handleBackPressLoggedIn() || router.handleBackPressLoggedOut()) return true

        // Double back by default
        backButtonBehaviorService.doubleBackToExit()
        return true
    }

    private fun attachNextRouter() {
        // Check if authenticated on launch and route to LoggedIn or LoggedOut
        authService.isSignedIn
            .take(1) // just once, on launch
            .observeOn(mainThreadScheduler)
            .autoDisposable(this)
            .subscribe { isSignedIn ->
                if (isSignedIn) {
                    router.attachLoggedIn()
                } else {
                    router.attachLoggedOut()
                }
            }
    }

    private fun setupLoggedOutHandler() {
        // Hide main fragment and display LoggedOut if no longer signed in
        authService.isSignedIn
            .skip(1) // ignore initial state
            .filter { !it }
            .observeOn(mainThreadScheduler)
            .autoDisposable(this)
            .subscribe {
                router.detachLoggedIn()
                navigationService.resetToRootComponent(rootComponent, hideSplashScreen = true)
                router.attachLoggedOut()
            }
    }

    interface Listener {
        fun removeFragmentByTag(tag: String)
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RootPresenter {}

    inner class LoggedOutListener : LoggedOutInteractor.Listener {
        override fun loggedIn() {
            router.detachLoggedOut()
            router.attachLoggedIn()
        }
    }
}
