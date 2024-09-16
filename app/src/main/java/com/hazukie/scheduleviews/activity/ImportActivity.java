package com.hazukie.scheduleviews.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.binders.UniBinder;
import com.hazukie.scheduleviews.fileutil.BasicOpts;
import com.hazukie.scheduleviews.fileutil.FileRootTypes;
import com.hazukie.scheduleviews.fileutil.NetFileOpts;
import com.hazukie.scheduleviews.fileutil.OftenOpts;
import com.hazukie.scheduleviews.models.ScheWithTimeModel;
import com.hazukie.scheduleviews.models.TimeModel;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.statics.Statics;
import com.hazukie.scheduleviews.utils.CycleUtil;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.StatusHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text);

        RecyclerView recy=findViewById(R.id.import_files);
        recy.setLayoutManager(new LinearLayoutManager(this));

        MultiTypeAdapter mdp=new MultiTypeAdapter();

        NetFileOpts netFileOpts=NetFileOpts.getInstance(this);
        File dir=netFileOpts.getPublicDir("课表导入");
        List<Unimodel> unis=new ArrayList<>();
        if(dir.isDirectory()&&dir.exists()){
            String[] files=dir.list();
            assert files != null;
            for (String file : files) unis.add(new Unimodel(file.endsWith(".sche")?0:1, file));
        }

        mdp.register(Unimodel.class,new UniBinder());
        mdp.setItems(unis);
        recy.setAdapter(mdp);

        BasicOpts basicOpts=BasicOpts.getInstance(this);
        OftenOpts oftenOpts=OftenOpts.getInstance(this);
        Button import_btn=findViewById(R.id.import_btn);

        List<ScheWithTimeModel> recorded_sches=oftenOpts.getRecordedScts();
        List<TimeModel> recorded_times=oftenOpts.getRecordTms();

        import_btn.setOnClickListener(view -> {
            for(Unimodel uni:unis) {
                File f = netFileOpts.getPublicFile("课表导入", uni.title);
                String new_n = uni.title.replaceAll("\\..*", ".txt");
                if (new File(new_n).exists() && uni.id == 1) continue;
                if(f.exists()) {
                    basicOpts.copy(uni.id == 0 ? FileRootTypes.sches : FileRootTypes.times, f, new_n);
                    if(uni.id==1) recorded_times.add(new TimeModel(0,new_n));
                    else recorded_sches.add(new ScheWithTimeModel(0,new_n,Statics.default_time_file_txt));
                }
            }

            List<TimeModel> timelist= CycleUtil.distinct(recorded_times);
            List<ScheWithTimeModel> schelist=CycleUtil.distinct(recorded_sches);
            oftenOpts.putRawSctzList(schelist);
            oftenOpts.putThms(timelist);
            DisplayHelper.Infost(this,"导入成功！");
        });
    }
}