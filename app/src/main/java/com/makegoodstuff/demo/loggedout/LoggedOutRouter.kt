package com.makegoodstuff.demoapp.root.loggedout

import com.uber.rib.core.ViewRouter

class LoggedOutRouter(
        view: LoggedOutView,
        interactor: LoggedOutInteractor,
        component: LoggedOutBuilder.Component
    ) : ViewRouter<LoggedOutView, LoggedOutInteractor, LoggedOutBuilder.Component>(view, interactor, component) {
}
