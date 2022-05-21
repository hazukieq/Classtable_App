package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.BasicActivity;
import com.example.classtool.base.OnItemClick;
import com.example.classtool.binders.UniversalBinder;
import com.example.classtool.models.FindSort;
import com.example.classtool.models.QTime;
import com.example.classtool.models.Time_sets;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;
import java.util.List;

public class Scheduldatas extends BasicActivity {

    private QMUITopBarLayout topBarLayout;
    private MultiTypeAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Object> alls=new ArrayList<>();
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduldatas);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        topBarLayout=(QMUITopBarLayout) findViewById(R.id.sche_data_topbar);
        recyclerView=(RecyclerView) findViewById(R.id.sche_data_recy);
        adapter=new MultiTypeAdapter();
        sp= PreferenceManager.getDefaultSharedPreferences(Scheduldatas.this);
        editor=sp.edit();
        iniViews();
    }

    private void  iniViews(){
        topBarLayout.setTitle("作息时间列表");
        List<String> sche_tags=new ArrayList<>();

        sche_tags= FilesUtil.readTimeTag(getApplicationContext());

        if(sche_tags.size()>0){
            for(int i=0;i<sche_tags.size();i++){
                String[] strs=sche_tags.get(i).split(",");
                alls.add(new QTime(0,"",strs[0]));
            }

        }
        UniversalBinder universalBinder=new UniversalBinder();
        universalBinder.setOnItemClick(new OnItemClick() {
            @Override
            public void onClick(View v, int position, int sirtq, String sortq, String str) {
                 List<String> timevalues=new ArrayList<>();
                 timevalues=FilesUtil.readTimeTag(getApplicationContext());
                 int morNums=4;
                 int noonStartCl=5;
                 int ngithStartCl=9;
                 if(timevalues.size()>0) {
                    String[] ptimes=timevalues.get(position).split(",");
                     morNums= FindSort.returnColorSort(Time_sets.class_nums,ptimes[2]);
                     noonStartCl=FindSort.returnColorSort(Time_sets.start_classes,ptimes[3]);
                     ngithStartCl=FindSort.returnColorSort(Time_sets.start_classes,ptimes[5]);
                     String current_time_temp=ptimes[0];
                     editor.putInt("Morning_nums_max",morNums);
                     editor.putInt("Noon_startClass",noonStartCl);
                     editor.putInt("Night_startClass",ngithStartCl);
                     editor.putString("current_time_temp",current_time_temp);
                     editor.commit();
                     Toast.makeText(Scheduldatas.this, "您已选择"+ptimes[0], Toast.LENGTH_SHORT).show();
                 }
               finish();
            }
        });
        adapter.register(QTime.class,universalBinder);
        adapter.setItems(alls);
        recyclerView.setAdapter(adapter);
    }
}