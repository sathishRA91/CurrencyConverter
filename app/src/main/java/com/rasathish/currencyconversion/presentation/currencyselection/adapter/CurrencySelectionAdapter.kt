package com.rasathish.currencyconversion.presentation.currencyselection.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rasathish.currencyconversion.R
import com.rasathish.currencyconversion.databinding.ItemCurrencySelectionBinding
import com.rasathish.currencyconversion.domain.model.CurrencyModel
import com.rasathish.currencyconversion.presentation.currencyselection.CurrencySelectionActivity


/**
 * Created by sathish on 03,January,2024
 */
class CurrencySelectionAdapter(private val context: Context, private var currencyList:List<CurrencyModel>) :
    RecyclerView.Adapter<CurrencySelectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding: ItemCurrencySelectionBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_currency_selection,
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val currencyItem = currencyList[i]

        viewHolder.itemView.setOnClickListener {

            val activity= context as CurrencySelectionActivity
            val goHome=Intent()
            goHome.putExtra("currencyCode",currencyItem.base)
            goHome.putExtra("currencyAmount",currencyItem.rate)
            activity.setResult(Activity.RESULT_OK, goHome)
            activity.finish()
        }

        viewHolder.itemRowBinding.itemCurrencyModel = currencyItem
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    inner class ViewHolder(var itemRowBinding: ItemCurrencySelectionBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {}

    fun updateData(currencyData:List<CurrencyModel>)
    {
        this.currencyList=currencyData
        notifyDataSetChanged()

    }

}