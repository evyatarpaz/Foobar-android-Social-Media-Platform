package com.example.foobarpart2.db.converters;

import android.net.Uri;

import androidx.room.TypeConverter;

public class UriConverters {
    @TypeConverter
    public static String fromUri(Uri uri) {
        return uri == null ? null : uri.toString();
    }

    @TypeConverter
    public static Uri toUri(String uriString) {
        return uriString == null ? null : Uri.parse(uriString);
    }
}
