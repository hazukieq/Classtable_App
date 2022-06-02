package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.BasicActivity;
import com.example.classtool.base.OnItemClick;
import com.example.classtool.binders.TimelistBinder;
import com.example.classtool.models.QTime;
import com.example.classtool.models.SchedulModel;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.List;

public class TimelistAct extends BasicActivity {
    private QMUITopBarLayout topbar;
    private RecyclerView recy;
    private MultiTypeAdapter multiAd;
    private TextView saving;
    private ArrayList<Object> alls=new ArrayList<>();
    private List<String> schedutimetags=new ArrayList<>();
    private List<String> timetags=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelist);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        List<String> qa=FilesUtil.readSchedulAndTimeTag(TimelistAct.this);
        List<String> wa=FilesUtil.readTimeTag(getApplicationContext());
        schedutimetags.addAll(qa);
        timetags.addAll(wa);
        initViews();
    }

    private void initViews(){
        topbar=(QMUITopBarLayout) findViewById(R.id.timelist_topbar);
        recy=(RecyclerView) findViewById(R.id.timelist_recy);
        saving=(TextView)findViewById(R.id.timelist_saving);

        saving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inq=new Intent();
                inq.setClass(TimelistAct.this,TimeAddingActivity.class);
                startActivity(inq);
            }
        });

        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        topbar.setTitle("作息时间列表");

        multiAd=new MultiTypeAdapter();
        TimelistBinder timelistBinder=new TimelistBinder();
        timelistBinder.setOnItemClick(new OnItemClick() {
            @Override
            public void onClick(View v, int position, int sirtq, String sortq, String str) {
                     Intent iy=new Intent();
                     iy.putExtra("sche_name",sortq);
                     iy.setClass(TimelistAct.this,TimesetAxt.class);
                     startActivity(iy);
            }
        });

        timelistBinder.setOnItemLClick(new TimelistBinder.OnItemLClick() {
            @Override
            public void onDelete(View v, int position) {
                SchedulModel schedulModel = (SchedulModel) alls.get(position);
                boolean is = FilesUtil.deleteSingleFile(getApplicationContext(),schedulModel.getSche());
                new QMUIDialog.MessageDialogBuilder(TimelistAct.this)
                        .setTitle("作息时间卡片")
                        .setSkinManager(QMUISkinManager.defaultInstance(TimelistAct.this))
                        .setMessage("请问您需要删除名为「"+schedulModel.getSche()+"」的作息时间文件吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确认", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                if (is == false) {
                                    Toast.makeText(TimelistAct.this, "删除失败！", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if(is==true){



                                    //写入已经删除时间索引记录后的时间总索引数据
                                    try{
                                        //移除时间总索引中的记录
                                        List<String> halls=new ArrayList<>();
                                        halls.addAll(timetags);
                                        String qtag=((SchedulModel) alls.get(position)).getSche();
                                        for(String sd:timetags){
                                            if(sd.split(",")[0].equals(qtag)){
                                                halls.remove(sd);
                                            }
                                        }

                                        //将课表索引中已经删除的时间表索引进行替换，预防相关BUG发生
                                        List<String> sss=new ArrayList<>();
                                        for(String sch:schedutimetags){
                                            int sc=0;
                                            if(sch.split(",")[1].equals(qtag)){
                                               sc=1;
                                            }

                                            if(sc==1){
                                                sss.add(sch.replace(qtag,"武鸣校区作息时间"));
                                            }else{
                                                sss.add(sch);
                                            }
                                        }
                                        //刷新RECYCLERVIEW视图
                                        alls.remove(position);
                                        multiAd.notifyItemRemoved(position);
                                        multiAd.notifyItemRangeChanged(0,alls.size());
                                        FilesUtil.AppendTimeTags(getApplicationContext(),halls);
                                        FilesUtil.RemoveScheDulAndTimeTag(TimelistAct.this,sss);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                }
                            }
                        })
                        .create( R.style.DialogTheme2).show();

                        }

        });

        multiAd.register(SchedulModel.class,timelistBinder);
        List<String> timecards=new ArrayList<>();
        timecards=FilesUtil.readTimeTag(getApplicationContext());
        //
        if(timecards.size()>0){
            String[] tags=new String[timecards.size()];
         for(int y=0;y<timecards.size();y++){
             String[] ios=timecards.get(y).split(",");
             tags[y]=ios[0];
         }

         for(int f=0;f< tags.length;f++){
             alls.add(new SchedulModel(f,tags[f],""));
         }

        }


        multiAd.setItems(alls);
        recy.setAdapter(multiAd);
    }

    @Override
    protected void onResume() {
        super.onResume();
        alls.clear();
        List<String> timecards = new ArrayList<>();
        timecards = FilesUtil.readTimeTag(getApplicationContext());
        //
        if (timecards.size() > 0) {
            String[] tags = new String[timecards.size()];
            for (int y = 0; y < timecards.size(); y++) {
                String[] ios = timecards.get(y).split(",");
                tags[y] = ios[0];
            }
            for (int f = 0; f < tags.length; f++) {
                alls.add(new SchedulModel(f, tags[f], ""));
            }
            multiAd.notifyDataSetChanged();
        }
    }
}