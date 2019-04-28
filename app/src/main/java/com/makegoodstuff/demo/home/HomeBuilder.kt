package com.makegoodstuff.demoapp.root.loggedin.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.makegoodstuff.demoapp.AppModule
import com.makegoodstuff.demoapp.R
import com.makegoodstuff.demoapp.root.loggedin.home.ChangePasswordBuilder
import com.makegoodstuff.demoapp.root.loggedin.home.ChangePasswordInteractor
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.BINARY

class HomeBuilder(dependency: ParentComponent) : ViewBuilder<HomeView, HomeRouter, HomeBuilder.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): HomeRouter {
        val view = createView(parentViewGroup)
        val interactor = HomeInteractor()
        val component = DaggerHomeBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.homeRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): HomeView? {
        return inflater.inflate(R.layout.home_view, parentViewGroup, false) as HomeView
    }

    interface ParentComponent {
        fun homeListener(): HomeInteractor.Listener
    }

    @dagger.Module
    abstract class Module {

        @HomeScope
        @Binds
        internal abstract fun presenter(view: HomeView): HomeInteractor.HomePresenter

        @dagger.Module
        companion object {

            @HomeScope @Provides @JvmStatic
            internal fun router(
                    component: Component,
                    view: HomeView,
                    interactor: HomeInteractor): HomeRouter {
                return HomeRouter(view, interactor, component,
                        ChangePasswordBuilder(component))
            }

            @HomeScope @Provides @JvmStatic
            internal fun changePasswordListener(interactor: ChangePasswordInteractor): ChangePasswordInteractor.Listener {
                return interactor.ChangePasswordListener()
            }
        }
    }

    @HomeScope
    @dagger.Component(modules = arrayOf(Module::class, AppModule::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<HomeInteractor>, BuilderComponent,
        HomePhoneBuilder.ParentComponent,
        HomeCodeBuilder.ParentComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: HomeInteractor): Builder

            @BindsInstance
            fun view(view: HomeView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun homeRouter(): HomeRouter
    }

    @Scope
    @Retention(BINARY)
    internal annotation class HomeScope

    @Qualifier
    @Retention(BINARY)
    internal annotation class HomeInternal
}
