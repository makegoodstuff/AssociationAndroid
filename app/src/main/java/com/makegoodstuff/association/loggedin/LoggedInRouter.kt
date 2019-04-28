package com.makegoodstuff.association.root.loggedin

import com.makegoodstuff.association.root.loggedin.newuser.HomeBuilder
import com.makegoodstuff.association.root.loggedin.newuser.HomeRouter
import com.makegoodstuff.association.root.loggedin.rootnavigator.RootNavigatorBuilder
import com.makegoodstuff.association.root.loggedin.rootnavigator.RootNavigatorRouter
import com.uber.rib.core.ViewRouter

class LoggedInRouter(
        view: LoggedInView,
        interactor: LoggedInInteractor,
        component: LoggedInBuilder.Component,
        private val homeBuilder: HomeBuilder,
        private val rootNavigatorBuilder: RootNavigatorBuilder
)
    : ViewRouter<LoggedInView, LoggedInInteractor, LoggedInBuilder.Component>(view, interactor, component) {

    private var homeRouter: HomeRouter? = null
    private var rootNavigator: RootNavigatorRouter? = null

    fun attachHome() {
        if (homeRouter != null) return
        homeRouter = homeBuilder.build(view).also {
            attachChild(it)
            view.addView(it.view)
        }
    }

    fun detachHome() {
        homeRouter?.also {
            detachChild(it)
            view.removeView(it.view)
        }

        homeRouter = null
    }

    fun handleBackPressHome() = homeRouter?.handleBackPress() ?: false

    fun attachRootNavigator() {
        if (rootNavigator != null) return
        rootNavigator = rootNavigatorBuilder.build(view).also {
            view.addView(it.view)
            attachChild(it)
        }
    }

    fun isOnlyRootNavigatorAttached(): Boolean {
        return rootNavigator != null && homeRouter == null
    }

    fun handleBackPressRootNavigator() = rootNavigator?.handleBackPress() ?: false
}
