package ru.resodostudios.cashsense.feature.transaction.overview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudios.cashsense.core.designsystem.component.CsAlertDialog
import ru.resodostudios.cashsense.core.designsystem.icon.CsIcons
import ru.resodostudios.cashsense.core.designsystem.icon.outlined.Delete
import ru.resodostudios.cashsense.core.locales.R
import ru.resodostudios.cashsense.core.ui.component.LoadingState
import ru.resodostudios.cashsense.core.ui.component.TransactionBottomSheet
import ru.resodostudios.cashsense.core.ui.transactions

@Composable
internal fun TransactionOverviewScreen(
    showNavigationIcon: Boolean,
    onBackClick: () -> Unit,
    onTransactionClick: (walletId: String, transactionId: String?, repeated: Boolean) -> Unit,
    viewModel: TransactionOverviewViewModel = hiltViewModel(),
) {
    val transactionOverviewState by viewModel.transactionOverviewUiState.collectAsStateWithLifecycle()

    TransactionOverviewScreen(
        transactionOverviewState = transactionOverviewState,
        showNavigationIcon = showNavigationIcon,
        onBackClick = onBackClick,
        onTransactionClick = onTransactionClick,
        updateTransactionId = viewModel::updateTransactionId,
        onUpdateTransactionIgnoring = viewModel::updateTransactionIgnoring,
        onDeleteTransaction = viewModel::deleteTransaction,
    )
}

@Composable
private fun TransactionOverviewScreen(
    transactionOverviewState: TransactionOverviewUiState,
    showNavigationIcon: Boolean,
    onBackClick: () -> Unit,
    onTransactionClick: (walletId: String, transactionId: String?, repeated: Boolean) -> Unit,
    updateTransactionId: (String) -> Unit = {},
    onUpdateTransactionIgnoring: (Boolean) -> Unit = {},
    onDeleteTransaction: () -> Unit = {},
) {
    when (transactionOverviewState) {
        TransactionOverviewUiState.Loading -> LoadingState(Modifier.fillMaxSize())
        is TransactionOverviewUiState.Success -> {
            var showTransactionBottomSheet by rememberSaveable { mutableStateOf(false) }
            var showTransactionDeletionDialog by rememberSaveable { mutableStateOf(false) }

            if (showTransactionBottomSheet && transactionOverviewState.selectedTransactionCategory != null) {
                val transaction = transactionOverviewState.selectedTransactionCategory.transaction
                TransactionBottomSheet(
                    transactionCategory = transactionOverviewState.selectedTransactionCategory,
                    currency = transactionOverviewState.selectedTransactionCategory.transaction.currency,
                    onDismiss = { showTransactionBottomSheet = false },
                    onIgnoreClick = onUpdateTransactionIgnoring,
                    onRepeatClick = { transactionId ->
                        onTransactionClick(transaction.walletOwnerId, transactionId, true)
                    },
                    onEdit = { transactionId ->
                        onTransactionClick(transaction.walletOwnerId, transactionId, false)
                    },
                    onDelete = { showTransactionDeletionDialog = true },
                )
            }
            if (showTransactionDeletionDialog) {
                CsAlertDialog(
                    titleRes = R.string.permanently_delete,
                    confirmButtonTextRes = R.string.delete,
                    dismissButtonTextRes = R.string.cancel,
                    icon = CsIcons.Outlined.Delete,
                    onConfirm = {
                        onDeleteTransaction()
                        showTransactionDeletionDialog = false
                    },
                    onDismiss = { showTransactionDeletionDialog = false },
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 88.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                transactions(
                    transactionsCategories = transactionOverviewState.transactionsCategories,
                    onTransactionClick = {
                        updateTransactionId(it)
                        showTransactionBottomSheet = true
                    },
                )
            }
        }
    }
}