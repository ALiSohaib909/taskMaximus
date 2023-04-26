package com.taskmaximus.data.dao

import com.taskmaximus.data.Model.facts
import retrofit2.http.GET

interface factDao {

    @GET("/facts")
    suspend fun getDao() : retrofit2.Response<facts>
}