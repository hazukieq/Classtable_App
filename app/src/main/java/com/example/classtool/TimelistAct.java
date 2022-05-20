package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.OnItemClick;
import com.example.classtool.binders.TimelistBinder;
import com.example.classtool.models.QTime;
import com.example.classtool.models.SchedulModel;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;
import java.util.List;

public class TimelistAct extends AppCompatActivity {
    private QMUITopBarLayout topbar;
    private RecyclerView recy;
    private MultiTypeAdapter multiAd;
    private TextView saving;
    private ArrayList<Object> alls=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelist);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
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
                boolean is = FilesUtil.deleteSingleFile(schedulModel.getSche());
                if (is == false) {
                    Toast.makeText(TimelistAct.this, "删除失败！", Toast.LENGTH_SHORT).show();
                } else if(is==true){
                    List<String> timetags=FilesUtil.readTimeTag();
                    String qtag=((SchedulModel) alls.get(position)).getSche();
                    for(String sd:timetags){
                        if(sd.split(",")[0].equals(qtag)){
                            timetags.remove(sd);
                        }
                    }

                    FilesUtil.AppendTimeTags(timetags);
                    alls.remove(position);
                    multiAd.notifyItemRemoved(position);
                    Toast.makeText(TimelistAct.this, "删除成功！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        multiAd.register(SchedulModel.class,timelistBinder);

        List<String> timecards=new ArrayList<>();
        timecards=FilesUtil.readTimeTag();
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
        timecards = FilesUtil.readTimeTag();
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