package com.example.myweather.presentation.util

import timber.log.Timber

class CustomTimberTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return "${element.className}:${element.lineNumber}#${element.methodName}"
    }
}