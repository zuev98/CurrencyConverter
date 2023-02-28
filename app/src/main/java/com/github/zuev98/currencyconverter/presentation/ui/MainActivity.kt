package com.github.zuev98.currencyconverter.presentation.ui

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.zuev98.currencyconverter.databinding.ActivityMainBinding
import com.github.zuev98.currencyconverter.presentation.state.ResponseStatus
import com.github.zuev98.currencyconverter.presentation.ui.adapter.CurrencyAdapter
import com.github.zuev98.currencyconverter.presentation.ui.vm.CurrencyViewModel
import com.github.zuev98.currencyconverter.presentation.util.APP_PREFERENCES
import com.github.zuev98.currencyconverter.presentation.util.APP_PREFERENCES_POSITION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val IS_DATA_SET_KEY = "is data set"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedP: SharedPreferences
    private lateinit var adapter: CurrencyAdapter
    private val currencyViewModel by lazy {
        ViewModelProvider(this)[CurrencyViewModel::class.java]
    }
    private var isDataSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()

        sharedP = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        savedInstanceState?.let {
            isDataSet = it.getBoolean(IS_DATA_SET_KEY)
            setSpinnerPosition()
        }

        if (!isDataSet) {
            setSpinnerPosition()
            loadData()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                currencyViewModel.currency.collect { state ->
                    if (state != null) {
                        when (state.responseStatus) {
                            ResponseStatus.SUCCESS -> {
                                state.data?.let {
                                    adapter.submitList(it)

                                    binding.lastUpdate.text = it[0].lastUpdate
                                    updateSpinner(currencyViewModel.getCurrencyCharCodeList())
                                    convert()
                                }
                            }
                            ResponseStatus.ERROR -> state.message?.let { showError(it) }
                        }
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_DATA_SET_KEY, isDataSet)
    }

    private fun initViews() = with(binding) {
        refreshLayout.setOnRefreshListener {
            loadData()
            refreshLayout.isRefreshing = false
        }

        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = CurrencyAdapter()
        recyclerView.adapter = adapter

        convertImgBtn.setOnClickListener {
            convert()
        }

        spinner.onItemSelectedListener = this@MainActivity
    }

    private fun setSpinnerPosition() {
        val spinnerPosition = sharedP.getInt(APP_PREFERENCES_POSITION, -1)
        if (spinnerPosition != -1)
            binding.spinner.setSelection(spinnerPosition)
    }

    private fun loadData() {
        currencyViewModel.getCurrencies(isConnectivityAvailable())
        isDataSet = true
    }

    private fun isConnectivityAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                return when {
                    capabilities.hasTransport(TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
        return false
    }

    private fun updateSpinner(charCodeList: List<String>) {
        ArrayAdapter(this, android.R.layout.simple_spinner_item, charCodeList)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }
        setSpinnerPosition()
    }

    private fun convert(position: Int = binding.spinner.selectedItemPosition) = with(binding) {
        val currency = currencyViewModel.currency.value?.data?.get(position)

        if (currency != null) {
            val numberOfRub = numberOfRub.text.toString().toDoubleOrNull() ?: 1.0.also {
                numberOfRub.setText("1")
            }

            result.text = String.format("%.2f", numberOfRub * currency.value / currency.nominal)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        sharedP.edit().putInt(APP_PREFERENCES_POSITION, position).apply()
        convert(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) { }

    private fun showError(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
}