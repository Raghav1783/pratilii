package com.example.pratilii.presentation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class appRoute {

    abstract val baseRoute: String

    open val arguments: List<NamedNavArgument>
        get() = emptyList()

    val route: String
        get() = baseRoute + if (arguments.isNotEmpty()) {
            "/" + arguments.joinToString("/") { "{${it.name}}" }
        } else {
            ""
        }


    fun withArgument(vararg args: String): String {
        return baseRoute + if (args.isNotEmpty()) {
            "/" + args.joinToString("/")
        } else {
            ""
        }
    }


    object Home : appRoute() {
        override val baseRoute = "home"
    }

    object MailDetail : appRoute() {
        override val baseRoute = "mail_detail"
        override val arguments = listOf(navArgument("id") { type = NavType.IntType })
    }
}