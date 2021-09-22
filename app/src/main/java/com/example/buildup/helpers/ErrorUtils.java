package com.example.buildup.helpers;

import com.example.api.BuildUpClient;
import com.example.api.models.entities.APIError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {
    public static APIError parseError(Response<?> response){
        Converter<ResponseBody, APIError> converter= BuildUpClient.INSTANCE
                .getRetrofit()
                .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }
        return error;
    }
}
