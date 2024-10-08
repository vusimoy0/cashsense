package ru.resodostudios.cashsense.navigation

import ru.resodostudios.cashsense.R
import ru.resodostudios.cashsense.core.designsystem.icon.CsIcons
import ru.resodostudios.cashsense.feature.category.list.navigation.CategoriesRoute
import ru.resodostudios.cashsense.feature.home.navigation.HomeRoute
import ru.resodostudios.cashsense.feature.settings.navigation.SettingsGraph
import ru.resodostudios.cashsense.feature.subscription.list.navigation.SubscriptionsRoute
import kotlin.reflect.KClass
import ru.resodostudios.cashsense.core.locales.R as localesR

enum class TopLevelDestination(
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val iconTextId: Int,
    val titleTextId: Int,
    val fabIcon: Int?,
    val fabTitle: Int?,
    val route: KClass<*>,
) {
    HOME(
        selectedIcon = CsIcons.HomeFilled,
        unselectedIcon = CsIcons.Home,
        iconTextId = localesR.string.home_title,
        titleTextId = R.string.app_name,
        fabIcon = CsIcons.Wallet,
        fabTitle = localesR.string.new_wallet,
        route = HomeRoute::class,
    ),
    CATEGORIES(
        selectedIcon = CsIcons.CategoryFilled,
        unselectedIcon = CsIcons.Category,
        iconTextId = localesR.string.categories_title,
        titleTextId = localesR.string.categories_title,
        fabIcon = CsIcons.Add,
        fabTitle = localesR.string.new_category,
        route = CategoriesRoute::class,
    ),
    SUBSCRIPTIONS(
        selectedIcon = CsIcons.AutoRenew,
        unselectedIcon = CsIcons.AutoRenew,
        iconTextId = localesR.string.subscriptions_title,
        titleTextId = localesR.string.subscriptions_title,
        fabIcon = CsIcons.Add,
        fabTitle = localesR.string.new_subscription,
        route = SubscriptionsRoute::class,
    ),
    SETTINGS(
        selectedIcon = CsIcons.SettingsFilled,
        unselectedIcon = CsIcons.Settings,
        iconTextId = localesR.string.settings_title,
        titleTextId = localesR.string.settings_title,
        fabIcon = null,
        fabTitle = null,
        route = SettingsGraph::class,
    )
}