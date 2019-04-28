package com.makegoodstuff.demoapp.root

import android.view.LayoutInflater
import android.view.ViewGroup
import com.makegoodstuff.demoapp.ActivityModule
import com.makegoodstuff.demoapp.App
import com.makegoodstuff.demoapp.AppModule
import com.makegoodstuff.demoapp.BuildConfig
import com.makegoodstuff.demoapp.BuildType
import com.makegoodstuff.demoapp.R
import com.makegoodstuff.demoapp.config.Config
import com.makegoodstuff.demoapp.navigation.NavigatorComponent
import com.makegoodstuff.demoapp.root.loggedin.LoggedInBuilder
import com.makegoodstuff.demoapp.root.loggedout.LoggedOutBuilder
import com.makegoodstuff.demoapp.root.loggedout.LoggedOutInteractor
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import com.uber.rib.core.lifecycle.ActivityLifecycleEvent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import io.reactivex.Observable
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.BINARY

/**
 * Builder for the {@link RootScope}.
 */
class RootBuilder(dependency: ParentComponent) : ViewBuilder<RootView, RootRouter, RootBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [RootRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @param appOpenUrl the Activity's Intent data url string.
     * @return a new [RootRouter].
     */
    fun build(parentViewGroup: ViewGroup, appOpenUrl: String): RootRouter {
        val view = createView(parentViewGroup)
        val interactor = RootInteractor()
        val component = DaggerRootBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .appOpenUrl(appOpenUrl)
                .interactor(interactor)
                .build()
        return component.rootRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): RootView? {
        return inflater.inflate(R.layout.root_view, parentViewGroup, false) as RootView
    }

    interface ParentComponent {
        fun rootListener(): RootInteractor.Listener
    }

    @dagger.Module
    abstract class Module {

        @RootScope @Binds
        internal abstract fun presenter(view: RootView): RootInteractor.RootPresenter

        @dagger.Module
        companion object {

            @RootScope @Provides @JvmStatic
            internal fun router(
                    component: Component,
                    view: RootView,
                    interactor: RootInteractor): RootRouter {
                return RootRouter(
                        view,
                        interactor,
                        component,
                        LoggedInBuilder(component),
                        LoggedOutBuilder(component))
            }

            @RootScope @Provides @JvmStatic
            internal fun config(): Config = App.sharedInstance().config

            @RootScope @Provides @JvmStatic
            internal fun loggedOutListener(interactor: RootInteractor): LoggedOutInteractor.Listener = interactor.LoggedOutListener()

            @RootScope @Provides @JvmStatic @Named("loggerTag")
            internal fun loggerTag(): String = RootInteractor::class.java.simpleName

            @RootScope @Provides @JvmStatic @Named("sessionId")
            internal fun sessionId(): String = App.sharedInstance().sessionId

            @RootScope @Provides @JvmStatic @Named("rootComponent")
            internal fun rootComponent(): NavigatorComponent = App.sharedInstance().config.componentConfig.rootNavigator
        }
    }

    @RootScope
    @dagger.Component(modules = arrayOf(Module::class, AppModule::class, ActivityModule::class), dependencies = arrayOf(ParentComponent::class))
    interface Component
        : InteractorBaseComponent<RootInteractor>,
            BuilderComponent,
            LoggedInBuilder.ParentComponent,
            LoggedOutBuilder.ParentComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: RootInteractor): Builder

            @BindsInstance
            fun view(view: RootView): Builder

            @BindsInstance
            fun appOpenUrl(@Named("appOpenUrl") url: String): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun rootRouter(): RootRouter
    }

    @Scope
    @Retention(BINARY)
    internal annotation class RootScope

    @Qualifier
    @Retention(BINARY)
    internal annotation class RootInternal
}
