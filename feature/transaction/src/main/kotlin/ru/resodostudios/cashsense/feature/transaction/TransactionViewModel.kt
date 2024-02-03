package ru.resodostudios.cashsense.feature.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.resodostudios.cashsense.core.data.repository.TransactionsRepository
import ru.resodostudios.cashsense.core.model.data.Transaction
import ru.resodostudios.cashsense.core.model.data.TransactionCategoryCrossRef
import ru.resodostudios.cashsense.feature.transaction.navigation.TransactionArgs
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionsRepository: TransactionsRepository,
) : ViewModel() {

    private val transactionArgs: TransactionArgs = TransactionArgs(savedStateHandle)
    private val transactionId: String? = transactionArgs.transactionId
    private val walletId: String = transactionArgs.walletId

    private val _transactionUiState = MutableStateFlow(TransactionUiState())
    val transactionUiState = _transactionUiState.asStateFlow()

    fun upsertTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionsRepository.upsertTransaction(transaction)
            if (transaction.categoryId != null) {
                transactionsRepository.deleteTransactionCategoryCrossRef(transaction.id)
                transactionsRepository.upsertTransactionCategoryCrossRef(
                    TransactionCategoryCrossRef(
                        transactionId = transaction.id,
                        categoryId = transaction.categoryId!!
                    )
                )
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionsRepository.deleteTransaction(transaction)
        }
    }
}