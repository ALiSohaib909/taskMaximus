package com.taskmaximus.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.taskmaximus.data.Baseresponse.Baseresponse
import com.taskmaximus.data.Model.facts
import com.taskmaximus.data.repo.factsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class factsViewmodel(context: Application, val repo: factsRepo) : AndroidViewModel(context) {
    val itemsresult = MutableLiveData<Baseresponse<facts>>()

    var liveData = itemsresult

    fun getData() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = repo.getFacts()
            try {
                if (result.code() == 200) {
                    liveData.value = Baseresponse.Success(result.body())
                }
            } catch (e: Exception) {
                liveData.value = Baseresponse.Error(e.message.toString())
            }
        }
    }
}