package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.classtool.base.BasicActivity;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
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
        initViews();
    }
    private void initViews(){
        topBar=(QMUITopBarLayout) findViewById(R.id.about_topbar);
        topBar.setTitle(getString(R.string.about_title));
        version=(TextView) findViewById(R.id.version);
        version.setText(getResources().getText(R.string.about_app_name));

        copyright=(TextView) findViewById(R.id.copyright);
        copyright.setText(getResources().getString(R.string.about_copyright));
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
    }

}