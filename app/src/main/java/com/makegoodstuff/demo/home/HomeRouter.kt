package com.makegoodstuff.demoapp.root.loggedin.home

import com.makegoodstuff.demoapp.root.loggedin.home.ChangePasswordBuilder
import com.makegoodstuff.demoapp.root.loggedin.home.ChangePasswordRouter
import com.uber.rib.core.ViewRouter

class HomeRouter(
        view: HomeView,
        interactor: HomeInteractor,
        component: HomeBuilder.Component,
        private val changePasswordBuilder: ChangePasswordBuilder) : ViewRouter<HomeView, HomeInteractor, HomeBuilder.Component>(view, interactor, component) {

    private var changePasswordRouter: ChangePasswordRouter? = null

    fun attachChangePassword() {
        if (changePasswordRouter == null) {
            changePasswordRouter = changePasswordBuilder.build(view)
            attachChild(changePasswordRouter)
        }
        view.addView(changePasswordRouter?.view)
    }

    fun detatchChangePassword() {
        detachChild(changePasswordRouter)
        view.removeView(changePasswordRouter?.view)
        changePasswordRouter = null
    }

    val isChangePasswordAttached: Boolean get() = changePasswordRouter != null

    fun handleBackPressChangePassword(): Boolean = changePasswordRouter?.handleBackPress() ?: false
}
