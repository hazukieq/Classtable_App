package com.example.classtool;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.BasicActivity;
import com.example.classtool.binders.ManageScheBinder;
import com.example.classtool.models.SchedulModel;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIEmptyView;

import java.util.ArrayList;
import java.util.List;

public class ManageScheAct extends BasicActivity {
    private RecyclerView recy;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<Object> alls=new ArrayList<>();
    private QMUIEmptyView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sche);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        recy=(RecyclerView) findViewById(R.id.times_add_recy);
        emptyView=(QMUIEmptyView)findViewById(R.id.empty);
       initRecy();
       checkVisible();
    }

    private void initRecy(){
        multiTypeAdapter=new MultiTypeAdapter();
        ManageScheBinder manageScheBinder=new ManageScheBinder();
        manageScheBinder.setOnPItemLClick(new ManageScheBinder.OnPItemLClick() {
            @Override
            public void onDelete(View v,String name, int position) {
                if(FilesUtil.deleteScheFile(getApplicationContext(),name)) {
                    if(alls.size()>0){
                        alls.remove(position);
                        multiTypeAdapter.notifyItemRemoved(position);
                        Toast.makeText(ManageScheAct.this, "删除文件成功！", Toast.LENGTH_SHORT).show();
                    }


                }
                List<String> alsa=FilesUtil.readSchedulAndTimeTag(getApplicationContext());
                FilesUtil.RemoveScheDulAndTimeTag(getApplicationContext(),alsa,name);
            }
        });

        List<String> df= FilesUtil.readSchedulAndTimeTag(getApplicationContext());
        //alls.add(new SchedulModel(0,df.get(0).split(",")[0],"不删除"));
        if(df.size()>0){
            for(int y=1;y<df.size();y++){
                //if(y==0&&df.get(0).split(",")[0].equals("临时课表"))
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

}