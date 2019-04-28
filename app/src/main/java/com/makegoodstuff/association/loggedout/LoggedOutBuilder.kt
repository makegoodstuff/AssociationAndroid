package com.makegoodstuff.association.root.loggedout

import android.view.LayoutInflater
import android.view.ViewGroup
import com.makegoodstuff.association.ActivityModule
import com.makegoodstuff.association.AppModule
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

class LoggedOutBuilder(dependency: ParentComponent) : ViewBuilder<LoggedOutView, LoggedOutRouter, LoggedOutBuilder.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): LoggedOutRouter {
        val view = createView(parentViewGroup)
        val interactor = LoggedOutInteractor()
        val component = DaggerLoggedOutBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.loggedoutRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): LoggedOutView? {
        return LoggedOutView(parentViewGroup.context)
    }

    interface ParentComponent

    @dagger.Module
    abstract class Module {

        @LoggedOutScope @Binds
        internal abstract fun presenter(view: LoggedOutView): LoggedOutInteractor.LoggedOutPresenter

        @dagger.Module
        companion object {

            @LoggedOutScope @Provides @JvmStatic
            internal fun router(
                    component: Component,
                    view: LoggedOutView,
                    interactor: LoggedOutInteractor): LoggedOutRouter {
                return LoggedOutRouter(
                        view,
                        interactor,
                        component)
            }

            @LoggedOutScope @Provides @JvmStatic
            internal fun lifecycleEvent(lifecycleScopeProvider: LifecycleScopeProvider<ActivityLifecycleEvent>): Observable<ActivityLifecycleEvent> {
                return lifecycleScopeProvider.lifecycle()
            }
        }
    }

    @LoggedOutScope
    @dagger.Component(modules = arrayOf(Module::class, AppModule::class, ActivityModule::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<LoggedOutInteractor>,
            BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: LoggedOutInteractor): Builder

            @BindsInstance
            fun view(view: LoggedOutView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun loggedoutRouter(): LoggedOutRouter
    }

    @Scope
    @Retention(BINARY)
    internal annotation class LoggedOutScope

    @Qualifier
    @Retention(BINARY)
    internal annotation class LoggedOutInternal
}
