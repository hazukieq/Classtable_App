package com.hazukie.scheduleviews.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hazukie.scheduleviews.R;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void startAct2Act(Context context, Class<?> clz){
        Intent intent=new Intent();
        intent.setClass(context,clz);
        startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.bottom_in,R.anim.noamin);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.noamin,R.anim.bottom_out);
    }
}
