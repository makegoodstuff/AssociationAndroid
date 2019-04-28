package com.makegoodstuff.demoapp.root.loggedin

import android.view.LayoutInflater
import android.view.ViewGroup
import com.makegoodstuff.demoapp.ActivityModule
import com.makegoodstuff.demoapp.AppModule
import com.makegoodstuff.demoapp.root.loggedin.home.HomeBuilder
import com.makegoodstuff.demoapp.root.loggedin.home.HomeInteractor
import com.makegoodstuff.demoapp.root.loggedin.rootnavigator.RootNavigatorBuilder
import com.uber.autodispose.LifecycleScopeProvider
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

class LoggedInBuilder(dependency: ParentComponent) : ViewBuilder<LoggedInView, LoggedInRouter, LoggedInBuilder.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): LoggedInRouter {
        val view = createView(parentViewGroup)
        val interactor = LoggedInInteractor()
        val component = DaggerLoggedInBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.loggedinRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): LoggedInView? {
        return LoggedInView(parentViewGroup.context)
    }

    interface ParentComponent

    @dagger.Module
    abstract class Module {

        @LoggedInScope @Binds
        internal abstract fun presenter(view: LoggedInView): LoggedInInteractor.LoggedInPresenter

        @dagger.Module
        companion object {

            @LoggedInScope @Provides @JvmStatic
            internal fun router(
                    component: Component,
                    view: LoggedInView,
                    interactor: LoggedInInteractor): LoggedInRouter {
                return LoggedInRouter(
                        view,
                        interactor,
                        component,
                        HomeBuilder(component),
                        RootNavigatorBuilder(component))
            }

            @LoggedInScope @Provides @JvmStatic
            internal fun homeListener(interactor: LoggedInInteractor): HomeInteractor.Listener {
                return interactor.HomeListener()
            }

            @LoggedInScope @Provides @JvmStatic
            internal fun lifecycleEvent(lifecycleScopeProvider: LifecycleScopeProvider<ActivityLifecycleEvent>): Observable<ActivityLifecycleEvent> {
                return lifecycleScopeProvider.lifecycle()
            }
        }
    }

    @LoggedInScope
    @dagger.Component(modules = arrayOf(Module::class, AppModule::class, ActivityModule::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<LoggedInInteractor>,
            BuilderComponent,
            HomeBuilder.ParentComponent,
            RootNavigatorBuilder.ParentComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: LoggedInInteractor): Builder

            @BindsInstance
            fun view(view: LoggedInView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun loggedinRouter(): LoggedInRouter
    }

    @Scope
    @Retention(BINARY)
    internal annotation class LoggedInScope

    @Qualifier
    @Retention(BINARY)
    internal annotation class LoggedInInternal
}
