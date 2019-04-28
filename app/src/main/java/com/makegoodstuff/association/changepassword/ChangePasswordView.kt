package com.makegoodstuff.association.root.ChangePassword

import android.view.LayoutInflater
import android.view.ViewGroup
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.BINARY

class ChangePasswordBuilder(dependency: ParentComponent) : ViewBuilder<ChangePasswordView, ChangePasswordRouter, ChangePasswordBuilder.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): ChangePasswordRouter {
        val view = createView(parentViewGroup)
        val interactor = ChangePasswordInteractor()
        val component = DaggerChangePasswordBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.ChangePasswordRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): ChangePasswordView {
        return inflater.inflate(R.layout.logged_out_view, parentViewGroup, false) as ChangePasswordView
    }

    interface ParentComponent {
        fun ChangePasswordListener(): ChangePasswordInteractor.Listener
    }

    @dagger.Module
    abstract class Module {

        @ChangePasswordScope @Binds
        internal abstract fun presenter(view: ChangePasswordView): ChangePasswordInteractor.ChangePasswordPresenter

        @dagger.Module
        companion object {

            @ChangePasswordScope @Provides @JvmStatic
            internal fun router(
                    component: Component,
                    view: ChangePasswordView,
                    interactor: ChangePasswordInteractor): ChangePasswordRouter {
                return ChangePasswordRouter(
                        view,
                        interactor,
                        component
                )
            }
        }
    }

    @ChangePasswordScope
    @dagger.Component(modules = arrayOf(Module::class, AppModule::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<ChangePasswordInteractor>,
            BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: ChangePasswordInteractor): Builder

            @BindsInstance
            fun view(view: ChangePasswordView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun ChangePasswordRouter(): ChangePasswordRouter
    }

    @Scope
    @Retention(BINARY)
    internal annotation class ChangePasswordScope

    @Qualifier
    @Retention(BINARY)
    internal annotation class ChangePasswordInternal
}
