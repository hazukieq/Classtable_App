package com.example.classtool;
import com.example.classtool.utils.ActcomWeb;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

public class HomeAct extends ActcomWeb {
    public HomeAct(){
        open_url="https://www.hazukieq.top/html/downloan_page.html";
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