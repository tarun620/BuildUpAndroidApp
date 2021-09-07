package com.example.buildup.helpers

import com.example.api.BuildUpClient.retrofit
import com.example.api.models.entities.APIError
import retrofit2.Response

object ErrorUtilsNew {
    fun parseError(response: Response<*>): APIError {
        val converter = retrofit
                .responseBodyConverter<APIError>(APIError::class.java, arrayOfNulls(0))
        val errorBody: APIError
        errorBody = converter.convert(response.errorBody())!!
        return errorBody
    }
}