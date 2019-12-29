package com.bourdi_bay.wordoftheday

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class DailyWord(
    val word: String,
    val date: String,
    var definition: String,
    var examples: List<String> = listOf(),
    var oftenUsed: List<String> = listOf()
) : Parcelable {
    @IgnoredOnParcel
    val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
    @IgnoredOnParcel
    val encodedDate: Date = dateFormat.parse(date)

    // Used in Adapter class, to avoid creating a custom filter class
    override fun toString(): String {
        return word
    }
}
