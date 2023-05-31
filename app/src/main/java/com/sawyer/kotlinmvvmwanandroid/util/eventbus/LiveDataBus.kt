package com.sawyer.kotlinmvvmwanandroid.util.eventbus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.reflect.Field
import java.lang.reflect.Method

/**解决LiveData黏性数据的类*/
object LiveDataBus {
    //存放订阅者
    private val mObserverMap: MutableMap<String,BusMutableLiveData<Any>> by lazy { HashMap() }
    /**
     * 暴露一个函数，让外面可以进行注册
     * @key:指定LiveData变量名
     * @type:指定LiveData的泛型的实际数据类型
     * @isSticky:是否接收黏性数据的开关
     * @Synchronized: kotlin的同步锁写法,此处使用防止多线程的并发问题
     */
    @Synchronized
    fun <T> with(key: String,type: Class<T>,isSticky: Boolean = true): BusMutableLiveData<T>{
        if (!mObserverMap.containsKey(key)){
            mObserverMap[key] = BusMutableLiveData(isSticky)
        }
        return mObserverMap[key] as BusMutableLiveData<T>
    }

    class BusMutableLiveData<T> private constructor(): MutableLiveData<T>() {
        //是否接收黏性数据
        var isSticky = true
        constructor(isSticky: Boolean): this(){
            this.isSticky = isSticky
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            if (!isSticky) hook(observer = observer)
        }

        /**
         * 使用反射和Hook技术，替换源码的逻辑来达到去除黏性数据的目的
         * LiveData.java
         * private void considerNotify(ObserverWrapper observer) {
         *     if (observer.mLastVersion >= mVersion) {
         *         return;  //Hook点，让两者相等，即可去除掉黏性数据的接收
         *     }
         *     observer.mLastVersion = mVersion;
         *     observer.mObserver.onChanged((T) mData);
         * }
         */
        private fun hook(observer: Observer<in T>) {
            //todo 1.拿到 ObserverWrapper#mLastVersion
            val liveDataClass: Class<LiveData<*>> = LiveData::class.java
            //获取LiveData类中mObservers对象
            val mObserversField : Field = liveDataClass.getDeclaredField("mObservers")
            mObserversField.isAccessible = true //设置其即使为私有属性也可以访问
            //获取mObservers这个成员变量的对象,即SafeIterableMap
            val mObserversObj: Any = mObserversField.get(this)
            //获取map对象的class对象
            val mObserversClass: Class<*> = mObserversObj.javaClass
            //获取mObservers对象的get方法
            val get: Method = mObserversClass.getDeclaredMethod("get",Any::class.java)
            //执行get方法
            val invokeEntry: Any? = get.invoke(mObserversObj, observer)
            //通过key取出Entry中的value
            var observerWrapper: Any? = null
            if (invokeEntry != null && invokeEntry is Map.Entry<*,*>){
                observerWrapper = invokeEntry.value
            }
            if (observerWrapper == null){
                throw NullPointerException("observerWrapper is null")
            }
            //获取ObserverWrapper类对象
            val superClass: Class<*> = observerWrapper.javaClass.superclass
            val mLastVersion = superClass.getDeclaredField("mLastVersion")
            mLastVersion.isAccessible = true

            //todo 2.拿到 LiveData#mVersion
            val mVersion: Field = liveDataClass.getDeclaredField("mVersion")
            mVersion.isAccessible = true

            //todo 3.对齐版本号，即mLastVersion = mVersion
            val mVersionValue: Any? = mVersion.get(this)
            mLastVersion.set(observerWrapper,mVersionValue)
        }
    }
}