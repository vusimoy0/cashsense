package ru.resodostudios.cashsense.feature.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudios.cashsense.core.designsystem.component.CsAlertDialog
import ru.resodostudios.cashsense.core.designsystem.icon.CsIcons
import ru.resodostudios.cashsense.core.model.data.Category
import ru.resodostudios.cashsense.core.ui.LoadingState
import ru.resodostudios.cashsense.core.ui.StoredIcon
import ru.resodostudios.cashsense.core.ui.validateAmount
import ru.resodostudios.cashsense.feature.categories.CategoriesUiState
import ru.resodostudios.cashsense.feature.categories.CategoriesViewModel
import ru.resodostudios.cashsense.feature.category.CategoryDialog
import ru.resodostudios.cashsense.core.ui.R as uiR

@Composable
fun TransactionDialog(
    onDismiss: () -> Unit,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
) {
    val transactionState by transactionViewModel.transactionUiState.collectAsStateWithLifecycle()
    val categoriesState by categoriesViewModel.categoriesUiState.collectAsStateWithLifecycle()

    TransactionDialog(
        transactionState = transactionState,
        categoriesState = categoriesState,
        onDismiss = onDismiss,
        onTransactionEvent = transactionViewModel::onTransactionEvent,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TransactionDialog(
    transactionState: TransactionUiState,
    categoriesState: CategoriesUiState,
    onDismiss: () -> Unit,
    onTransactionEvent: (TransactionEvent) -> Unit,
) {
    val dialogTitle = if (transactionState.isEditing) R.string.feature_transaction_edit_transaction else R.string.feature_transaction_new_transaction
    val dialogConfirmText = if (transactionState.isEditing) uiR.string.save else uiR.string.add

    CsAlertDialog(
        titleRes = dialogTitle,
        confirmButtonTextRes = dialogConfirmText,
        dismissButtonTextRes = uiR.string.core_ui_cancel,
        iconRes = CsIcons.Transaction,
        onConfirm = {
            onTransactionEvent(TransactionEvent.Save)
            onDismiss()
        },
        isConfirmEnabled = transactionState.amount.validateAmount().second,
        onDismiss = onDismiss,
    ) {
        when (categoriesState) {
            CategoriesUiState.Loading -> LoadingState()
            is CategoriesUiState.Success -> {
                val (descTextField, amountTextField) = remember { FocusRequester.createRefs() }

                var showCategoryDialog by rememberSaveable { mutableStateOf(false) }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                ) {
                    OutlinedTextField(
                        value = transactionState.amount,
                        onValueChange = { onTransactionEvent(TransactionEvent.UpdateAmount(it.validateAmount().first)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next,
                        ),
                        label = { Text(stringResource(uiR.string.amount)) },
                        placeholder = { Text(stringResource(uiR.string.amount) + "*") },
                        supportingText = { Text(stringResource(uiR.string.required)) },
                        maxLines = 1,
                        modifier = Modifier
                            .focusRequester(amountTextField)
                            .focusProperties { next = descTextField },
                    )
                    CategoryExposedDropdownMenuBox(
                        currentCategory = transactionState.category,
                        categories = categoriesState.categories,
                        onCategoryClick = { onTransactionEvent(TransactionEvent.UpdateCategory(it)) },
                        onNewCategoryClick = { showCategoryDialog = true }
                    )
                    OutlinedTextField(
                        value = transactionState.description,
                        onValueChange = { onTransactionEvent(TransactionEvent.UpdateDescription(it)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                        label = { Text(stringResource(uiR.string.description)) },
                        maxLines = 1,
                        modifier = Modifier.focusRequester(descTextField),
                    )
                }
                if (showCategoryDialog) {
                    CategoryDialog(
                        onDismiss = { showCategoryDialog = false }
                    )
                }
                LaunchedEffect(Unit) {
                    if (!transactionState.isEditing) {
                        amountTextField.requestFocus()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryExposedDropdownMenuBox(
    currentCategory: Category?,
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    onNewCategoryClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    var iconId by rememberSaveable {
        mutableIntStateOf(currentCategory?.iconId ?: 0)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = currentCategory?.title ?: stringResource(uiR.string.none),
            onValueChange = {},
            label = { Text(stringResource(ru.resodostudios.cashsense.feature.category.R.string.feature_category_title)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(StoredIcon.asRes(iconId)),
                    contentDescription = null
                )
            },
            maxLines = 1,
            singleLine = true,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(ru.resodostudios.cashsense.feature.category.R.string.feature_category_new_category),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                onClick = {
                    onNewCategoryClick()
                    expanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(CsIcons.Add),
                        contentDescription = null
                    )
                },
            )
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.title.toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    onClick = {
                        onCategoryClick(category)
                        iconId = category.iconId ?: 0
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(StoredIcon.asRes(category.iconId ?: 0)),
                            contentDescription = null,
                        )
                    },
                )
            }
        }
    }
}