package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.classtool.utils.ActcomWeb;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

public class WebloadAct extends ActcomWeb {
    public WebloadAct()
    {
        open_url="https://github.com/hazukieq/Classtable_App";
        }

    @Override
    public void customStatus() {
        super.customStatus();
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    public configTopbars getConfigTopbars(QMUITopBarLayout topBarLayout) {
        return new configTopbars(topBarLayout){
            @Override
            public void setTopbarTitle() {
                super.setTopbarTitle();
                topBarLayout.setTitle("项目地址");
            }


        };
    }


}