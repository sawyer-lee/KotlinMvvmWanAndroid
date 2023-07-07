package com.sawyer.reflect.proxy;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtil {

    public static void injectEvent(Activity activity){
        //1.使用自定义注解的Activity
        Class<? extends Activity> activityClass = activity.getClass();
        //2.获取该Activity的所有方法
        Method[] declaredMethods = activityClass.getDeclaredMethods();

        for (Method method : declaredMethods) {
            //3.获取单个方法上的所有注解
            Annotation[] annotations = method.getAnnotations();
            //4.遍历注解数组
            for (Annotation annotation : annotations) {
                //5.获得该注解的类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                //6.处理带有EventType类型的注解
                if (annotationType.isAnnotationPresent(EventType.class)){
                    //7.获取注解上的EventType注解
                    EventType eventType = annotationType.getAnnotation(EventType.class);
                    //8.获取该EventType注解的值
                    Class listenerType = eventType.listenerType();
                    String listenerSetter = eventType.listenerSetter();
                    Log.d("DynamicActivity", "type = "+listenerType+",name = "+listenerSetter);

                    try {
                        //9.通过注解里面定义的方法来获得传入的值
                        Method valueMethod = annotationType.getDeclaredMethod("value");
                        int[] viewIds = (int[]) valueMethod.invoke(annotation);
                        method.setAccessible(true);

                        //12.所有的方法都回调给动态代码类的InvocationHandler
                        Object listenerProxy = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class[]{listenerType}, new InvocationHandler() {
                                    @Override
                                    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
                                        return method.invoke(activity, args);
                                    }
                                });

                        for (int viewId: viewIds) {
                            //10.获取到当前Button
                            View view = activity.findViewById(viewId);
                            //11.反射执行setOnClickListener()/setOnLongClickListener()
                            Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                            Log.d("DynamicActivity", "setter = "+ setter.getName());
                            setter.invoke(view, listenerProxy);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
