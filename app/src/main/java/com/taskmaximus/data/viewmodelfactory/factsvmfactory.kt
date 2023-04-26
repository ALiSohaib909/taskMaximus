package com.taskmaximus.data.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.taskmaximus.data.repo.factsRepo
import com.taskmaximus.data.viewModel.factsViewmodel

class factsvmfactory (val application: Application, val itemsrepository: factsRepo): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when(modelClass){
        factsViewmodel::class.java -> factsViewmodel(application,itemsrepository)
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    } as T



}