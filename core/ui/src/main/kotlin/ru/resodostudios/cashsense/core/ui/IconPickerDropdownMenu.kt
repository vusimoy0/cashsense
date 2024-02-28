package ru.resodostudios.cashsense.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import ru.resodostudios.cashsense.core.designsystem.icon.CsIcons

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IconPickerDropdownMenu(
    @DrawableRes
    currentIconId: Int,
    onIconClick: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = ImageVector.vectorResource(StoredIcon.asRes(currentIconId)),
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            FlowRow(maxItemsInEachRow = 5) {
                StoredIcon.entries.forEach { icon ->
                    IconButton(
                        onClick = {
                            onIconClick(icon.storedId)
                            expanded = false
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(icon.iconId),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

enum class StoredIcon(
    @DrawableRes
    val iconId: Int,
    val storedId: Int,
) {
    CATEGORY(CsIcons.Category, 0),
    ACCOUNT_BALANCE(CsIcons.AccountBalance, 1),
    APPAREL(CsIcons.Apparel, 2),
    CHAIR(CsIcons.Chair, 3),
    EXERCISE(CsIcons.Exercise,4),
    FASTFOOD(CsIcons.Fastfood, 5),
    DIRECTIONS_BUS(CsIcons.DirectionsBus, 6),
    HANDYMAN(CsIcons.Handyman, 7),
    LANGUAGE(CsIcons.Language, 8),
    LOCAL_BAR(CsIcons.LocalBar, 9),
    LOCAL_GAS_STATION(CsIcons.LocalGasStation, 10),
    MEMORY(CsIcons.Memory, 11),
    PAYMENTS(CsIcons.Payments, 12),
    PETS(CsIcons.Pets, 13),
    PHISHING(CsIcons.Phishing, 14),
    PILL(CsIcons.Pill, 14),
    TRANSACTION(CsIcons.Transaction, 15),
    RESTAURANT(CsIcons.Restaurant, 16),
    SCHOOL(CsIcons.School, 17),
    SELF_CARE(CsIcons.SelfCare, 18),
    SHOPPING_CART(CsIcons.ShoppingCart, 19),
    SIM_CARD(CsIcons.SimCard, 20),
    SMOKING_ROOMS(CsIcons.SmokingRooms, 21),
    SPORTS_ESPORTS(CsIcons.SportsEsports, 22),
    TRAVEL(CsIcons.Travel, 23);

    companion object {

        @DrawableRes
        fun asRes(stored: Int): Int = entries.first { it.storedId == stored }.iconId
    }
}