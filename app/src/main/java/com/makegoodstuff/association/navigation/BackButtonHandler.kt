package com.makegoodstuff.association.navigation

import androidx.fragment.app.Fragment
import com.makegoodstuff.association.AppModule
import com.makegoodstuff.association.service.NavigationService

/**
 * An optional interface to be implemented by Fragments in order to receive
 * notifications when the back button is pressed.
 *
 * A fragment that does not implement this interface will receive standard
 * default behavior.
 */
interface BackButtonHandler {
    /**
     * Called when the back button has been pressed and the implementing
     * fragment is frontmost.
     */
    fun onBack()
}

object BackButtonNavigationHandler {
    fun standardHandler(component: NavigatorComponent,
                        displayedFragment: Fragment?,
                        navigationService: NavigationService) {
        when (displayedFragment) {
            is BackButtonHandler -> {
                displayedFragment.onBack()
            }
            else -> {
                if (navigationService.observeRoute(component)
                        .blockingFirst()
                        .size > 1) {
                    navigationService.pop(component)
                } else {
                    AppModule.backButtonBehaviorService().doubleBackToExit()
                }
            }
        }
    }
}