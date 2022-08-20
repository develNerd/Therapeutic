package com.flepper.therapeutic

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}