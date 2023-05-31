package com.sawyer.kotlinmvvmwanandroid.model.bean

import androidx.annotation.Keep

/**
 *  作用：指示被描述的对象，在编译的时候不要被混淆。
    使用场景：当一个方法、属性、对象等，需要被反射使用时，比如通过反射来实现工厂。
            这时候可以使用该Annotation来标记，这样在编译的时候，就不会被混淆（如果开了混淆的话）
 */

/**
    序列化：将对象写入到IO流
    反序列化：从IO流中恢复对象
    意义：序列化机制允许将实现序列化的Java对象转换位字节序列，这些字节序列可以保存在磁盘上，或通过网络传输，以达到以后恢复成原来的对象
 */
@Keep
data class UserInfo(
    val admin: Boolean,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String,
    val coinCount:Int,
    val collectIds: MutableList<Long>,
    val chapterTops: List<Any>
)