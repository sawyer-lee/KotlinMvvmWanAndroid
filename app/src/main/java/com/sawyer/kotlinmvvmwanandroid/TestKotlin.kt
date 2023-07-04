package com.sawyer.kotlinmvvmwanandroid


import kotlin.math.sqrt

fun main(){
//    val sparseArray = SparseArray<ByteArray>(16)
//    val hashMap = HashMap<Int,ByteArray>(16)

    /*val start = System.currentTimeMillis()
    runBlocking {
        coroutineScope {
            repeat(10000){
                sparseArray[it] = ByteArray(3)
//                hashMap[it] = ByteArray(3)
            }
        }
    }
    val end = System.currentTimeMillis()
    println("total time = ${end - start}ms")*/


    /*val process: Process = Runtime.getRuntime().exec("java -version")
    val bufferedReader = BufferedReader(InputStreamReader(process.errorStream))
    val readLine: String = bufferedReader.readLine()
    println(readLine)
    process.destroy()*/

//    println("density: ${calculate()}")
}

//用于计算dpi
fun calculate() : Double{
    val a = 3216  //高度px
    val b = 1440  //宽度px
    val inch = 6.7  //对角线长度
    return sqrt((a*a + b*b).toDouble())/inch

}