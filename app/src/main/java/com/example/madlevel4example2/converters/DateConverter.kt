package com.example.madlevel4example2.converters

import androidx.room.TypeConverter
import java.util.*

/**
 * This class will convert the date object to a long value and vice versa
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}