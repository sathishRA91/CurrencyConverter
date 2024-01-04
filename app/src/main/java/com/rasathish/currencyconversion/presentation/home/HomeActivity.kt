package com.rasathish.currencyconversion.presentation.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.rasathish.currencyconversion.R
import com.rasathish.currencyconversion.constant.ResultResource
import com.rasathish.currencyconversion.databinding.ActivityHomeBinding
import com.rasathish.currencyconversion.domain.model.CurrencyModel
import com.rasathish.currencyconversion.presentation.currencyselection.CurrencySelectionActivity
import com.rasathish.currencyconversion.presentation.home.adapter.CurrencyViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * Created by sathish on 03,January,2024
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {


    private lateinit var activityHomeBinding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val currencyList = ArrayList<CurrencyModel>()
    private lateinit var currencyViewAdapter: CurrencyViewAdapter
    private var decimalFormat: NumberFormat = DecimalFormat("#0.00")
    private var currencyCode = "KWD"
    private var currencyAmount = "0.0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding =
            DataBindingUtil.setContentView<ActivityHomeBinding?>(this, R.layout.activity_home)
                .apply {
                    this.homeViewModel = viewModel
                    lifecycleOwner = this@HomeActivity
                    this.executePendingBindings()
                }
        init()
    }


    private fun init() {

        activityHomeBinding.RvCurrencyView.layoutManager = GridLayoutManager(this, 3)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                viewModel.getResultResponse.collect { result ->
                    when (result) {
                        is ResultResource.Loading -> {
                            activityHomeBinding.PbLoading.isVisible = true
                        }

                        is ResultResource.ErrorMessage -> {
                            activityHomeBinding.PbLoading.isVisible = false
                        }

                        is ResultResource.Success -> {
                            activityHomeBinding.PbLoading.isVisible = false
                            if (result.data != null && result.data.rates.size > 0) {
                                currencyList.addAll(result.data.rates)
                                currencyViewAdapter =
                                    CurrencyViewAdapter(this@HomeActivity, result.data.rates)
                                activityHomeBinding.RvCurrencyView.adapter = currencyViewAdapter

                            }
                        }

                        else -> {}
                    }
                }
            }
        }


        activityHomeBinding.EtCurrencyAmount.addTextChangedListener {

            currencyConvert(it.toString())
        }

        activityHomeBinding.TvCurrencyCode.setOnClickListener {
            val goCurrencySelection = Intent(this, CurrencySelectionActivity::class.java)
            currencySelectionLauncher.launch(goCurrencySelection)
        }
    }


    private fun currencyConvert(amount: String) {
        val currencyShowList = amount.let { value ->
            if (value.isNotEmpty()) {
                currencyList.map {
                    it.also { value ->
                        value.base = it.base
                        value.rate =
                            decimalFormat.format(it.rate.toDouble() * amount.toDouble()).toString()
                    }
                }
            } else {
                currencyList.map {
                    it.also { value ->
                        value.base = it.base
                        value.rate = "0.0"
                    }
                }
            }
        }

        currencyViewAdapter.updateData(currencyShowList)

    }

    private val currencySelectionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {

                    currencyCode = it.data!!.getStringExtra("currencyCode").toString()
                    currencyAmount = it.data!!.getStringExtra("currencyAmount").toString()

                    viewModel.currencyCode.value = currencyCode

                    if (currencyCode != "KWD") {
                        updateCurrencyAmount(currencyAmount)
                    } else {
                        currencyViewAdapter.updateData(currencyList)
                    }


                }
            }
        }

    private fun updateCurrencyAmount(amount: String) {

        val currencyShowList = currencyList.map {
            it.apply {
                this.base = it.base
                this.rate = decimalFormat.format(it.rate.toDouble() / amount.toDouble()).toString()
            }
        }
        currencyViewAdapter.updateData(currencyShowList)
    }
}