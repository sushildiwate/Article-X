package com.sushil.articlex.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.sushil.articlex.R
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import java.text.DecimalFormat
import kotlin.math.abs

//Function to check network connection
fun isInternetAvailable(activity: Activity): Boolean {
    var result = false
    val connectivityManager =
        activity.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    connectivityManager?.let {
        it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
    }
    return result
}

//function to calculate the difference between date and time
fun articleDateTime(context: Context,createdDateTime:String) :String{
    val currentTime = DateTime.now()
    val parsedCreatedDateTime = DateTime.parse(createdDateTime)

    val day = Days.daysBetween(parsedCreatedDateTime,currentTime).days
    val hour = Hours.hoursBetween(parsedCreatedDateTime,currentTime).hours
    val minute = Minutes.minutesBetween( parsedCreatedDateTime,currentTime).minutes

    return when {
        day > 0 -> "$day ${context.getString(R.string.day)}"
        hour > 0 ->"$hour ${context.getString(R.string.hour)}"
        else -> "$minute ${context.getString(R.string.minute)}"
    }
}
//function to format the numbers in expected format
fun Int.getFormatedValue(context: Context, suffix: String): String {
    return if (abs(this / 1000) > 1)
        "${DecimalFormat("##.#").format((toDouble() / 1000))}${context.getString(R.string.k)} $suffix"
    else
        "$this $suffix"
}