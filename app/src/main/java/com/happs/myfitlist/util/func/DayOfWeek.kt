package com.happs.myfitlist.util.func

import java.util.Calendar

fun getCurrentDayOfWeekIndex(): Int {
    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    // Adjust index to match your DiasList (assuming Monday is index 0)
    return (dayOfWeek - 2 + 7) % 7
}