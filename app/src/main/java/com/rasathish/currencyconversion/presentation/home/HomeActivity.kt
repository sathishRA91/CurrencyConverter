package com.rasathish.currencyconversion.presentation.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.rasathish.currencyconversion.R
import com.rasathish.currencyconversion.constant.ResultResource
import com.rasathish.currencyconversion.databinding.ActivityHomeBinding
import com.rasathish.currencyconversion.presentation.currencyselection.CurrencySelectionActivity
import com.rasathish.currencyconversion.presentation.home.adapter.CurrencyViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * Created by sathish on 03,January,2024
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {


    private lateinit var activityHomeBinding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private val currencyList = ArrayList<CurrencyItemModel>()
    private lateinit var currencyViewAdapter: CurrencyViewAdapter
    private var decimalFormat: NumberFormat = DecimalFormat("#0.000")
    private var currencyCode = "USD"
    private var currencyAmount = "0.0"

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        activityHomeBinding.homeViewModel = homeViewModel
        activityHomeBinding.lifecycleOwner = this
        activityHomeBinding.executePendingBindings()

        init()
    }


    private fun init() {

        activityHomeBinding.RvCurrencyView.layoutManager = GridLayoutManager(this, 3)

        lifecycleScope.launchWhenStarted {

            homeViewModel.getResultResponse.collect { result ->

                when (result) {
                    is ResultResource.Loading -> {
                        activityHomeBinding.PbLoading.isVisible = true
                    }

                    is ResultResource.ErrorMessage -> {
                        activityHomeBinding.PbLoading.isVisible = false
                        Toast.makeText(this@HomeActivity, result.message, Toast.LENGTH_LONG).show()
                    }

                    is ResultResource.Success -> {
                        activityHomeBinding.PbLoading.isVisible = false
                        homeViewModel.getCurrencyList.collectLatest {

                            if (it != null && it.size > 0) {
                                currencyList.addAll(it)
                                currencyViewAdapter = CurrencyViewAdapter(this@HomeActivity, it)
                                activityHomeBinding.RvCurrencyView.adapter = currencyViewAdapter

                            }

                        }
                    }
                    else -> {}
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


    private fun currencyConvert(amount: String?) {

        val currencyShowList:List<CurrencyItemModel> = amount?.let{
            currencyList.map {
               val currencyConvertAmount = if (currencyCode == "USD") {
                    it.currencyAmount.toDouble() * amount.toDouble()
                } else {
                    (it.currencyAmount.toDouble() / currencyAmount.toDouble()) * amount.toDouble()
                }
                it.apply {
                   currencyAmount= decimalFormat.format(currencyConvertAmount).toString()
                }
            }
        }?: currencyList.map {
             it.apply {
                 currencyCode=it.currencyCode
                 currencyAmount= "0.0"
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

                    homeViewModel.currencyCode.value = currencyCode

                    if (currencyCode != "USD") {
                        updateCurrencyAmount(currencyAmount)
                    } else {
                        currencyViewAdapter.updateData(currencyList)
                    }


                }
            }
        }

    private fun updateCurrencyAmount(amount: String) {

        val currencyShowList =  currencyList.map {
            it.apply {
                this.currencyCode=it.currencyCode
                this.currencyAmount= decimalFormat.format(it.currencyAmount.toDouble() / amount.toDouble()).toString()
            }
        }
        currencyViewAdapter.updateData(currencyShowList)
    }
}