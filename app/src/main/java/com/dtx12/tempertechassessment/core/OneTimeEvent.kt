package com.dtx12.tempertechassessment.core

class OneTimeEvent<T>(private val _value: T) {

    private var _handled: Boolean = false

    val value: T?
        get() {
            if (_handled) {
                return null
            }
            _handled = true
            return _value
        }
}
