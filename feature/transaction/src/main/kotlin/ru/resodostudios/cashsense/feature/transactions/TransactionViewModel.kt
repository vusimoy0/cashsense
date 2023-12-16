package ru.resodostudios.cashsense.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.resodostudios.cashsense.core.data.repository.TransactionRepository
import ru.resodostudios.cashsense.core.model.data.Transaction
import ru.resodostudios.cashsense.core.model.data.TransactionCategoryCrossRef
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    fun upsertTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.upsertTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
        }
    }

    fun upsertTransactionCategoryCrossRef(crossRef: TransactionCategoryCrossRef) {
        viewModelScope.launch {
            transactionRepository.upsertTransactionCategoryCrossRef(crossRef)
        }
    }
}