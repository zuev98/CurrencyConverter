package com.github.zuev98.currencyconverter.presentation.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zuev98.currencyconverter.data.entity.Currency
import com.github.zuev98.currencyconverter.domain.usecases.GetCurrenciesUseCase
import com.github.zuev98.currencyconverter.presentation.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : ViewModel() {
    private var _currency = MutableLiveData<State<List<Currency>>>()
    val currency: LiveData<State<List<Currency>>>
        get() = _currency

    fun getCurrencies(isRemote: Boolean) {
        viewModelScope.launch {
            _currency.value = loadCurrencies(isRemote)
        }
    }

    private suspend fun loadCurrencies(isRemote: Boolean): State<List<Currency>> =
        try {
            State.onSuccess(getCurrenciesUseCase.getCurrencies(isRemote))
        } catch (e: IOException) {
            State.onError(e.message)
        } catch (e: Exception) {
            State.onError(e.message)
        }

    fun getCurrencyCharCodeList(): List<String> {
        val list = mutableListOf<String>()
        _currency.value?.data?.forEach {
            list.add(it.charCode)
        }

        return list
    }
}