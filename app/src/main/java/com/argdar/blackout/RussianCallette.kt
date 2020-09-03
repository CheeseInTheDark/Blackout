package com.argdar.blackout

class RussianCallette(
    private val phone: Phone,
    private val numberPool: PhoneNumberPool) {

    fun callSomeone() {
        numberPool.getNumber()?.let { phone.call(it) }
    }
}