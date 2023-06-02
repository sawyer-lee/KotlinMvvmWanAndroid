package com.sawyer.kotlinmvvmwanandroid.ui.test

import android.os.Bundle
import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.set
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivityTestBinding
import com.sawyer.kotlinmvvmwanandroid.util.Logger

class TestActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestBinding
    val sparseArray = SparseArray<ByteArray>(16)
    val hashMap = HashMap<Int,ByteArray>(16)
    val length = 100000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSparse.setOnClickListener {
                val start = System.currentTimeMillis()
                for (i in 0..length){
                    sparseArray[i] = ByteArray(10)
                }
                val end = System.currentTimeMillis()
                Logger.i("lee","sparse put total time = ${end - start} ms")
            }
            btnHash.setOnClickListener {
                val start = System.currentTimeMillis()
                for (i in 0..length){
                    hashMap[i] = ByteArray(10)
                }
                /*runBlocking {
                    coroutineScope {
                        repeat(1000){
                            hashMap[it] = ByteArray(10)
                        }
                    }
                }*/
                val end = System.currentTimeMillis()
                Logger.i("lee","hash put total time = ${end - start} ms")
            }
            btnGetTime.setOnClickListener {
                val start = System.currentTimeMillis()
                for (i in 0..length){
                    sparseArray[i]
                }
                var time = System.currentTimeMillis()
                Logger.i("lee","sparse get total time = ${time - start} ms")

                time = System.currentTimeMillis()
                for (i in 0..length){
                    hashMap[i]
                }
                Logger.i("lee","hash get total time = ${System.currentTimeMillis() - time} ms")
            }
        }

    }
}