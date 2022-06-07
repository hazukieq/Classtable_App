package com.example.classtool;

import com.example.classtool.utils.ActcomWeb;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

public class WebloadAct extends ActcomWeb {
    public WebloadAct()
    {
        open_url="https://github.com/hazukieq/Classtable_App";
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