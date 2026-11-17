package com.lino.campusgps.util

import java.util.Locale

fun formatearDistancia(metros: Float): String =
    if (metros < 1000) "${metros.toInt()} m"
    else String.format(Locale.US, "%.1f km", metros / 1000)