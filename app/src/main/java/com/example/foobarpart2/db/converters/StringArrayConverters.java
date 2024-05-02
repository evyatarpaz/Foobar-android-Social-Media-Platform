package com.example.foobarpart2.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class StringArrayConverters {
    @TypeConverter
    public static String fromArrayToString(String[] array) {
        if (array == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<String[]>() {}.getType();
        return gson.toJson(array, type);
    }

    @TypeConverter
    public static String[] fromStringToArray(String string) {
        if (string == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<String[]>() {}.getType();
        return gson.fromJson(string, type);
    }
}
