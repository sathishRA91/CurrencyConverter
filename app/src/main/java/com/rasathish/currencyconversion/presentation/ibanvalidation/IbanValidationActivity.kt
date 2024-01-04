package com.rasathish.currencyconversion.presentation.ibanvalidation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rasathish.currencyconversion.R
import com.rasathish.currencyconversion.constant.ResultResource
import com.rasathish.currencyconversion.constant.Validation
import com.rasathish.currencyconversion.databinding.ActivityIbanValidationBinding
import com.rasathish.currencyconversion.presentation.home.HomeActivity
import com.rasathish.currencyconversion.presentation.home.adapter.CurrencyViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by sathish on 04,January,2024
 */
@AndroidEntryPoint
class IbanValidationActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var activityIbanValidationBinding: ActivityIbanValidationBinding
    private val viewModel: IBanValidationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        activityIbanValidationBinding =
            DataBindingUtil.setContentView<ActivityIbanValidationBinding?>(
                this,
                R.layout.activity_iban_validation
            )
                .apply {
                    this.ibanViewModel = viewModel
                    lifecycleOwner = this@IbanValidationActivity
                    this.executePendingBindings()
                }
        initValidateIban()
    }

    private fun initValidateIban() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                viewModel.getResultResponse.collect { result ->
                    when (result) {
                        is ResultResource.Loading -> {
                            activityIbanValidationBinding.PbLoading.isVisible = true
                        }

                        is ResultResource.ErrorMessage -> {
                            activityIbanValidationBinding.PbLoading.isVisible = false
                            Toast.makeText(this@IbanValidationActivity,"Please Enter valid IBAN Number",Toast.LENGTH_SHORT).show()
                        }

                        is ResultResource.Success -> {
                            activityIbanValidationBinding.PbLoading.isVisible = false
                            activityIbanValidationBinding.CvIbanDetail.isVisible = true

                            if (result.data != null) {
                                viewModel.ibanStatusMessage.value = result.data.message
                                viewModel.ibanCity.value =  result.data.bankData.city
                                viewModel.ibanCountry.value =  result.data.ibanData.country
                                viewModel.ibanBankName.value = result.data.bankData.name
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        activityIbanValidationBinding.BtValidate.setOnClickListener(this)
        activityIbanValidationBinding.BtProceed.setOnClickListener(this)
        activityIbanValidationBinding.BtSkip.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.Bt_validate -> {

                if (Validation.isBanNumberValid(viewModel.ibanNumber.value)) {
                    viewModel.validateIban()
                }
                else
                {
                    Toast.makeText(this@IbanValidationActivity,"Please Enter valid IBAN Number",Toast.LENGTH_SHORT).show()
                }

            }

            R.id.Bt_proceed -> {

                startActivity(Intent(this@IbanValidationActivity, HomeActivity::class.java))
            }

            R.id.Bt_skip -> {

                startActivity(Intent(this@IbanValidationActivity, HomeActivity::class.java))
            }
        }
    }

}