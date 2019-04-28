
package com.makegoodstuff.demoapp.navigation

import com.makegoodstuff.demoapp.config.FragmentBuilder

enum class TransitionAnimationKey {
    None, Default, Modal
}

interface Component {
    val name: String
    val builder: FragmentBuilder
    val transitionAnimation: TransitionAnimationKey
    val uniqueId: String
}

interface NavigatorComponent : Component {
    val initialComponents: List<Component> get
}

data class SimpleComponent(
        override val name: String,
        override val builder: FragmentBuilder,
        override val transitionAnimation: TransitionAnimationKey) : Component {
    override val uniqueId: String = name
}

data class IdentifierComponent(
        override val name: String,
        override val builder: FragmentBuilder,
        override val transitionAnimation: TransitionAnimationKey,
        val identifier: String) : Component {
    override val uniqueId get() = "${name}__$identifier"
}

data class FlatNavigatorComponent(
        override val name: String,
        override val builder: FragmentBuilder,
        override val transitionAnimation: TransitionAnimationKey,
        override val initialComponents: List<Component>) : NavigatorComponent {
    override val uniqueId: String = name
}

data class TabNavigatorComponent(
        override val name: String,
        override val builder: FragmentBuilder,
        override val transitionAnimation: TransitionAnimationKey,
        override val initialComponents: List<Component>,
        val tabIndex: Int?) : NavigatorComponent {
    override val uniqueId get() = "${name}_$tabIndex"
}

