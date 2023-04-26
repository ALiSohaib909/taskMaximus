package com.taskmaximus.data.repo

import com.taskmaximus.data.Model.facts
import com.taskmaximus.data.dao.factDao
import retrofit2.Response

class factsRepo(val facts: factDao) {

    suspend fun getFacts(): Response<facts> {
        return facts.getDao()
    }
}