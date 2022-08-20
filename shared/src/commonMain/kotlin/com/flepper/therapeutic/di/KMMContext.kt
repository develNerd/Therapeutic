package com.flepper.therapeutic.di


expect class Context()

expect class KMMContext(context:Context){
    var application:Context
}