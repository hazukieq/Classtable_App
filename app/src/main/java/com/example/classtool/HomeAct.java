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

public class HomeAct extends ActcomWeb {
    public HomeAct(){
        open_url="https://www.hazukieq.top/html/downloan_page.html";
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
                topBarLayout.setTitle("使用说明");
            }
        };
    }
}