package com.taskmaximus.ui.activities.mainAct

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.taskmaximus.Ads.InterstitialClass
import com.taskmaximus.Ads.NativeAd
import com.taskmaximus.data.Baseresponse.Baseresponse
import com.taskmaximus.data.Model.facts
import com.taskmaximus.data.dao.factDao
import com.taskmaximus.data.db.retrofitInstance
import com.taskmaximus.data.repo.factsRepo
import com.taskmaximus.data.viewModel.factsViewmodel
import com.taskmaximus.databinding.ActivityMainBinding
import com.taskmaximus.utils.ViewDialog
import com.taskmaximus.ui.activities.mainAct.adapter.AdapterFacts

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var factsViewmodel: factsViewmodel
    lateinit var adapter: AdapterFacts

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InterstitialClass.loadInterstitial(this)
        NativeAd.adNativeAd(
            this, true,
            binding.nativeAdContainerAd,
            true
        )
        {
            binding.nativeAdContainerAd.visibility = View.GONE

        }

        adapter = AdapterFacts()
        binding.rvFacts.layoutManager = LinearLayoutManager(this)

        val data = retrofitInstance.getInstance(this).create(factDao::class.java)
        val factsRepo = factsRepo(data)
        factsViewmodel = factsViewmodel(this.application, factsRepo)
        factsViewmodel.getData()
        observeData()

        binding.btnReload.setOnClickListener {
            observeData()
        }
        binding.btnAd.setOnClickListener {
            InterstitialClass.showInterstitial(this)
        }
    }

    private fun observeData() {
        factsViewmodel.itemsresult.observe(this) {
            when (it) {
                is Baseresponse.Error -> {
                    Log.d("Error", it.errormessage.toString())
                    Toast.makeText(this, it.errormessage.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                is Baseresponse.Loading -> {
                }
                is Baseresponse.Success -> {
                    Log.d("response", it.data.toString())
                    get(it.data!!)
                    it.data!!.forEach {
                        //   get(it)
                        Log.d("response2", it.toString())
                    }
                }
            }

        }
    }

    fun get(listofFacts: facts) {
        adapter.getFacts(listofFacts)
        binding.rvFacts.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        Log.d("onR", "dadaasdas")
        InterstitialClass.loadInterstitial(this)
    }

    override fun onBackPressed() {
        val viewDialog= ViewDialog()
        viewDialog.showDialog(this,"Do you Want to Exit App?")
    }
}