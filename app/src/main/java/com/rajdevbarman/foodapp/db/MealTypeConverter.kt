package com.rajdevbarman.foodapp.db

import androidx.room.TypeConverters
import androidx.room.TypeConverter

@TypeConverters
class MealTypeConverter {
    @TypeConverter
    fun fromAnyToString(attribute: Any?): String{
        if (attribute == null)
            return ""
        return attribute as String
    }

    @TypeConverter
    fun fromStringToAny(attribute: String?): Any{
        if (attribute == null)
            return ""
        return attribute
    }
}