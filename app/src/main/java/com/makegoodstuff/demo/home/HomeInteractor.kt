package com.makegoodstuff.demoapp.root.loggedin.home

import com.makegoodstuff.demoapp.root.loggedin.home.ChangePasswordInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import javax.inject.Inject
import javax.inject.Named

@RibInteractor
class HomeInteractor : Interactor<HomeInteractor.HomePresenter, HomeRouter>() {

    @Inject lateinit var listener: Listener
    @Inject lateinit var presenter: HomePresenter

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
    }

    override fun handleBackPress(): Boolean {
        return router.handleBackPressChangePassword()
    }

    interface Listener {
        fun loggedOut()
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface HomePresenter

    inner class ChangePasswordListener : ChangePasswordInteractor.Listener {
        override fun finishChangePassword() {
            router.detachChangePassword()
        }
    }
}
