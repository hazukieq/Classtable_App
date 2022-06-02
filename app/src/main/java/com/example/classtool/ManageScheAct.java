package com.example.classtool;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.BasicActivity;
import com.example.classtool.binders.ManageScheBinder;
import com.example.classtool.models.SchedulModel;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;

public class ManageScheAct extends BasicActivity {
    private RecyclerView recy;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<Object> alls=new ArrayList<>();
    private QMUIEmptyView emptyView;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Handler handler;
    private  List<String> schetimtags=new ArrayList<>();
    private List<String> deleteSchtags=new ArrayList<>();
    private List<String> backups=new ArrayList<>();
    private List<String> ande=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sche);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        recy=(RecyclerView) findViewById(R.id.times_add_recy);
        emptyView=(QMUIEmptyView)findViewById(R.id.empty);
        sp= PreferenceManager.getDefaultSharedPreferences(ManageScheAct.this);
        editor=sp.edit();
        List<String> la=FilesUtil.readSchedulAndTimeTag(ManageScheAct.this);
        schetimtags.addAll(la);
        ande.addAll(la);

       initRecy();
       checkVisible();
    }


    private void initRecy(){
        multiTypeAdapter=new MultiTypeAdapter();
        ManageScheBinder manageScheBinder=new ManageScheBinder();
        manageScheBinder.setOnPItemLClick(new ManageScheBinder.OnPItemLClick() {
            @Override
            public void onDelete(View v,String name, int position) {
                new QMUIDialog.MessageDialogBuilder(ManageScheAct.this)
                        .setTitle("课表卡片")
                        .setSkinManager(QMUISkinManager.defaultInstance(ManageScheAct.this))
                        .setMessage("请问您需要删除名为「"+name+"」的课表文件吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确认", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                if (FilesUtil.deleteScheFile(getApplicationContext(), name)) {
                                    try {
                                       deleteSchtags.add(name);
                                        alls.remove(position);
                                        multiTypeAdapter.notifyItemRemoved(position);
                                       // Toast.makeText(ManageScheAct.this, "删除文件成功！", Toast.LENGTH_SHORT).show();
                                        checkVisible();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                Thread.sleep(400);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            editor.putInt("ScheSelected", 0);
                                            editor.commit();
                                            dialog.dismiss();
                                        }
                                    }).start();


                                }else{
                                   /* new QMUITipDialog.Builder(ManageScheAct.this)
                                            .setTipWord("删除文件失败！")
                                            .create().show();*/
                                    Toast.makeText(ManageScheAct.this, "删除文件失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .create( R.style.DialogTheme2).show();

            }
        });

        List<String> df= FilesUtil.readSchedulAndTimeTag(getApplicationContext());
        if(df.size()>0){
            for(int y=1;y<df.size();y++){
                alls.add(new SchedulModel(y,df.get(y).split(",")[0],"dl"));
            }
        }
        multiTypeAdapter.register(SchedulModel.class,manageScheBinder);
        multiTypeAdapter.setItems(alls);
        recy.setAdapter(multiTypeAdapter);
    }



    private void checkVisible(){
        if(alls.size()==0){
            recy.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setDetailColor(getColor(R.color.class_pink));
            emptyView.setBackgroundColor(getColor(R.color.light_white));
            emptyView.setTitleText("这里好像什么都没有哟~");
            emptyView.setTitleColor(getColor(R.color.text_red));

        }else if(alls.size()>0){
            recy.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(String str:schetimtags){
            for(String tr:deleteSchtags){
                if(str.split(",")[0].equals(tr)){
                    ande.remove(str);
                }
            }
        }
        backups.addAll(ande);
        FilesUtil.RemoveScheDulAndTimeTag(ManageScheAct.this,backups);
    }
}