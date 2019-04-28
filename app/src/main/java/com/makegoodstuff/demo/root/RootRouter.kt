package com.makegoodstuff.demoapp.root

import com.makegoodstuff.demoapp.root.loggedin.LoggedInBuilder
import com.makegoodstuff.demoapp.root.loggedin.LoggedInRouter
import com.makegoodstuff.demoapp.root.loggedout.LoggedOutBuilder
import com.makegoodstuff.demoapp.root.loggedout.LoggedOutRouter
import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * Root router for app. Should only have attached one of: LoggedIn, LoggedOut
 */
class RootRouter(
        view: RootView,
        interactor: RootInteractor,
        component: RootBuilder.Component,
        private val loggedInBuilder: LoggedInBuilder,
        private val loggedOutBuilder: LoggedOutBuilder
) : ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component) {

    private var loggedInRouter: LoggedInRouter? = null
    private var loggedOutRouter: LoggedOutRouter? = null

    fun attachLoggedIn() {
        if (loggedInRouter == null) {
            loggedInRouter = loggedInBuilder.build(view)
            attachChild(loggedInRouter)
            view.content.addView(loggedInRouter?.view)
        }
    }

    fun detachLoggedIn() {
        if (loggedInRouter != null) {
            detachChild(loggedInRouter)
            view.content.removeView(loggedInRouter?.view)
            loggedInRouter = null
        }
    }

    fun handleBackPressLoggedIn() = loggedInRouter?.handleBackPress() ?: false

    fun attachLoggedOut() {
        if (loggedOutRouter == null) {
            loggedOutRouter = loggedOutBuilder.build(view)
            attachChild(loggedOutRouter)
            view.content.addView(loggedOutRouter?.view)
        }
    }

    fun detachLoggedOut() {
        if (loggedOutRouter != null) {
            detachChild(loggedOutRouter)
            view.content.removeView(loggedOutRouter?.view)
            loggedOutRouter = null
        }
    }

    fun handleBackPressLoggedOut() = loggedOutRouter?.handleBackPress() ?: false
}

