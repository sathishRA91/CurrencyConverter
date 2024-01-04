package com.rasathish.currencyconversion.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rasathish.currencyconversion.R
import com.rasathish.currencyconversion.databinding.ItemCurrencyBinding
import com.rasathish.currencyconversion.domain.model.CurrencyModel

/**
 * Created by sathish on 03,January,2024
 */
class CurrencyViewAdapter(private val context: Context,private var currencyList:List<CurrencyModel>) :
    RecyclerView.Adapter<CurrencyViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding: ItemCurrencyBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_currency,
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val currencyItem = currencyList[i]

        viewHolder.itemRowBinding.itemCurrencyModel = currencyItem
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    inner class ViewHolder(var itemRowBinding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {}

    fun updateData(currencyData:List<CurrencyModel>)
    {
        this.currencyList=currencyData
        notifyDataSetChanged()

    }
}