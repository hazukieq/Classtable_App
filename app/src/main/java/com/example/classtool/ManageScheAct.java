package com.example.classtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.OnItemClick;
import com.example.classtool.binders.ManageScheBinder;
import com.example.classtool.binders.TimelistBinder;
import com.example.classtool.binders.UniversalBinder;
import com.example.classtool.models.QTime;
import com.example.classtool.models.SchedulModel;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

public class ManageScheAct extends AppCompatActivity {
    private RecyclerView recy;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<Object> alls=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sche);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        recy=(RecyclerView) findViewById(R.id.times_add_recy);
        multiTypeAdapter=new MultiTypeAdapter();
        ManageScheBinder manageScheBinder=new ManageScheBinder();
        manageScheBinder.setOnPItemLClick(new ManageScheBinder.OnPItemLClick() {
            @Override
            public void onDelete(View v,String name, int position) {
                if(FilesUtil.deleteScheFile(name)) {
                    Toast.makeText(ManageScheAct.this, "删除文件成功！", Toast.LENGTH_SHORT).show();
                    alls.remove(position);
                    multiTypeAdapter.notifyItemRemoved(position);
                }
                List<String> alsa=FilesUtil.readSchedulAndTimeTag();
                FilesUtil.RemoveScheDulAndTimeTag(alsa,name);
            }
        });

        List<String> df= FilesUtil.readSchedulAndTimeTag();

        if(df.size()>0){
            for(int y=0;y<df.size();y++){
                alls.add(new SchedulModel(y,df.get(y).split(",")[0],"dl"));
            }
        }
        multiTypeAdapter.register(SchedulModel.class,manageScheBinder);
        multiTypeAdapter.setItems(alls);
        recy.setAdapter(multiTypeAdapter);
    }
}