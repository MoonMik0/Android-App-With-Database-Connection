package com.ozlemelmali.kotlintriesv7

import com.ozlemelmali.kotlintriesv7.Data.ColumnResponse
import com.ozlemelmali.kotlintriesv7.Data.LoginResponse
import com.ozlemelmali.kotlintriesv7.Data.TableDataResponse
import com.ozlemelmali.kotlintriesv7.Data.UserLogin
import retrofit2.Call
import retrofit2.http.*

data class Item(val id: Int, val name: String, val description: String)

interface ApiService {
    @GET("items/") //get all column from items table
    fun getItems(): Call<List<Item>>

    @GET("items/{id}") // get all column from items by id
    fun getItem(@Path("id") id: Int): Call<Item>

    @POST("items/") //create new row in items
    @FormUrlEncoded
    fun createItem(@Field("name") name: String, @Field("description") description: String): Call<Item>

    @PUT("items/{id}") // update row in items by id
    @FormUrlEncoded
    fun updateItem(@Path("id") id: Int, @Field("name") name: String, @Field("description") description: String): Call<Item>

    @DELETE("items/{id}") // delete row in items by id
    fun deleteItem(@Path("id") id: Int): Call<Void>

    @GET("/getTableNames") // get names of the tales in database
    fun getTableNames(): Call<List<String>>

    @GET("/getTableData/{table_name}") // get rows from specified table
    fun getTableData(@Path("table_name") tableName: String): Call<TableDataResponse>

    @POST("login") // login authentication
    @Headers("Content-Type: application/json")
    fun login(@Body request: UserLogin): Call<LoginResponse>

    @GET("/get_columns/{table_name}")
    fun getTableColumns(@Path("table_name") tableName: String): Call<ColumnResponse>
}

