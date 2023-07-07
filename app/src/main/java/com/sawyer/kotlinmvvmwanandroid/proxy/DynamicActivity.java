package com.sawyer.kotlinmvvmwanandroid.proxy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sawyer.kotlinmvvmwanandroid.R;
import com.sawyer.reflect.proxy.InjectUtil;
import com.sawyer.reflect.proxy.OnClick;
import com.sawyer.reflect.proxy.OnLongClick;

/**
 * 使用注解、反射和动态代理，实现onClick、onLongClick的功能
 */
public class DynamicActivity extends AppCompatActivity {

    private static final String TAG = "DynamicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        InjectUtil.injectEvent(this);
    }

    @OnClick({R.id.btnTest1, R.id.btnTest2})
    public void onButtonClick(View view){
        if (view.getId() == R.id.btnTest1){
            Log.d(TAG, "onClick: btn1");
        }else {
            Log.d(TAG, "onClick: btn2");
        }
    }

    @OnLongClick({R.id.btnTest1, R.id.btnTest2})
    public boolean onButtonLongClick(View view){
        if (view.getId() == R.id.btnTest1){
            Log.d(TAG, "onLongClick: btn1");
        }else {
            Log.d(TAG, "onLongClick: btn2");
        }
        return false;
    }

}