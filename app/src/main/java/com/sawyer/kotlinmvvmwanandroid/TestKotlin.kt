package com.sawyer.kotlinmvvmwanandroid

import kotlin.math.sqrt

fun main(){
    println("density: ${calculate()}")
}

fun calculate() : Double{
    val a = 3216
    val b = 1440
    val inch = 6.7
    return sqrt((a*a + b*b).toDouble())/inch

}