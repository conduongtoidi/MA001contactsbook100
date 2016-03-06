package com.whatsoft.contactbook.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Created by mb
 */
public class ParserUtil {

    public static <T> T fromJson(String json, Class<T> classOfT) throws Exception {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();
        return gson.fromJson(json, classOfT);
    }


    public static <T> T fromJson(Reader json, Class<T> classOfT) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object src, Type typeOfSrc) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(src, typeOfSrc);
    }

    public static String toJson(Object src) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(src);
    }

    public static <T> T fromJson(String json, Type typeOfT) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(json, typeOfT);
    }
}
