package com.hazukie.scheduleviews.activity;

import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.Nullable;

import com.drakeet.multitype.MultiTypeAdapter;
import com.hazukie.scheduleviews.R;
import com.hazukie.scheduleviews.base.BaseActivity;
import com.hazukie.scheduleviews.binders.UniBinder;
import com.hazukie.scheduleviews.custom.CRecyclerView;
import com.hazukie.scheduleviews.models.Unimodel;
import com.hazukie.scheduleviews.utils.DisplayHelper;
import com.hazukie.scheduleviews.utils.StatusHelper;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text);
        inits();

    }

    private void inits(){
        CRecyclerView recy=findViewById(R.id.about_recy);
        MultiTypeAdapter mdp=new MultiTypeAdapter();
        List<Object> alls=new ArrayList<>();

        UniBinder uniBinder=new UniBinder();
        uniBinder.setJustify(view -> {
            int pa= DisplayHelper.dp2px(this,16);
            int lr=DisplayHelper.dp2px(this,12);
            view.setPadding(lr,pa,0,pa);
            view.setBackground(getDrawable(R.drawable.setting_item_bg));
            view.setGravity(Gravity.START);
        });

        alls.add(new Unimodel(0,getString(R.string.about_app_name)));
        alls.add(new Unimodel(1,getString(R.string.about_app_author)));
        alls.add(new Unimodel(2,getString(R.string.about_app_address)));
        alls.add(new Unimodel(3,getString(R.string.about_app_update)));
        alls.add(new Unimodel(4,getString(R.string.about_app_contactus)));
        alls.add(new Unimodel(5,getString(R.string.about_app_advise)));
        alls.add(new Unimodel(6,getString(R.string.about_app_cites)));

        mdp.register(Unimodel.class,uniBinder);
        recy.setAdapter(mdp);
        mdp.setItems(alls);

    }
}
