package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Transition;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.classtool.base.BasicActivity;
import com.example.classtool.utils.FragcomWebView;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.util.QMUIToastHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

public class AboutActivity extends BasicActivity {
    private QMUITopBarLayout topBar;
    private TextView version,copyright;
    private QMUIGroupListView about_list;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        try {
            initViews();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void initViews() throws PackageManager.NameNotFoundException {
        topBar=(QMUITopBarLayout) findViewById(R.id.about_topbar);
        topBar.setTitle(getString(R.string.about_title));
        version=(TextView) findViewById(R.id.version);
        PackageManager pm=getPackageManager();
        PackageInfo packageInfo=pm.getPackageInfo(getPackageName(),0);
        version.setText(getResources().getText(R.string.about_app_name)+"(版本号："+packageInfo.versionName+")");


        about_list=(QMUIGroupListView) findViewById(R.id.about_list);
        QMUICommonListItemView itemView1=about_list.createItemView(getResources().getString(R.string.about_contact));

        QMUIGroupListView.newSection(AboutActivity.this)
                .addItemView(about_list.createItemView(getResources().getString(R.string.about_name)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addItemView(about_list.createItemView(getResources().getString(R.string.about_author)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addItemView(about_list.createItemView(getResources().getString(R.string.open_address)), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent inq=new Intent();
                        inq.setClass(AboutActivity.this,WebloadAct.class);
                        startActivity(inq);
                    }
                })
                .addItemView(about_list.createItemView("使用说明"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent();
                        in.setClass(AboutActivity.this,HomeAct.class);
                        startActivity(in);
                    }
                })
                .addItemView(itemView1, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new QMUIDialog.MessageDialogBuilder(AboutActivity.this)
                                .setTitle(getString(R.string.about_contact))
                                .setSkinManager(QMUISkinManager.defaultInstance(AboutActivity.this))
                                .setMessage(getText(R.string.about_contact_details))
                                .addAction("知道了", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .create( R.style.DialogTheme2).show();
                    }
                })
                .addItemView(about_list.createItemView(getResources().getString(R.string.about_allcited)), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new QMUIDialog.MessageDialogBuilder(AboutActivity.this)
                                .setTitle(getString(R.string.about_allcited))
                                .setSkinManager(QMUISkinManager.defaultInstance(AboutActivity.this))
                                .setMessage(getText(R.string.about_cite_list))
                                .addAction(getString(R.string.dialog_know), new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .create( R.style.DialogTheme2).show();
                    }
                })
                .addTo(about_list);

        //iniFrags();
    }

   /* private void iniFrags(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transition=fm.beginTransaction();
        transition.replace(R.id.weblos,new Webf("https://www.hazukieq.top/html/about.html"));
        transition.commit();
    }

    public static  class  Webf extends FragcomWebView{
        public  Webf(String url){
            open_url=url;
        }
        }*/
    }

