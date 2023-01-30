package com.github.zuev98.currencyconverter.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.zuev98.currencyconverter.data.entity.Currency
import com.github.zuev98.currencyconverter.databinding.ListItemCurrencyBinding

class CurrencyAdapter :
    ListAdapter<Currency, CurrencyAdapter.CurrencyViewHolder>(Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCurrencyBinding.inflate(inflater, parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = getItem(position)
        holder.bind(currency)
    }

    class CurrencyViewHolder(
        private val binding: ListItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currency: Currency) {
            binding.apply {
                name.text = currency.name
                charCode.text = currency.charCode
                nominal.text = currency.nominal.toString()
                value.text = java.text.DecimalFormat("0.0000").format(currency.value)
            }
        }
    }
}

private class Comparator : DiffUtil.ItemCallback<Currency>() {
    override fun areItemsTheSame(
        oldItem: Currency,
        newItem: Currency
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Currency,
        newItem: Currency
    ): Boolean {
        return oldItem == newItem
    }
}
