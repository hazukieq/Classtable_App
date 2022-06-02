package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;

import com.example.classtool.base.BasicActivity;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

public class SettingActivity extends BasicActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private QMUIGroupListView groupListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sp.edit();
        QMUITopBarLayout top=(QMUITopBarLayout)findViewById(R.id.setting_main_topbar);
        top.setTitle("设置");
        top.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        groupListView=(QMUIGroupListView)findViewById(R.id.groupListView);

        QMUICommonListItemView start_sche =groupListView.createItemView("制作云课表");
        start_sche.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);

        QMUICommonListItemView st_sche =groupListView.createItemView("下载云课表");
        st_sche.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);

        QMUICommonListItemView  manage_sche=groupListView.createItemView("课表文件管理");
        manage_sche.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        int size = QMUIDisplayHelper.dp2px(SettingActivity.this, 20);
        QMUIGroupListView.newSection(SettingActivity.this)
                .setTitle("功能版块")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(start_sche, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setClass(SettingActivity.this,SharedManageActivity.class);
                        startActivity(intent);
                        /*new QMUIDialog.MessageDialogBuilder(SettingActivity.this)
                                .setTitle("课表云同步")
                                .setSkinManager(QMUISkinManager.defaultInstance(SettingActivity.this))
                                .setMessage("指的是，一个人上传其制作的课表到服务器，并共享给同班同学的一种同步。\n目前作者还没有开发这一功能...")
                                .addAction("取消", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .addAction("前往", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        Intent intent=new Intent();
                                        intent.setClass(SettingActivity.this,DownloadActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                })
                                .create( R.style.DialogTheme2).show();*/
                    }
                })
                .addItemView(st_sche, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent ifq=new Intent();
                       ifq.setClass(SettingActivity.this,GetscheActivity.class);
                       startActivity(ifq);
                    }
                })
                .addItemView(manage_sche, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent();
                        in.setClass(SettingActivity.this,ManageScheAct.class);
                        startActivity(in);
                    }
                })
                .setOnlyShowStartEndSeparator(true)
                .addTo(groupListView);

    }
}