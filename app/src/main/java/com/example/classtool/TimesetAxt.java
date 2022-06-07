package com.example.classtool;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drakeet.multitype.MultiTypeAdapter;
import com.example.classtool.base.BasicActivity;
import com.example.classtool.base.OnItemClick;
import com.example.classtool.binders.QTimeBinder;
import com.example.classtool.models.QTime;
import com.example.classtool.utils.FilesUtil;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.List;

public class TimesetAxt extends BasicActivity {
    private QMUITopBarLayout topBarLayout;
    private RecyclerView recy;
    private TextView saving;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<Object> alls=new ArrayList<>();
    private String a="";
    private QMUISkinValueBuilder builder;
    private QMUIFrameLayout frameLayout;
    private View mainlayout;
    private QMUIFullScreenPopup popups;
    private QMUIRoundButton time_add_pup_save_btn;
    private ImageButton time_add_pup_close_btn;
    private  TextView time_add_pup_autoId,hintq;
    private EditText time_add_pup_setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeset_axt);
        Intent iq=getIntent();
        a=iq.getStringExtra("sche_name");
        initPopupView();
        initsViews();

    }

    private void initsViews(){
        topBarLayout=(QMUITopBarLayout) findViewById(R.id.timesetaxt_topbar);
        topBarLayout.setTitle(a);
        topBarLayout.setBackgroundColor(getColor(R.color.light_white));
        recy=(RecyclerView) findViewById(R.id.timesetaxt_add_recy);
        saving=(TextView) findViewById(R.id.timesetaxt_add_saving);
        hintq=(TextView)findViewById(R.id.timesetaxt_hint);
        multiTypeAdapter=new MultiTypeAdapter();
        QTimeBinder qTimeBinder=new QTimeBinder();

        saving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<QTime> sall=new ArrayList<>();
                for(int f=0;f<alls.size();f++){
                    sall.add((QTime) alls.get(f));
                }
                FilesUtil.AppendClassTime(getApplicationContext(),sall,a);

                //Toast.makeText(TimesetAxt.this, "数据更新成功！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        qTimeBinder.setOnItemClick(new OnItemClick() {
            @Override
            public void onClick(View v, int position, int sirtq, String sortq, String str) {
                      showAddpup(hintq,position,sirtq,sortq,str);
            }
        });
        multiTypeAdapter.register(QTime.class,qTimeBinder);

        List<QTime> timeqall= FilesUtil.readClassTime(getApplicationContext(),a);
        for(QTime aq:timeqall){
            alls.add(aq);
        }

        multiTypeAdapter.setItems(alls);
        recy.setAdapter(multiTypeAdapter);
    }

    private void initPopupView(){
        builder = QMUISkinValueBuilder.acquire();
        frameLayout = new QMUIFrameLayout(TimesetAxt.this);
        frameLayout.setBackground(
                QMUIResHelper.getAttrDrawable(TimesetAxt.this, com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg));
        builder.background(com.qmuiteam.qmui.R.attr.qmui_skin_support_popup_bg);
        QMUISkinHelper.setSkinValue(frameLayout, builder);
        frameLayout.setRadius(QMUIDisplayHelper.dp2px(TimesetAxt.this, 12));
        int padding = QMUIDisplayHelper.dp2px(TimesetAxt.this, 8);
        frameLayout.setPadding(padding, padding, padding, padding);
        int size = QMUIDisplayHelper.dp2px(TimesetAxt.this, 300);
        int height = QMUIDisplayHelper.dp2px(TimesetAxt.this, 400);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, height);

        mainlayout = View.inflate(TimesetAxt.this, R.layout.time_add_item_popup, null);
        time_add_pup_close_btn = (ImageButton) mainlayout.findViewById(R.id.time_add_popup_close_btn);
        time_add_pup_save_btn=(QMUIRoundButton) mainlayout.findViewById(R.id.time_add_save_btn);
        time_add_pup_autoId = (TextView) mainlayout.findViewById(R.id.time_add_popup_id);
        time_add_pup_setTime = (EditText) mainlayout.findViewById(R.id.time_add_popup_setTime);

        Glide.with(mainlayout).load(R.drawable.ic_action_close).into(time_add_pup_close_btn);

        frameLayout.addView(mainlayout, lp);
        popups = new QMUIFullScreenPopup(TimesetAxt.this);

    }

    private void showAddpup(View AttachView,int position,int sort,String sortStr,String content){
        popups.addView(frameLayout)
                .skinManager(QMUISkinManager.defaultInstance(TimesetAxt.this))
                .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                    @Override
                    public void onBlankClick(QMUIFullScreenPopup popup) {
                    }
                })
                .show(AttachView);

        time_add_pup_autoId.setText(sortStr);
        if(content.equals("上课时间-下课时间")){
            time_add_pup_setTime.setText("");
        }else{
            time_add_pup_setTime.setText(content.replace(":",""));
        }


        time_add_pup_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popups.dismiss();
            }
        });

        time_add_pup_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStr=time_add_pup_setTime.getText().toString();
                alls.remove(position);
                alls.add(position,new QTime(sort,sortStr, timeStr.replaceAll("(\\d{2})(.*)(\\d{2})$","$1:$2:$3")));
                multiTypeAdapter.notifyItemChanged(position,"updating");
                popups.dismiss();
            }
        });
    }
}