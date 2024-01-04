package com.rasathish.currencyconversion.presentation.currencyselection

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasathish.currencyconversion.R
import com.rasathish.currencyconversion.databinding.ActivityCurrencySelectionBinding
import com.rasathish.currencyconversion.domain.model.CurrencyModel
import com.rasathish.currencyconversion.presentation.currencyselection.adapter.CurrencySelectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by sathish on 04,January,2024
 */
@AndroidEntryPoint
class CurrencySelectionActivity:AppCompatActivity()
{

    private lateinit var activityCurrencySelectionBinding:ActivityCurrencySelectionBinding
    private val viewModel: CurrencySelectionViewModel by viewModels()
    private lateinit var currencySelectionAdapter: CurrencySelectionAdapter
    private val currencyList = ArrayList<CurrencyModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCurrencySelectionBinding=DataBindingUtil.setContentView<ActivityCurrencySelectionBinding?>(this, R.layout.activity_currency_selection).apply {
            currencySelectionModel = viewModel
            lifecycleOwner=this@CurrencySelectionActivity
            executePendingBindings()
        }
        initCurrencySelection()
    }

    private fun initCurrencySelection()
    {
        activityCurrencySelectionBinding.RvCurrencySelectionView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                viewModel.getCurrencyList.collectLatest {

                    if (it != null && it.size > 0) {
                        currencyList.addAll(it)
                        currencySelectionAdapter = CurrencySelectionAdapter(this@CurrencySelectionActivity, currencyList)
                        activityCurrencySelectionBinding.RvCurrencySelectionView.adapter = currencySelectionAdapter
                    }

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
        if(searchKey.isNotEmpty())
        {
            val currencySearchList = currencyList.map { it }.filter {
                it.base.lowercase(Locale.ENGLISH).contains(searchKey.lowercase(Locale.getDefault()))
            }

            if(currencySearchList.isNotEmpty())
            {
                currencySelectionAdapter.updateData(currencySearchList)
            }
        }

    }
}