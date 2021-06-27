package com.example.buildup.helpers

import com.example.api.BuildUpClient.retrofit
import com.example.api.models.entities.APIErrorNew
import retrofit2.Response
import java.io.IOException

object ErrorUtilsNew {
    fun parseError(response: Response<*>): APIErrorNew {
        val converter = retrofit
                .responseBodyConverter<APIErrorNew>(APIErrorNew::class.java, arrayOfNulls(0))
        val errorBody: APIErrorNew
        errorBody = converter.convert(response.errorBody())!!
        return errorBody
    }
}