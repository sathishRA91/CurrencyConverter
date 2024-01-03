package com.rasathish.currencyconversion.presentation.currencyselection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasathish.currencyconversion.R
import com.rasathish.currencyconversion.databinding.ActivityCurrencySelectionBinding
import com.rasathish.currencyconversion.presentation.currencyselection.adapter.CurrencySelectionAdapter
import com.rasathish.currencyconversion.presentation.currencyselection.model.CurrencySelectionModel
import com.rasathish.currencyconversion.presentation.home.CurrencyItemModel
import com.rasathish.currencyconversion.presentation.home.adapter.CurrencyViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by sathish on 03,January,2024
 */
@AndroidEntryPoint
class CurrencySelectionActivity:AppCompatActivity()
{

    private lateinit var activityCurrencySelectionBinding:ActivityCurrencySelectionBinding
    private lateinit var currencySelectionViewModel: CurrencySelectionViewModel
    private lateinit var currencySelectionAdapter: CurrencySelectionAdapter
    private val currencyList = ArrayList<CurrencySelectionModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCurrencySelectionBinding=DataBindingUtil.setContentView(this, R.layout.activity_currency_selection)
        currencySelectionViewModel=ViewModelProvider(this)[CurrencySelectionViewModel::class.java]
        activityCurrencySelectionBinding.currencySelectionModel=currencySelectionViewModel
        activityCurrencySelectionBinding.lifecycleOwner=this
        activityCurrencySelectionBinding.executePendingBindings()

        initCurrencySelection()
    }

    private fun initCurrencySelection()
    {
        activityCurrencySelectionBinding.RvCurrencySelectionView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launchWhenStarted {

            currencySelectionViewModel.getCurrencyList.collectLatest {

                if (it != null && it.size > 0) {
                    currencyList.addAll(it)
                    currencySelectionAdapter = CurrencySelectionAdapter(this@CurrencySelectionActivity, currencyList)
                    activityCurrencySelectionBinding.RvCurrencySelectionView.adapter = currencySelectionAdapter
                }

            }
        }

        activityCurrencySelectionBinding.EtCurrencySelection.addTextChangedListener {

            currencyFilter(it.toString())
        }

        activityCurrencySelectionBinding.IvBackPress.setOnClickListener {
            finish()
        }
    }

    private fun currencyFilter(searchKey:String)
    {
      val currencySearchList = currencyList.map { it }.filter {
            it.currencyCode.lowercase(Locale.ENGLISH).contains(searchKey.lowercase(Locale.getDefault()))
        }

        currencySelectionAdapter.updateData(currencySearchList)
    }
}